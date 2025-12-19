package com.cloudprint.util;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 区域工具类
 */
@Slf4j
public class AreaUtils {

    /**
     * 初始化 SEARCHER
     */
    @SuppressWarnings("InstantiationOfUtilityClass")
    private final static AreaUtils INSTANCE = new AreaUtils();

    /**
     * Area 内存缓存，提升访问速度
     */
    private static Map<Integer, Area> areas;

    private AreaUtils() {
        long now = System.currentTimeMillis();
        areas = new HashMap<>();
        areas.put(Area.ID_GLOBAL, new Area(Area.ID_GLOBAL, "全球", 0, null, new ArrayList<>()));
        areas.put(Area.ID_CHINA, new Area(Area.ID_CHINA, "中国", 1, areas.get(Area.ID_GLOBAL), new ArrayList<>()));
        
        try {
            // 从 csv 中加载数据
            List<CsvRow> rows = CsvUtil.getReader().read(ResourceUtil.getUtf8Reader("area.csv")).getRows();
            if (rows != null && !rows.isEmpty()) {
                rows.remove(0); // 删除 header
                for (CsvRow row : rows) {
                    // 创建 Area 对象
                    Area area = new Area(Integer.valueOf(row.get(0)), row.get(1), Integer.valueOf(row.get(2)),
                            null, new ArrayList<>());
                    // 添加到 areas 中
                    areas.put(area.getId(), area);
                }

                // 构建父子关系
                for (CsvRow row : rows) {
                    Area area = areas.get(Integer.valueOf(row.get(0))); // 自己
                    Area parent = areas.get(Integer.valueOf(row.get(3))); // 父
                    if (area != null && parent != null && !area.equals(parent)) {
                        area.setParent(parent);
                        parent.getChildren().add(area);
                    }
                }
                log.info("启动加载 AreaUtils 成功，共加载 {} 个区域，耗时 ({}) 毫秒", areas.size() - 2, System.currentTimeMillis() - now);
            }
        } catch (Exception e) {
            log.error("启动加载 AreaUtils 失败，耗时 ({}) 毫秒", System.currentTimeMillis() - now, e);
        }
    }

    /**
     * 获得指定编号对应的区域
     *
     * @param id 区域编号
     * @return 区域
     */
    public static Area getArea(Integer id) {
        return areas.get(id);
    }

    /**
     * 获得指定区域对应的编号
     *
     * @param pathStr 区域路径，例如说：河南省/石家庄市/新华区
     * @return 区域
     */
    public static Area parseArea(String pathStr) {
        if (pathStr == null || pathStr.isEmpty()) {
            return null;
        }
        String[] paths = pathStr.split("/");
        Area area = null;
        for (String path : paths) {
            if (area == null) {
                area = CollectionUtils.findFirst(new ArrayList<>(areas.values()), item -> item.getName().equals(path));
            } else {
                area = CollectionUtils.findFirst(area.getChildren(), item -> item.getName().equals(path));
            }
        }
        return area;
    }

    /**
     * 获取所有节点的全路径名称如：河南省/石家庄市/新华区
     *
     * @param areas 地区树
     * @return 所有节点的全路径名称
     */
    public static List<String> getAreaNodePathList(List<Area> areas) {
        List<String> paths = new ArrayList<>();
        if (areas != null) {
            areas.forEach(area -> getAreaNodePathList(area, "", paths));
        }
        return paths;
    }

    /**
     * 构建一棵树的所有节点的全路径名称，并将其存储为 "祖先/父级/子级" 的形式
     *
     * @param node  父节点
     * @param path  全路径名称
     * @param paths 全路径名称列表，省份/城市/地区
     */
    private static void getAreaNodePathList(Area node, String path, List<String> paths) {
        if (node == null) {
            return;
        }
        // 构建当前节点的路径
        String currentPath = path.isEmpty() ? node.getName() : path + "/" + node.getName();
        paths.add(currentPath);
        // 递归遍历子节点
        if (node.getChildren() != null) {
            for (Area child : node.getChildren()) {
                getAreaNodePathList(child, currentPath, paths);
            }
        }
    }

    /**
     * 格式化区域
     *
     * @param id 区域编号
     * @return 格式化后的区域
     */
    public static String format(Integer id) {
        return format(id, " ");
    }

    /**
     * 格式化区域
     *
     * 例如说：
     * 1. id = “静安区”时：上海 上海市 静安区
     * 2. id = “上海市”时：上海 上海市
     * 3. id = “上海”时：上海
     * 4. id = “美国”时：美国
     * 当区域在中国时，默认不显示中国
     *
     * @param id        区域编号
     * @param separator 分隔符
     * @return 格式化后的区域
     */
    public static String format(Integer id, String separator) {
        // 获得区域
        Area area = areas.get(id);
        if (area == null) {
            return null;
        }

        // 格式化
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < AreaTypeEnum.values().length; i++) { // 避免死循环
            sb.insert(0, area.getName());
            // “递归”父节点
            area = area.getParent();
            if (area == null || area.getId() == Area.ID_GLOBAL || area.getId() == Area.ID_CHINA) {
                break;
            }
            sb.insert(0, separator);
        }
        return sb.toString();
    }

    /**
     * 获取指定类型的区域列表
     *
     * @param type 区域类型
     * @param func 转换函数
     * @param <T>  结果类型
     * @return 区域列表
     */
    public static <T> List<T> getByType(AreaTypeEnum type, Function<Area, T> func) {
        return CollectionUtils.convertList(areas.values(), func, area -> type.getType().equals(area.getType()));
    }

    /**
     * 根据区域编号、上级区域类型，获取上级区域编号
     *
     * @param id   区域编号
     * @param type 区域类型
     * @return 上级区域编号
     */
    public static Integer getParentIdByType(Integer id, AreaTypeEnum type) {
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            Area area = AreaUtils.getArea(id);
            if (area == null) {
                return null;
            }
            // 情况一：匹配到，返回它
            if (type.getType().equals(area.getType())) {
                return area.getId();
            }
            // 情况二：找到根节点，返回空
            if (area.getParent() == null || area.getParent().getId() == null) {
                return null;
            }
            // 其它：继续向上查找
            id = area.getParent().getId();
        }
        return null;
    }

    /**
     * 根据门店area_id获取省市县名称
     * @param areaId 门店区域编号
     * @return 省市县名称，格式：省份 城市 区县
     */
    public static String getStoreAreaName(Integer areaId) {
        return format(areaId, " ");
    }

    /**
     * 根据门店area_id获取省份名称
     * @param areaId 门店区域编号
     * @return 省份名称
     */
    public static String getProvinceName(Integer areaId) {
        Integer provinceId = getParentIdByType(areaId, AreaTypeEnum.PROVINCE);
        if (provinceId != null) {
            Area area = areas.get(provinceId);
            return area != null ? area.getName() : null;
        }
        return null;
    }

    /**
     * 根据门店area_id获取城市名称
     * @param areaId 门店区域编号
     * @return 城市名称
     */
    public static String getCityName(Integer areaId) {
        Integer cityId = getParentIdByType(areaId, AreaTypeEnum.CITY);
        if (cityId != null) {
            Area area = areas.get(cityId);
            return area != null ? area.getName() : null;
        }
        return null;
    }

    /**
     * 根据门店area_id获取区县名称
     * @param areaId 门店区域编号
     * @return 区县名称
     */
    public static String getDistrictName(Integer areaId) {
        Area area = areas.get(areaId);
        if (area != null && area.getType() == AreaTypeEnum.DISTRICT.getType()) {
            return area.getName();
        }
        return null;
    }
}
