package com.cloudprint.gui;

import com.cloudprint.dao.OrderDAO;
import com.cloudprint.dao.OrderItemDAO;
import com.cloudprint.dao.ProductPrintDocumentDAO;
import com.cloudprint.dao.TradeDeliveryPickUpStoreDAO;
import com.cloudprint.model.ProductPrintDocument;
import com.cloudprint.model.TradeOrder;
import com.cloudprint.model.TradeOrderItem;
import com.cloudprint.model.TradeDeliveryPickUpStore;
import com.cloudprint.util.AreaUtils;
import com.cloudprint.util.FormatUtils;
import com.cloudprint.util.PrinterStatusChecker;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

@Getter
public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JTable orderItemTable;
    private DefaultTableModel orderItemTableModel;
    private JTextField storeIdField;
    private JLabel storeInfoLabel;
    
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductPrintDocumentDAO productPrintDocumentDAO;
    private TradeDeliveryPickUpStoreDAO storeDAO;
    private TradeDeliveryPickUpStore currentStore;
    private PrinterStatusChecker printerStatusChecker;
    private JToggleButton syncToggleButton;
    
    public MainFrame(TradeDeliveryPickUpStore store) {
        super("云小象智能打印调度系统 - " + (store != null ? store.getName() : "未知门店"));
        this.currentStore = store;
        System.out.println("开始初始化MainFrame，门店ID: " + (store != null ? store.getId() : "未知"));
        
        // 简化初始化过程，先确保基本界面能够显示
        try {
            // 初始化打印机状态检查器
            printerStatusChecker = new PrinterStatusChecker();
            
            // 设置全局字体为微软雅黑
            Font microsoftYaHei = new Font("微软雅黑", Font.PLAIN, 13);
            UIManager.put("Label.font", microsoftYaHei);
            UIManager.put("Button.font", microsoftYaHei);
            UIManager.put("TextField.font", microsoftYaHei);
            UIManager.put("TextArea.font", microsoftYaHei);
            UIManager.put("ComboBox.font", microsoftYaHei);
            UIManager.put("Table.font", microsoftYaHei);
            UIManager.put("TableHeader.font", microsoftYaHei);
            UIManager.put("List.font", microsoftYaHei);
            UIManager.put("CheckBox.font", microsoftYaHei);
            UIManager.put("RadioButton.font", microsoftYaHei);
            UIManager.put("TabbedPane.font", microsoftYaHei);
            
            // 1. 设置基本属性
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1400, 1000);
            setLocationRelativeTo(null);
            
            // 2. 创建主面板
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(Color.WHITE);
            
            // 3. 创建顶部工具栏
            JPanel toolbar = createToolbar();
            mainPanel.add(toolbar, BorderLayout.NORTH);
            
            // 添加门店信息显示面板
            JPanel storeInfoPanel = createStoreInfoPanel();
            mainPanel.add(storeInfoPanel, BorderLayout.SOUTH);
            
            // 4. 创建选项卡
            tabbedPane = new JTabbedPane();
            
            // 5. 创建订单列表面板
            JPanel orderPanel = createOrderPanel();
            
            // 6. 创建打印机管理面板
            PrinterManagementPanel printerPanel = new PrinterManagementPanel();
            
            // 7. 添加选项卡
            // 设置选项卡字体和高度
            Font tabFont = new Font("微软雅黑", Font.BOLD, 16);
            UIManager.put("TabbedPane.font", tabFont);
            UIManager.put("TabbedPane.tabHeight", 40);
            
            // 加载图标
            ImageIcon orderIcon = null;
            ImageIcon printerIcon = null;
            try {
                orderIcon = new ImageIcon(getClass().getResource("/icons/to_be_printed.png"));
                // 缩放图标
                Image orderImg = orderIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                orderIcon = new ImageIcon(orderImg);
            } catch (Exception e) {
                System.err.println("无法加载订单图标: " + e.getMessage());
            }
            
            try {
                printerIcon = new ImageIcon(getClass().getResource("/icons/printer-icon.png"));
                // 缩放图标
                Image printerImg = printerIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                printerIcon = new ImageIcon(printerImg);
            } catch (Exception e) {
                System.err.println("无法加载打印机图标: " + e.getMessage());
            }
            
            // 添加带图标的选项卡
            tabbedPane.addTab("待打印订单", orderIcon, orderPanel);
            tabbedPane.addTab("打印机管理", printerIcon, printerPanel);
            
            // 8. 将选项卡添加到主面板
            mainPanel.add(tabbedPane, BorderLayout.CENTER);
            
            // 9. 设置内容面板
            setContentPane(mainPanel);
            
            // 10. 确保所有组件可见
            mainPanel.setVisible(true);
            setVisible(true);
            
            System.out.println("MainFrame基本初始化完成，可见性: " + isVisible());
            
            // 11. 延迟初始化DAO和加载数据，避免阻塞界面显示
            SwingUtilities.invokeLater(() -> {
                System.out.println("延迟初始化DAO和加载数据");
                try {
                    orderDAO = new OrderDAO();
                    orderItemDAO = new OrderItemDAO();
                    productPrintDocumentDAO = new ProductPrintDocumentDAO();
                    storeDAO = new TradeDeliveryPickUpStoreDAO();
                    System.out.println("OrderDAO、OrderItemDAO、ProductPrintDocumentDAO和TradeDeliveryPickUpStoreDAO初始化成功");
                    
                    // 加载待打印订单
                    if (store != null) {
                        loadPendingPrintOrders(store.getId().intValue());
                        // 加载门店信息
                        loadStoreInfo(store.getId());
                    } else {
                        System.err.println("门店信息为空，无法加载数据");
                    }
                } catch (Exception e) {
                    System.err.println("初始化DAO或加载数据失败: " + e.getMessage());
                    e.printStackTrace();
                    System.err.println("加载数据失败: " + e.getMessage());
                }
            });
            
        } catch (Exception e) {
            System.err.println("初始化MainFrame时发生异常: " + e.getMessage());
            e.printStackTrace();
            System.err.println("初始化失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建顶部工具栏
     */
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BorderLayout());
        toolbar.setBackground(new Color(0xec, 0x84, 0x21)); // 爱马仕橙背景
        
        // 显示当前门店信息
        JLabel currentStoreLabel = new JLabel("当前门店: " + (currentStore != null ? currentStore.getName() : "未知"));
        currentStoreLabel.setForeground(Color.WHITE); // 设置文字为白色
        currentStoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        
        // 创建左侧面板，放置当前门店信息
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(0xec, 0x84, 0x21));
        leftPanel.add(currentStoreLabel);
        
        // 创建右侧面板，放置刷新和退出按钮
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(0xec, 0x84, 0x21));
        
        // 初始化storeIdField
        storeIdField = new JTextField(10);
        
        // 从会话中获取当前选择的门店ID作为默认值
        com.cloudprint.model.TradeDeliveryPickUpStore currentStore = com.cloudprint.util.UserSession.getCurrentStore();
        if (currentStore != null) {
            storeIdField.setText(String.valueOf(currentStore.getId()));
        } else {
            storeIdField.setText("3"); // 默认门店ID
        }
        storeIdField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        
        // 创建刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.setBackground(Color.WHITE); // 设置按钮背景为白色
        refreshButton.setForeground(new Color(0xec, 0x84, 0x21)); // 设置按钮文字为橙色
        
        // 创建退出按钮
        JButton exitButton = new JButton("退出");
        exitButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        exitButton.setBackground(Color.WHITE); // 设置按钮背景为白色
        exitButton.setForeground(new Color(0xec, 0x84, 0x21)); // 设置按钮文字为橙色
        
        // 创建定时同步打印机数据开关
        syncToggleButton = new JToggleButton("定时同步打印机数据");
        syncToggleButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        syncToggleButton.setBackground(Color.WHITE); // 设置按钮背景为白色
        syncToggleButton.setForeground(new Color(0xec, 0x84, 0x21)); // 设置按钮文字为橙色
        syncToggleButton.setSelected(false); // 默认关闭
        
        // 为开关添加事件监听器
        syncToggleButton.addActionListener(e -> {
            boolean isSelected = syncToggleButton.isSelected();
            if (isSelected) {
                // 开启定时同步
                printerStatusChecker.startStatusCheck();
                System.out.println("开启定时同步打印机数据");
            } else {
                // 关闭定时同步
                printerStatusChecker.stopStatusCheck();
                System.out.println("关闭定时同步打印机数据");
            }
        });
        
        // 尝试添加图标（如果资源存在）
        try {

            // 创建刷新图标
            URL refreshIconUrl = getClass().getResource("/icons/refresh.png");
            if (refreshIconUrl != null) {
                ImageIcon refreshIcon = new ImageIcon(refreshIconUrl);
                refreshButton.setIcon(refreshIcon);
                refreshButton.setIconTextGap(5); // 设置图标与文字间距
            }
            
            // 创建退出图标
            URL exitIconUrl = getClass().getResource("/icons/exit.png");
            if (exitIconUrl != null) {
                ImageIcon exitIcon = new ImageIcon(exitIconUrl);
                exitButton.setIcon(exitIcon);
                exitButton.setIconTextGap(5); // 设置图标与文字间距
            }
            
            // 创建定时同步图标
            URL syncIconUrl = getClass().getResource("/icons/sync.png");
            if (syncIconUrl != null) {
                ImageIcon syncIcon = new ImageIcon(syncIconUrl);
                syncToggleButton.setIcon(syncIcon);
                syncToggleButton.setIconTextGap(5); // 设置图标与文字间距
            }
        } catch (Exception e) {
            // 图标资源不存在，忽略异常，使用无图标按钮
            System.out.println("未找到图标资源，将使用无图标按钮");
        }
        
        // 添加按钮到右侧面板
        rightPanel.add(syncToggleButton);
        rightPanel.add(refreshButton);
        rightPanel.add(exitButton);
        
        // 添加左右面板到工具栏
        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);
        
        // 添加刷新按钮事件
        refreshButton.addActionListener(e -> {
            String storeIdStr = storeIdField.getText().trim();
            if (storeIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入工厂ID", "输入错误", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Integer storeId = Integer.parseInt(storeIdStr);
                
                // 获取当前选中的选项卡索引
                int selectedIndex = tabbedPane.getSelectedIndex();
                
                // 根据当前选中的选项卡刷新对应的数据
                if (selectedIndex == 0) { // 待打印订单选项卡
                    loadPendingPrintOrders(storeId);
                    JOptionPane.showMessageDialog(this, "订单数据已刷新", "刷新成功", JOptionPane.INFORMATION_MESSAGE);
                } else if (selectedIndex == 1) { // 打印机管理选项卡
                    // 刷新打印机列表
                    PrinterManagementPanel printerPanel = (PrinterManagementPanel) tabbedPane.getComponentAt(1);
                    printerPanel.refreshDevices();
                    JOptionPane.showMessageDialog(this, "打印机列表已刷新", "刷新成功", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "工厂ID必须是数字", "输入错误", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "刷新失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 添加退出按钮事件
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "确定要退出并返回登录页面吗？", "退出确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                // 清除会话信息
                com.cloudprint.util.UserSession.clearSession();
                // 隐藏当前主窗口
                this.dispose();
                // 重新显示登录页面
                com.cloudprint.FactoryServerApplication.main(new String[0]);
            }
        });
        
        return toolbar;
    }
    
    /**
     * 创建门店信息显示面板
     */
    private JPanel createStoreInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("门店信息"));
        
        // 初始化DAO
        if (storeDAO == null) {
            storeDAO = new TradeDeliveryPickUpStoreDAO();
        }
        
        // 创建门店信息显示区域
        storeInfoLabel = new JLabel("请输入工厂ID查询门店信息");
        storeInfoLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        storeInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 添加到面板
        panel.add(storeInfoLabel, BorderLayout.CENTER);
        
        // 设置面板大小
        panel.setPreferredSize(new Dimension(0, 60));
        
        return panel;
    }
    
    /**
     * 创建订单列表面板
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // 创建订单表格模型
        String[] orderColumns = {"订单ID", "订单流水号", "用户ID", "商品数量", "订单状态", "支付状态", "打印状态", "创建时间"};
        orderTableModel = new DefaultTableModel(orderColumns, 0);
        
        // 创建订单表格
        orderTable = new JTable(orderTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁用所有单元格编辑
            }
        };
        orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // 设置订单表格所有单元格居中对齐
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < orderTable.getColumnCount(); i++) {
            orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 设置订单表格行高，增加每一行的高度
        orderTable.setRowHeight(30);
        
        // 创建订单表格滚动面板
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setPreferredSize(new Dimension(1100, 500));
        orderScrollPane.setBackground(Color.WHITE);
        
        // 创建订单商品项表格模型
        String[] orderItemColumns = {"文档项ID", "文档名称", "文档属性", "数量", "打印起始页", "文件类型", "文件大小", "文档地址"};
        orderItemTableModel = new DefaultTableModel(orderItemColumns, 0);
        
        // 创建订单商品项表格
        orderItemTable = new JTable(orderItemTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁用所有单元格编辑
            }
        };
        orderItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        orderItemTable.setRowHeight(35); // 设置行高为35px，增加商品项每一行的高度
        
        // 设置订单商品项表格单元格渲染器
        for (int i = 0; i < orderItemTable.getColumnCount(); i++) {
            if (i == 2) { // 商品属性列使用自定义渲染器
                orderItemTable.getColumnModel().getColumn(i).setCellRenderer(new PropertiesTableCellRenderer());
            } else {
                orderItemTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // 增加文档名称和文档属性的宽度
        orderItemTable.getColumnModel().getColumn(1).setPreferredWidth(200); // 文档名称列宽度设置为200px
        orderItemTable.getColumnModel().getColumn(2).setPreferredWidth(300); // 文档属性列宽度设置为300px
        orderItemTable.getColumnModel().getColumn(5).setPreferredWidth(100); // 文件类型列宽度设置为100px
        orderItemTable.getColumnModel().getColumn(6).setPreferredWidth(100); // 文件大小列宽度设置为100px
        orderItemTable.getColumnModel().getColumn(7).setPreferredWidth(400); // 文档地址列宽度设置为400px
        
        // 为文档地址列添加点击事件监听器，实现点击预览功能
        orderItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = orderItemTable.rowAtPoint(evt.getPoint());
                int column = orderItemTable.columnAtPoint(evt.getPoint());
                // 检查是否点击了文档地址列（索引为7）
                if (row >= 0 && column == 7) {
                    Object value = orderItemTable.getValueAt(row, column);
                    if (value != null && !value.toString().isEmpty()) {
                        String fileUrl = value.toString();
                        // 尝试打开浏览器预览文件
                        try {
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(fileUrl));
                            System.out.println("打开文件预览: " + fileUrl);
                        } catch (Exception e) {
                            System.err.println("打开文件预览失败: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        
        // 创建订单商品项表格滚动面板
        JScrollPane orderItemScrollPane = new JScrollPane(orderItemTable);
        orderItemScrollPane.setPreferredSize(new Dimension(1100, 200));
        orderItemScrollPane.setBackground(Color.WHITE);
        orderItemScrollPane.setBorder(BorderFactory.createTitledBorder("订单文档详情"));
        
        // 添加表格到面板
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(orderScrollPane, BorderLayout.NORTH);
        tablePanel.add(orderItemScrollPane, BorderLayout.CENTER);
        
        panel.add(tablePanel, BorderLayout.CENTER);
        
        // 添加订单选中事件监听器，用于显示对应的商品项
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    // 获取选中订单的ID
                    Long orderId = (Long) orderTable.getValueAt(selectedRow, 0);
                    // 加载对应的订单商品项
                    loadOrderItems(orderId);
                } else {
                    // 清空商品项表格
                    orderItemTableModel.setRowCount(0);
                }
            }
        });
        
        return panel;
    }
    
    /**
     * 加载待打印订单
     * @param storeId 门店ID
     */
    private void loadPendingPrintOrders(Integer storeId) {
        System.out.println("开始加载门店ID=" + storeId + "的待打印订单");
        
        // 清空表格
        orderTableModel.setRowCount(0);
        
        try {
            if (orderDAO == null) {
                System.err.println("OrderDAO未初始化，无法加载订单");
                System.err.println("系统未完全初始化，无法加载订单");
                return;
            }
            
            // 查询数据库
            List<TradeOrder> orders = orderDAO.getPendingPrintOrdersByStoreId(storeId);

            // 查询门店信息
            TradeDeliveryPickUpStore store = null;
            if (storeDAO != null) {
                store = storeDAO.getStoreById(storeId.longValue());
                if (store != null) {
                    System.out.println("门店名称: " + store.getName());
                    System.out.println("门店地址: " + store.getDetailAddress());
                } else {
                    System.out.println("未找到ID为 " + storeId + " 的门店信息");
                }
            }
            
            System.out.println("查询到 " + orders.size() + " 条待打印订单");
            
            // 添加数据到表格
            for (TradeOrder order : orders) {
                Object[] row = {
                    order.getId(),
                    order.getNo(),
                    order.getUserId(),
                    order.getProductCount(),
                    FormatUtils.getStatusText(order),
                    order.getPayStatus() ? "已支付" : "未支付",
                    FormatUtils.getPrintStatusText(order.getPrintStatus()),
                    order.getCreateTime()
                };
                orderTableModel.addRow(row);
            }
            
            // 刷新表格
            orderTableModel.fireTableDataChanged();
            orderTable.repaint();
            
            // 显示查询结果
            String message = orders.size() > 0 ? 
                "共查询到 " + orders.size() + " 条待打印订单" : 
                "未查询到待打印订单，请检查门店ID或等待新订单";
            
            System.out.println("查询结果: " + message);
            
        } catch (Exception e) {
            System.err.println("加载待打印订单失败: " + e.getMessage());
            e.printStackTrace();
            System.err.println("加载订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 加载订单商品项
     * @param orderId 订单ID
     */
    private void loadOrderItems(Long orderId) {
        System.out.println("开始加载订单ID=" + orderId + "的商品项");
        
        // 清空商品项表格
        orderItemTableModel.setRowCount(0);
        
        try {
            if (orderItemDAO == null) {
                System.err.println("OrderItemDAO未初始化，无法加载订单商品项");
                return;
            }
            
            // 查询数据库
            List<TradeOrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
            
            System.out.println("查询到 " + orderItems.size() + " 条订单商品项");
            
            // 添加数据到表格
            for (TradeOrderItem item : orderItems) {
                // 初始化文档相关字段
                String fileType = ""; 
                String fileSizeStr = "";
                
                // 查询文档信息
                System.out.println("开始查询文档信息: spu_id=" + item.getSpuId() + ", sku_id=" + item.getSkuId());
                // 初始化文档相关字段
                String fileUrl = "";
                if (productPrintDocumentDAO != null) {
                    // 先根据spu_id和sku_id获取document_id
                    Long documentId = productPrintDocumentDAO.getDocumentIdBySpuAndSku(item.getSpuId(), item.getSkuId());
                    System.out.println("查询到的documentId: " + documentId);
                    if (documentId != null) {
                        // 根据document_id获取文档信息
                        ProductPrintDocument document = productPrintDocumentDAO.getDocumentById(documentId);
                        System.out.println("查询到的文档: " + document);
                        if (document != null) {
                            fileType = document.getFileType() != null ? document.getFileType() : "";
                            System.out.println("文档类型: " + fileType);
                            // 格式化文件大小
                            if (document.getFileSize() != null) {
                                fileSizeStr = FormatUtils.formatFileSize(document.getFileSize());
                                System.out.println("文档大小: " + fileSizeStr);
                            }
                            // 获取文件URL
                            fileUrl = document.getFileUrl() != null ? document.getFileUrl() : "";
                            System.out.println("文档地址: " + fileUrl);
                        }
                    } else {
                        System.out.println("未查询到documentId，检查spu_id和sku_id是否正确");
                    }
                } else {
                    System.out.println("ProductPrintDocumentDAO未初始化");
                }
                System.out.println("最终文档类型: " + fileType + ", 文档大小: " + fileSizeStr + ", 文档地址: " + fileUrl);
                
                Object[] row = {
                    item.getId(),
                    item.getSpuName(),
                    FormatUtils.parseProperties(item.getProperties()),
                    item.getCount(),
                    item.getPageStart() + "-" + item.getPageEnd() + "页",
                    fileType,
                    fileSizeStr,
                    fileUrl
                };
                orderItemTableModel.addRow(row);
            }
            
            // 刷新表格
            orderItemTableModel.fireTableDataChanged();
            orderItemTable.repaint();
            
        } catch (Exception e) {
            System.err.println("加载订单商品项失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 加载门店信息
     * @param storeId 门店ID
     */
    private void loadStoreInfo(Long storeId) {
        try {
            if (storeDAO == null) {
                storeDAO = new TradeDeliveryPickUpStoreDAO();
            }
            
            // 查询门店信息
            TradeDeliveryPickUpStore store = storeDAO.getStoreById(storeId);
            
            if (store != null) {
                // 格式化门店信息
                // 使用AreaUtils获取省市县名称
                String areaName = AreaUtils.getStoreAreaName(store.getAreaId());
                
                String storeInfo = String.format("工厂名称: %s | 联系电话: %s | 地区: %s | 地址: %s", 
                    store.getName() != null ? store.getName() : "未知",
                    store.getPhone() != null ? store.getPhone() : "未知",
                    areaName != null ? areaName : "未知",
                    store.getDetailAddress() != null ? store.getDetailAddress() : "未知");
                
                // 更新门店信息标签
                storeInfoLabel.setText(storeInfo);
            } else {
                storeInfoLabel.setText("未找到ID为 " + storeId + " 的门店信息");
            }
        } catch (Exception e) {
            System.err.println("加载门店信息失败: " + e.getMessage());
            e.printStackTrace();
            storeInfoLabel.setText("加载门店信息时出错: " + e.getMessage());
        }
    }
    
    /**
     * 商品属性标签单元格渲染器
     */
    private class PropertiesTableCellRenderer extends DefaultTableCellRenderer {
        // 定义不同属性使用的颜色
        private final Color[] tagColors = {
            new Color(100, 181, 246), // 蓝色
            new Color(102, 187, 106), // 绿色
            new Color(255, 152, 0),   // 橙色
            new Color(156, 39, 176),  // 紫色
            new Color(239, 83, 80),   // 红色
            new Color(63, 81, 181),   // 靛蓝色
            new Color(255, 235, 59),  // 黄色
            new Color(76, 175, 80)    // 浅绿色
        };
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // 创建面板来容纳多个标签
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setOpaque(true);
            
            // 设置背景颜色，根据选中状态调整
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            
            if (value != null) {
                String propertiesStr = value.toString();
                // 如果是原始JSON格式，先解析
                if (propertiesStr.startsWith("[{")) {
                    propertiesStr = FormatUtils.parseProperties(propertiesStr);
                }
                
                // 分割属性值
                String[] properties = propertiesStr.split("\\s*\\|\\s*");
                
                // 创建标签并添加到面板
                for (int i = 0; i < properties.length; i++) {
                    String prop = properties[i].trim();
                    if (!prop.isEmpty()) {
                        JLabel tagLabel = new JLabel(prop);
                        // 设置标签样式
                        tagLabel.setOpaque(true);
                        // 使用循环颜色数组
                        Color bgColor = tagColors[i % tagColors.length];
                        tagLabel.setBackground(bgColor);
                        tagLabel.setForeground(Color.WHITE);
                        tagLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
                        tagLabel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(bgColor.darker(), 1),
                            BorderFactory.createEmptyBorder(2, 8, 2, 8)
                        ));
                        tagLabel.setBorder(BorderFactory.createCompoundBorder(
                            tagLabel.getBorder(),
                            BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2) // 增加内边距
                        ));
                        panel.add(tagLabel);
                    }
                }
            }
            
            return panel;
        }
    }
}