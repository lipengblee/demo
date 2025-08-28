package com.star.lp.module.product.service.print;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.star.lp.framework.common.exception.ServiceException;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.infra.api.file.FileApi;
import com.star.lp.module.infra.framework.file.core.client.FileClient;
import com.star.lp.module.product.controller.admin.spu.vo.ProductSkuSaveReqVO;
import com.star.lp.module.product.controller.admin.spu.vo.ProductSpuSaveReqVO;
import com.star.lp.module.product.controller.app.print.vo.*;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.dataobject.printconfig.PrintConfigDO;
import com.star.lp.module.product.dal.dataobject.property.ProductPropertyDO;
import com.star.lp.module.product.dal.dataobject.property.ProductPropertyValueDO;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentMapper;
import com.star.lp.module.product.service.printconfig.PrintConfigService;
import com.star.lp.module.product.service.property.ProductPropertyService;
import com.star.lp.module.product.service.property.ProductPropertyValueService;
import com.star.lp.module.product.service.spu.ProductSpuService;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.module.product.enums.ErrorCodeConstants.*;
import static com.star.lp.module.product.enums.PrintErrorCodeConstants.*;
import static com.google.common.io.Files.getFileExtension;

/**
 * 打印文档 Service 实现类
 */
@Service
@Validated
@Slf4j
public class ProductPrintDocumentServiceImpl implements ProductPrintDocumentService {

    @Resource
    private ProductPrintDocumentMapper printDocumentMapper;

    @Resource
    private FileApi fileApi;

    @Resource
    private ProductSpuService productSpuService;

    @Resource
    private ProductPropertyService productPropertyService;

    @Resource
    private ProductPropertyValueService productPropertyValueService;

    @Resource
    private ProductPrintDocumentSpuService documentSpuService;

    @Resource
    private PrintConfigService printConfigService;

    // 支持的文件类型
    private static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "jpg", "jpeg", "png", "bmp"
    );

    // 单个文件最大大小（50MB）
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    // 用户最大文档数量
    private static final long MAX_DOCUMENTS_PER_USER = 100;

    // 定义时间格式：年月日时分秒（例如：20240615143025）
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductPrintDocumentDO uploadDocument(Long userId, MultipartFile file, String name) {
        // 1. 基础校验
        validateFile(file);
        validateUserDocumentCount(userId);
        // 2. 计算文件哈希值，检查是否已存在
        String fileHash = calculateFileHash(file);
        ProductPrintDocumentDO existingDoc = printDocumentMapper.selectByUserIdAndFileHash(userId, fileHash);
        if (existingDoc != null) {
            return existingDoc; // 如果文件已存在，直接返回
        }
        // 3. 上传文件
        String fileUrl = uploadFileToStorage(file);

        // 4. 解析文档信息
        AppProductPrintDocumentInfoRespVO docInfo = parseDocumentInfo(file);

        // 5. 生成缩略图（异步处理）- 未完成
        String thumbnailUrl = generateThumbnail(file, fileUrl);

        // 6. 保存到数据库
        ProductPrintDocumentDO document = ProductPrintDocumentDO.builder()
                .userId(userId)
                .name(StrUtil.isBlank(name) ? docInfo.getFileName() : name)
                .originalName(file.getOriginalFilename())
                .fileType(docInfo.getFileType())
                .fileSize(docInfo.getFileSize())
                .fileUrl(fileUrl)
                .thumbnailUrl(thumbnailUrl)
                .pageCount(docInfo.getPageCount())
                .status(ProductPrintDocumentDO.STATUS_NORMAL)
                .fileHash(fileHash)
                .build();

        printDocumentMapper.insert(document);

        log.info("用户 {} 上传文档成功，文档ID: {}, 文件名: {}", userId, document.getId(), document.getName());
        return document;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProductPrintDocumentDO> batchUploadDocuments(Long userId, MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw exception(PRINT_DOCUMENT_FILES_EMPTY);
        }

        // 检查批量上传数量限制
        if (files.length > 20) {
            throw exception(PRINT_DOCUMENT_BATCH_LIMIT_EXCEEDED);
        }

        List<ProductPrintDocumentDO> documents = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                ProductPrintDocumentDO document = uploadDocument(userId, file, null);
                documents.add(document);
            } catch (Exception e) {
                log.error("批量上传文档失败，文件名: {}, 错误: {}", file.getOriginalFilename(), e.getMessage());
                // 继续处理其他文件，不中断整个批量上传流程
            }
        }

        return documents;
    }

    @Override
    public PageResult<ProductPrintDocumentDO> getDocumentPage(AppProductPrintDocumentPageReqVO pageReqVO) {
        return printDocumentMapper.selectPage(pageReqVO);
    }

    @Override
    public ProductPrintDocumentDO getDocument(Long userId, Long id) {
        return validateDocumentOwner(userId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocument(Long userId, AppProductPrintDocumentUpdateReqVO updateReqVO) {
        // 校验文档所有权
        ProductPrintDocumentDO document = validateDocumentOwner(userId, updateReqVO.getId());

        // 更新文档信息
        document.setName(updateReqVO.getName());
        printDocumentMapper.updateById(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long userId, Long id) {
        // 校验文档所有权
        ProductPrintDocumentDO document = validateDocumentOwner(userId, id);

        // 检查文档是否可以删除（未被订单使用）
        if (!documentSpuService.canDeleteDocument(id, userId)) {
            throw exception(PRINT_DOCUMENT_CANNOT_DELETE_USED_BY_ORDER);
        }

        // 删除文档与SPU的关联关系
        documentSpuService.deleteByDocumentId(id);

        // 逻辑删除文档
        printDocumentMapper.logicDeleteByUserIdAndId(userId, id);

        log.info("用户 {} 删除文档成功，文档ID: {}", userId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppProductPrintSpuCreateRespVO createPrintSpu(Long userId, AppProductPrintSpuCreateReqVO createReqVO) {
        // 1. 校验文档所有权
        List<ProductPrintDocumentDO> documents = printDocumentMapper.selectByUserIdAndIds(userId, createReqVO.getDocumentIds());
        if (documents.size() != createReqVO.getDocumentIds().size()) {
            throw exception(PRINT_DOCUMENT_NOT_EXISTS);
        }
        // 2. 校验打印选项
        validatePrintOptions(createReqVO.getPrintOptions());
        // 3. 构建SPU基本信息
        ProductSpuSaveReqVO spuSaveReqVO = buildPrintSpuSaveReqVO(createReqVO, documents);
        // 4. 构建SKU信息（基于打印选项组合）
        List<ProductSkuSaveReqVO> skuList = buildPrintSkuList(createReqVO.getPrintOptions(), documents);
        spuSaveReqVO.setSkus(skuList);
        // 5. 创建商品SPU
        Long spuId = productSpuService.createSpu(spuSaveReqVO);
        // 6. 创建文档与SPU的关联关系
        documentSpuService.createDocumentSpuRelations(spuId, createReqVO.getDocumentIds());
        // 7. 获取创建的SKU ID
        Long skuId = null;
        if (skuList != null && !skuList.isEmpty()) {
            // 方法1: 通过SPU ID查询关联的SKU
            List<Long> skuIds = productSpuService.getSkuIdsBySpuId(spuId);
            if (!skuIds.isEmpty()) {
                skuId = skuIds.get(0);
            }
        }
        if (skuId == null) {
            throw new ServiceException(401, "创建SKU失败，无法获取SKU ID");
        }

        AppProductPrintSpuCreateRespVO respVO = new AppProductPrintSpuCreateRespVO();
        respVO.setSpuId(spuId);
        respVO.setSkuId(skuId);
        respVO.setCategoryId(spuSaveReqVO.getCategoryId());

        log.info("用户 {} 创建打印商品成功，SPU ID: {}, 文档数量: {}", userId, spuId, skuId, documents.size());
        return respVO;
    }

    @Override
    public AppProductPrintDocumentPreviewRespVO previewDocument(Long userId, Long id) {
        // 校验文档所有权
        ProductPrintDocumentDO document = validateDocumentOwner(userId, id);

        // 生成预览信息
        AppProductPrintDocumentPreviewRespVO preview = new AppProductPrintDocumentPreviewRespVO();
        preview.setId(document.getId());
        preview.setName(document.getName());
        preview.setPageCount(document.getPageCount());
        preview.setFileSize(document.getFileSize());

        // 生成预览图片URL（这里需要根据实际的预览服务来实现）
        List<String> previewUrls = generatePreviewUrls(document);
        preview.setPreviewUrls(previewUrls);

        // 设置建议的打印设置
        AppProductPrintDocumentPreviewRespVO.PrintSetting suggestedSetting = suggestPrintSetting(document);
        preview.setSuggestedSetting(suggestedSetting);

        return preview;
    }

    @Override
    public List<AppPrintSettingOptionRespVO> getPrintOptionsType() {
        // 商品类型 1-实物商品 2-云打印服务
        List<ProductPropertyDO> cloudPrintProperties = productPropertyService.getPropertyListByProductType(2);
        List<AppPrintSettingOptionRespVO> result = new ArrayList<>();
        cloudPrintProperties.forEach(item -> {
            AppPrintSettingOptionRespVO option = new AppPrintSettingOptionRespVO();
            option.setId(item.getId());
            option.setName(item.getName());
            option.setRemark(item.getRemark());
            option.setSort(item.getSort());

            List<ProductPropertyValueDO> propertyValues = productPropertyValueService.getPropertyValueListByPropertyId(Collections.singleton(item.getId()));

            // 转换属性值为响应对象
            List<AppPrintSettingOptionValueRespVO> values = propertyValues.stream()
                    .map(value -> {
                        AppPrintSettingOptionValueRespVO valueVO = new AppPrintSettingOptionValueRespVO();
                        valueVO.setOptionId(value.getPropertyId());
                        valueVO.setId(value.getId());
                        valueVO.setValue(value.getName());
                        valueVO.setPrice(value.getPrice());
                        valueVO.setRemark(value.getRemark());
                        return valueVO;
                    })
                    .collect(Collectors.toList());
            option.setValues(values);
            result.add(option);
        });
        return result;
    }


    @Override
    public AppProductPrintDocumentInfoRespVO parseDocumentInfo(MultipartFile file) {
        AppProductPrintDocumentInfoRespVO info = new AppProductPrintDocumentInfoRespVO();

        String fileName = file.getOriginalFilename();
        String fileType = FileUtil.extName(fileName).toLowerCase();
        long fileSize = file.getSize();

        info.setFileName(fileName);
        info.setFileType(fileType);
        info.setFileSize(fileSize);
        info.setSupportPrint(SUPPORTED_FILE_TYPES.contains(fileType));

        if (!info.getSupportPrint()) {
            info.setUnsupportedReason("不支持的文件格式，支持的格式有：" + String.join(", ", SUPPORTED_FILE_TYPES));
        } else {
            // 解析页数（这里需要根据文件类型使用不同的解析方法）
            info.setPageCount(parsePageCount(file, fileType));
        }

        return info;
    }

    @Override
    public ProductPrintDocumentDO validateDocumentOwner(Long userId, Long id) {
        ProductPrintDocumentDO document = printDocumentMapper.selectByUserIdAndId(userId, id);
        if (document == null) {
            throw exception(PRINT_DOCUMENT_NOT_EXISTS);
        }
        return document;
    }

    // ========== 私有方法 ==========

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw exception(PRINT_DOCUMENT_FILE_EMPTY);
        }

        String fileName = file.getOriginalFilename();
        if (StrUtil.isBlank(fileName)) {
            throw exception(PRINT_DOCUMENT_FILENAME_INVALID);
        }

        String fileType = FileUtil.extName(fileName).toLowerCase();
        if (!SUPPORTED_FILE_TYPES.contains(fileType)) {
            throw exception(PRINT_DOCUMENT_TYPE_NOT_SUPPORTED, String.join(", ", SUPPORTED_FILE_TYPES));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw exception(PRINT_DOCUMENT_SIZE_EXCEEDED, MAX_FILE_SIZE / 1024 / 1024);
        }
    }

    private void validateUserDocumentCount(Long userId) {
        Long documentCount = printDocumentMapper.countByUserId(userId);
        if (documentCount >= MAX_DOCUMENTS_PER_USER) {
            throw exception(PRINT_DOCUMENT_COUNT_EXCEEDED, MAX_DOCUMENTS_PER_USER);
        }
    }

    private String calculateFileHash(MultipartFile file) {
        try {
            return DigestUtil.md5Hex(file.getInputStream());
        } catch (IOException e) {
            throw exception(PRINT_DOCUMENT_HASH_FAILED);
        }
    }

    private String uploadFileToStorage(MultipartFile file) {
        try {
            //屏蔽文件名称
            String path = fileApi.createFile(file.getBytes(), null, null, null);
            return path;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw exception(PRINT_DOCUMENT_UPLOAD_FAILED);
        }
    }

    private String generateThumbnail(MultipartFile file, String fileUrl) {
        // TODO: 实现缩略图生成逻辑
        // 这里可以调用第三方服务或使用本地工具生成缩略图
        return null;
    }

    private Integer parsePageCount(MultipartFile file, String fileType) {
        if (file == null || file.isEmpty()) {
            return 0;
        }

        try (InputStream inputStream = file.getInputStream()) {
            switch (fileType.toLowerCase()) {
                case "pdf":
                    return parsePdfPageCount(inputStream);
                case "doc":
                    return parseDocPageCount(inputStream);
                case "docx":
                    return parseDocxPageCount(inputStream);
                case "xls":
                    return parseXlsPageCount(inputStream);
                case "xlsx":
                    return parseXlsxPageCount(inputStream);
                case "ppt":
                    return parsePptPageCount(inputStream);
                case "pptx":
                    return parsePptxPageCount(inputStream);
                case "txt":
                    return parseTxtPageCount(inputStream);
                case "jpg":
                case "jpeg":
                case "png":
                case "bmp":
                    return parseImagePageCount(inputStream);
                default:
                    return 1; // 未知格式默认返回1页
            }
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("解析文件页数时出错: " + e.getMessage());
            return 0; // 出错时默认返回0页
        }
    }

    /**
     * 解析PDF文件页数
     */
    private Integer parsePdfPageCount(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            return document.getNumberOfPages();
        }
    }

    /**
     * 解析DOC文件页数
     */
    private Integer parseDocPageCount(InputStream inputStream) throws IOException {
        try (HWPFDocument document = new HWPFDocument(inputStream)) {
            // DOC文件页数计算比较复杂，这里使用字符数估算
            // 实际项目中可能需要更精确的计算方法
            int totalChars = document.getDocumentText().length();
            // 假设每页约2000个字符（可根据实际情况调整）
            return Math.max(1, (int) Math.ceil(totalChars / 2000.0));
        }
    }

    /**
     * 解析DOCX文件页数
     */
    private Integer parseDocxPageCount(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            // 获取文档的页数属性
            try {
                // 尝试从文档属性中获取页数
                if (document.getProperties() != null &&
                        document.getProperties().getExtendedProperties() != null &&
                        document.getProperties().getExtendedProperties().getPages() > 0) {
                    return document.getProperties().getExtendedProperties().getPages();
                }
            } catch (Exception e) {
                // 忽略属性获取失败
            }

            // 如果无法从属性获取，使用段落数估算
            int paragraphs = document.getParagraphs().size();
            // 假设每页约25个段落（可根据实际情况调整）
            return Math.max(1, (int) Math.ceil(paragraphs / 25.0));
        }
    }

    /**
     * 解析XLS文件页数（工作表数）
     */
    private Integer parseXlsPageCount(InputStream inputStream) throws IOException {
        try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {
            return workbook.getNumberOfSheets();
        }
    }

    /**
     * 解析XLSX文件页数（工作表数）
     */
    private Integer parseXlsxPageCount(InputStream inputStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            return workbook.getNumberOfSheets();
        }
    }

    /**
     * 解析PPT文件页数（幻灯片数）
     */
    private Integer parsePptPageCount(InputStream inputStream) throws IOException {
        try (HSLFSlideShow ppt = new HSLFSlideShow(inputStream)) {
            return ppt.getSlides().size();
        }
    }

    /**
     * 解析PPTX文件页数（幻灯片数）
     */
    private Integer parsePptxPageCount(InputStream inputStream) throws IOException {
        try (XMLSlideShow ppt = new XMLSlideShow(inputStream)) {
            return ppt.getSlides().size();
        }
    }

    /**
     * 解析TXT文件页数
     */
    private Integer parseTxtPageCount(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            int lineCount = 0;
            while (reader.readLine() != null) {
                lineCount++;
            }
            // 假设每页50行（可根据实际情况调整）
            return Math.max(1, (int) Math.ceil(lineCount / 50.0));
        }
    }

    /**
     * 解析图片文件页数（图片文件始终为1页）
     */
    private Integer parseImagePageCount(InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        return (image != null) ? 1 : 0;
    }

    private void validatePrintOptions(List<AppProductPrintSpuCreateReqVO.PrintOption> printOptions) {
        if (CollUtil.isEmpty(printOptions)) {
            throw exception(PRINT_OPTIONS_EMPTY);
        }

        // 校验属性和属性值是否存在
        List<Long> propertyIds = printOptions.stream()
                .map(AppProductPrintSpuCreateReqVO.PrintOption::getPropertyId)
                .collect(Collectors.toList());

        List<Long> valueIds = printOptions.stream()
                .map(AppProductPrintSpuCreateReqVO.PrintOption::getValueId)
                .collect(Collectors.toList());

        List<ProductPropertyDO> properties = productPropertyService.getPropertyList(propertyIds);
        if (properties.size() != propertyIds.size()) {
            throw exception(PROPERTY_NOT_EXISTS);
        }

        List<ProductPropertyValueDO> propertyValues = productPropertyValueService.getPropertyValueListByIds(valueIds);
        if (propertyValues.size() != valueIds.size()) {
            throw exception(PROPERTY_VALUE_NOT_EXISTS);
        }

        // 创建属性ID到属性值的映射
        Map<Long, ProductPropertyValueDO> valueMap = propertyValues.stream()
                .collect(Collectors.toMap(ProductPropertyValueDO::getId, value -> value));

        // 验证每个选项的属性值是否属于对应的属性
        for (AppProductPrintSpuCreateReqVO.PrintOption option : printOptions) {
            ProductPropertyValueDO value = valueMap.get(option.getValueId());
            if (value == null || !value.getPrice().equals(option.getExtraPrice())) {
                throw exception(PROPERTY_VALUE_NOT_MATCH_PROPERTY);
            }
        }

    }

    /**
     * 生成包含当前时间和名称的字符串
     *
     * @param name 关联的名称
     * @return 格式为[名称_时间戳]的字符串（例如：文档打印_20240615143025）
     */
    public static String generate(String name) {
        Objects.requireNonNull(name, "关联名称不能为空");
        return generate(name, LocalDateTime.now());
    }

    /**
     * 生成包含指定时间和名称的字符串
     *
     * @param name     关联的名称
     * @param dateTime 指定的时间
     * @return 格式为[名称_时间戳]的字符串
     */
    public static String generate(String name, LocalDateTime dateTime) {
        Objects.requireNonNull(name, "关联名称不能为空");
        Objects.requireNonNull(dateTime, "时间不能为空");
        return name + "_" + dateTime.format(DEFAULT_FORMATTER);
    }


    /**
     * 构建打印商品SPU保存请求对象
     *
     * @param createReqVO 创建打印商品的请求对象
     * @param documents   打印文档列表
     * @return 构建好的商品SPU保存请求对象
     */
    private ProductSpuSaveReqVO buildPrintSpuSaveReqVO(AppProductPrintSpuCreateReqVO createReqVO,
                                                       List<ProductPrintDocumentDO> documents) {
        //获取打印相关配置
        String print_pic_url = printConfigService.getPrintConfigByConfigKey("print_pic_url");
        String give_integral = printConfigService.getPrintConfigByConfigKey("give_integral");
        String print_generate_keyword = printConfigService.getPrintConfigByConfigKey("print_generate_keyword");
        String print_delivery_types = printConfigService.getPrintConfigByConfigKey("print_delivery_types");
        String print_delivery_template_id = printConfigService.getPrintConfigByConfigKey("print_delivery_template_id");
        List<Integer> typesList = Arrays.stream(print_delivery_types.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        String print_product_category = printConfigService.getPrintConfigByConfigKey("print_product_category");
        String print_product_brand = printConfigService.getPrintConfigByConfigKey("print_product_brand");

        ProductSpuSaveReqVO spuSaveReqVO = new ProductSpuSaveReqVO();
        spuSaveReqVO.setName(generate(createReqVO.getSpuName()));
        spuSaveReqVO.setIntroduction(generate(createReqVO.getIntroduction()));
        spuSaveReqVO.setSort(100); //排序默认
        spuSaveReqVO.setPicUrl(print_pic_url); //图片默认
        spuSaveReqVO.setGiveIntegral(Integer.parseInt(give_integral)); //积分默认
        spuSaveReqVO.setKeyword(generate(print_generate_keyword));
        spuSaveReqVO.setDeliveryTypes(typesList); //1 快递、2 自提
        spuSaveReqVO.setSubCommissionType(false); // 分销关闭，未来优化
        spuSaveReqVO.setProductType(2); // 商品类型 1-实物商品 2-云打印服务
        spuSaveReqVO.setDeliveryTemplateId(Long.parseLong(print_delivery_template_id)); // 配送模板ID

        // 设置商品描述（包含文档信息）
        StringBuilder description = new StringBuilder();
        description.append("打印文档包含：");
        for (ProductPrintDocumentDO doc : documents) {
            description.append("- ").append(doc.getName())
                    .append(" (").append(doc.getPageCount()).append("页)--");
        }
        spuSaveReqVO.setDescription(description.toString());

        // 设置默认分类和品牌（这里需要根据实际业务设置）
        spuSaveReqVO.setCategoryId(Long.valueOf(print_product_category)); // 打印指定服务分类
        spuSaveReqVO.setBrandId(Long.valueOf(print_product_brand));    // 打印指定品牌

        // 设置商品图片（使用第一个文档的缩略图）
        if (!documents.isEmpty() && StrUtil.isNotBlank(documents.get(0).getThumbnailUrl())) {
            spuSaveReqVO.setPicUrl(documents.get(0).getThumbnailUrl());
        }

        spuSaveReqVO.setSpecType(true); // 多规格商品
        return spuSaveReqVO;
    }

    private List<ProductSkuSaveReqVO> buildPrintSkuList(List<AppProductPrintSpuCreateReqVO.PrintOption> printOptions,
                                                        List<ProductPrintDocumentDO> documents) {
        List<ProductSkuSaveReqVO> skuList = new ArrayList<>();

        //为每个打印选项创建一个SKU
        ProductSkuSaveReqVO sku = new ProductSkuSaveReqVO();

        // 设置SKU属性
        List<ProductSkuSaveReqVO.Property> properties = new ArrayList<>();

        int extraPrice = 0; //初始值

        for (AppProductPrintSpuCreateReqVO.PrintOption option : printOptions) {
            ProductPropertyDO productPropertyDO = productPropertyService.getProperty(option.getPropertyId());
            ProductPropertyValueDO productPropertyValueDO = productPropertyValueService.getPropertyValue(option.getValueId());
            ProductSkuSaveReqVO.Property property = new ProductSkuSaveReqVO.Property()
                    .setPropertyId(option.getPropertyId()).setPropertyName(productPropertyDO.getName())
                    .setValueId(option.getValueId()).setValueName(productPropertyValueDO.getName())
                    .setSinglePrice(productPropertyValueDO.getPrice());
            properties.add(property);
            sku.setProperties(properties);
            extraPrice += option.getExtraPrice();
        }

        // 计算价格（基础价格 + 额外价格 * 总页数）
        int totalPages = documents.stream().mapToInt(ProductPrintDocumentDO::getPageCount).sum();

        int print_base_price = Integer.parseInt(printConfigService.getPrintConfigByConfigKey("print_base_price"));
        int basePrice = print_base_price; // 基础价格

        sku.setPrice(basePrice + extraPrice * totalPages);

        sku.setMarketPrice(sku.getPrice() + 1000); // 市场价稍高
        sku.setCostPrice(sku.getPrice());   // 成本价

        sku.setName(generate("文档SKU名称"));
        sku.setPicUrl(printConfigService.getPrintConfigByConfigKey("print_pic_url"));   //图片

        sku.setStock(999); // 打印服务库存充足

        skuList.add(sku);
        return skuList;
    }

    private List<String> generatePreviewUrls(ProductPrintDocumentDO document) {
        // TODO: 实现预览图片URL生成逻辑
        // 可以调用文档预览服务，生成每一页的预览图片
        List<String> urls = new ArrayList<>();
        if (StrUtil.isNotBlank(document.getThumbnailUrl())) {
            urls.add(document.getThumbnailUrl());
        }
        return urls;
    }

    private AppProductPrintDocumentPreviewRespVO.PrintSetting suggestPrintSetting(ProductPrintDocumentDO document) {
        AppProductPrintDocumentPreviewRespVO.PrintSetting setting =
                new AppProductPrintDocumentPreviewRespVO.PrintSetting();

        // 根据文档类型和特征推荐打印设置
        setting.setPaperSize("A4");
        setting.setOrientation("portrait");
        setting.setQuality("high");
        setting.setColorMode("color");
        setting.setDuplex(document.getPageCount() > 2);

        return setting;
    }

    //---------------------------------------------------------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductPrintDocumentDO mergeDocumentsToPdf(Long userId, AppProductPrintDocumentMergeReqVO mergeReqVO) {
        // 1. 校验文档所有权和存在性
        List<ProductPrintDocumentDO> documents = validateDocumentsForMerge(userId, mergeReqVO.getDocumentIds());

        // 2. 检查用户文档数量限制
        validateUserDocumentCount(userId);

        // 3. 执行文档合成
        byte[] mergedPdfBytes = executeDocumentMerge(documents);

        // 4. 上传合成后的PDF文件
        String mergedFileUrl = uploadMergedPdf(mergedPdfBytes, mergeReqVO.getPdfName());

        // 5. 计算文件哈希值，检查重复
        String fileHash = DigestUtil.md5Hex(mergedPdfBytes);
        ProductPrintDocumentDO existingDoc = printDocumentMapper.selectByUserIdAndFileHash(userId, fileHash);
        if (existingDoc != null) {
            return existingDoc;
        }

        // 6. 生成缩略图
        String thumbnailUrl = generatePdfThumbnail(mergedPdfBytes);

        // 7. 计算总页数
        int totalPages = calculateTotalPages(mergedPdfBytes);

        // 8. 保存到数据库
        ProductPrintDocumentDO mergedDocument = ProductPrintDocumentDO.builder()
                .userId(userId)
                .name(mergeReqVO.getPdfName())
                .originalName(mergeReqVO.getPdfName())
                .fileType("pdf")
                .fileSize((long) mergedPdfBytes.length)
                .fileUrl(mergedFileUrl)
                .thumbnailUrl(thumbnailUrl)
                .pageCount(totalPages)
                .status(ProductPrintDocumentDO.STATUS_NORMAL)
                .fileHash(fileHash)
                .remark("由" + documents.size() + "个文档合成")
                .build();

        printDocumentMapper.insert(mergedDocument);

        log.info("用户 {} 合成PDF成功，合成文档数量: {}, 新PDF ID: {}",
                userId, documents.size(), mergedDocument.getId());

        return mergedDocument;
    }
    
    private byte[] executeDocumentMerge(List<ProductPrintDocumentDO> documents) {
        List<PDDocument> sourceDocuments = new ArrayList<>();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PDDocument mergedDocument = new PDDocument()) {

            // 使用PDFMergerUtility来合并文档
            PDFMergerUtility merger = new PDFMergerUtility();

            // 首先将所有文档转换为PDF并加载到PDDocument中
            for (ProductPrintDocumentDO doc : documents) {
                try {
                    byte[] convertedPdfBytes = convertDocumentToPdf(doc);
                    PDDocument sourceDoc = PDDocument.load(new ByteArrayInputStream(convertedPdfBytes));
                    sourceDocuments.add(sourceDoc);
                    merger.appendDocument(mergedDocument, sourceDoc);
                } catch (Exception e) {
                    log.error("处理文档失败: {}, 错误: {}", doc.getName(), e.getMessage());
                }
            }

            // 保存合并后的文档
            mergedDocument.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("文档合成失败", e);
            throw exception(DOCUMENT_MERGE_FAILED);
        } finally {
            // 确保关闭所有源文档
            for (PDDocument doc : sourceDocuments) {
                try {
                    doc.close();
                } catch (IOException e) {
                    log.error("关闭PDF文档失败", e);
                }
            }
        }
    }

    // 6. 文档转PDF方法
    private byte[] convertDocumentToPdf(ProductPrintDocumentDO doc) {
        String fileType = doc.getFileType().toLowerCase();

        try {
            // 下载原始文件
            byte[] fileBytes = downloadFile(doc.getFileUrl());

            switch (fileType) {
                case "pdf":
                    return fileBytes; // PDF文件直接返回
                case "doc":
                case "docx":
                    return convertWordToPdf(fileBytes,fileType);
//                case "ppt":
//                case "pptx":
//                    return convertPowerPointToPdf(fileBytes,fileType);
                case "jpg":
                case "jpeg":
                case "png":
                case "bmp":
                    return convertImageToPdf(fileBytes,fileType);
                default:
                    throw new ServiceException(400, "不支持的文件格式: " + fileType);
            }
        } catch (Exception e) {
            log.error("文档转PDF失败: {}", doc.getName(), e);
            throw exception(DOCUMENT_CONVERT_FAILED, doc.getName());
        }
    }

    // 7. Word转PDF
    private byte[] convertWordToPdf(byte[] wordBytes, String fileType) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PDDocument pdfDocument = new PDDocument()) {

            if ("docx".equals(fileType)) {
                try (XWPFDocument wordDocument = new XWPFDocument(new ByteArrayInputStream(wordBytes))) {
                    convertWordDocumentToPdf(wordDocument,pdfDocument);
                }
            } else {
                try (HWPFDocument wordDocument = new HWPFDocument(new ByteArrayInputStream(wordBytes))) {
                    convertLegacyWordToPdf(wordDocument,pdfDocument);
                }
            }

            pdfDocument.save(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new ServiceException(500, "Word转PDF失败: " + e.getMessage());
        }
    }

    /**
     * 转换docx文档为PDF（写入到已创建的PDDocument）
     */
    private void convertWordDocumentToPdf(XWPFDocument wordDocument, PDDocument pdfDocument) throws IOException {
        // 使用XWPFConverter将docx内容转换到临时输出流
        try (ByteArrayOutputStream tempOut = new ByteArrayOutputStream()) {
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(wordDocument, tempOut, options);

            // 将临时流中的PDF内容合并到目标PDDocument
            try (PDDocument tempDoc = PDDocument.load(tempOut.toByteArray())) {
                for (PDPage page : tempDoc.getPages()) {
                    pdfDocument.addPage(page);
                }
            }
        }
    }

    /**
     * 转换doc文档为PDF（写入到已创建的PDDocument）
     * 注意：HWPF对doc转PDF支持有限，复杂格式可能会有问题
     */
    private void convertLegacyWordToPdf(HWPFDocument wordDocument, PDDocument pdfDocument) throws IOException {
        // 提取doc文档文本内容（简单转换示例）
        String content = String.valueOf(wordDocument.getText());

        // 创建PDF页面并写入文本
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700); // 页边距
            String[] lines = content.split("\n");
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15); // 行间距
            }
            contentStream.endText();
        }
    }

    // 10. 图片转PDF
    private byte[] convertImageToPdf(byte[] imageBytes, String fileType) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PDDocument pdfDocument = new PDDocument()) {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                throw new ServiceException(400, "无法读取图片文件");
            }

            // 创建PDF页面
            PDPage page = new PDPage();
            pdfDocument.addPage(page);

            // 将图片添加到页面
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, imageBytes, "image");

            try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                // 计算图片在页面中的位置和大小
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                float imageWidth = image.getWidth();
                float imageHeight = image.getHeight();

                // 保持比例缩放
                float scaleX = (pageWidth - 40) / imageWidth; // 留20像素边距
                float scaleY = (pageHeight - 40) / imageHeight;
                float scale = Math.min(scaleX, scaleY);

                float scaledWidth = imageWidth * scale;
                float scaledHeight = imageHeight * scale;

                float x = (pageWidth - scaledWidth) / 2;
                float y = (pageHeight - scaledHeight) / 2;

                contentStream.drawImage(pdImage, x, y, scaledWidth, scaledHeight);
            }

            pdfDocument.save(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new ServiceException(500, "图片转PDF失败: " + e.getMessage());
        }
    }

    // 11. 添加目录页面
    private void addTableOfContents(PDDocument document, List<ProductPrintDocumentDO> documents) {
        try {
            PDPage tocPage = new PDPage();
            document.addPage(tocPage);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, tocPage)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("目录");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);

                int pageNumber = 2; // 目录页是第1页，内容从第2页开始
                for (ProductPrintDocumentDO doc : documents) {
                    contentStream.showText(doc.getName() + " ............................ " + pageNumber);
                    contentStream.newLineAtOffset(0, -20);
                    pageNumber += doc.getPageCount();
                }

                contentStream.endText();
            }
        } catch (Exception e) {
            log.error("添加目录失败", e);
        }
    }

    // 12. 合并PDF文档
    private void mergePdfDocument(PDDocument targetDocument, byte[] sourcePdfBytes, int startPageNumber) {
        try (PDDocument sourceDocument = PDDocument.load(new ByteArrayInputStream(sourcePdfBytes))) {

            for (int i = 0; i < sourceDocument.getNumberOfPages(); i++) {
                PDPage page = sourceDocument.getPage(i);
                targetDocument.addPage(page);

            }

        } catch (Exception e) {
            log.error("合并PDF失败", e);
            throw new ServiceException(500, "PDF合并失败");
        }
    }

    // 13. 添加页码
    private void addPageNumber(PDDocument document, int pageIndex, int pageNumber) {
        try {
            PDPage page = document.getPage(pageIndex);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, false)) {

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                float pageWidth = page.getMediaBox().getWidth();
                contentStream.newLineAtOffset(pageWidth - 50, 30); // 右下角
                contentStream.showText(String.valueOf(pageNumber));
                contentStream.endText();
            }
        } catch (Exception e) {
            log.error("添加页码失败，页面: {}", pageIndex, e);
        }
    }

    // 14. 校验文档方法
    private List<ProductPrintDocumentDO> validateDocumentsForMerge(Long userId, List<Long> documentIds) {
        if (CollUtil.isEmpty(documentIds)) {
            throw exception(PRINT_DOCUMENT_IDS_EMPTY);
        }

        if (documentIds.size() > 50) {
            throw exception(DOCUMENT_MERGE_LIMIT_EXCEEDED, 50);
        }

        List<ProductPrintDocumentDO> documents = printDocumentMapper.selectByUserIdAndIds(userId, documentIds);
        if (documents.size() != documentIds.size()) {
            throw exception(PRINT_DOCUMENT_NOT_EXISTS);
        }

        // 检查文档状态
        for (ProductPrintDocumentDO doc : documents) {
            if (!ProductPrintDocumentDO.STATUS_NORMAL.equals(doc.getStatus())) {
                throw exception(DOCUMENT_STATUS_INVALID, doc.getName());
            }
        }

        return documents;
    }

    // 15. 下载文件方法
    private byte[] downloadFile(String fileUrl) {
        try {
            return fileApi.getFileContent(fileUrl);
        } catch (Exception e) {
            log.error("下载文件失败: {}", fileUrl, e);
            throw new ServiceException(500, "文件下载失败");
        }
    }

    //上传合成PDF
    private String uploadMergedPdf(byte[] pdfBytes, String fileName) {
        try {
            if (!fileName.toLowerCase().endsWith(".pdf")) {
                fileName += ".pdf";
            }
            return fileApi.createFile(pdfBytes, null, null, null);
        } catch (Exception e) {
            log.error("上传合成PDF失败", e);
            throw exception(PRINT_DOCUMENT_UPLOAD_FAILED);
        }
    }

    //计算PDF页数
    private int calculateTotalPages(byte[] pdfBytes) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            return document.getNumberOfPages();
        } catch (Exception e) {
            log.error("计算PDF页数失败", e);
            return 1;
        }
    }

    // 18. 生成PDF缩略图
    private String generatePdfThumbnail(byte[] pdfBytes) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            if (document.getNumberOfPages() == 0) {
                return null;
            }
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 150, ImageType.RGB);

            try (ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream()) {
                ImageIO.write(image, "jpg", thumbnailStream);

                String thumbnailFileName = "thumbnail_" + System.currentTimeMillis() + ".jpg";
                return fileApi.createFile(thumbnailStream.toByteArray(), thumbnailFileName, null, null);
            }
        } catch (Exception e) {
            log.error("生成PDF缩略图失败", e);
            return null;
        }
    }


}
