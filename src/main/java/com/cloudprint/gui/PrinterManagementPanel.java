package com.cloudprint.gui;

import com.cloudprint.dao.PrintDeviceDAO;
import com.cloudprint.model.TradePrintDevice;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;


public class PrinterManagementPanel extends JPanel {
    private PrintDeviceDAO printDeviceDAO;
    
    private JTextField nameField;
    private JTextField modelField;
    private JTextField typeField;
    private JTextField addressField;
    private JTextField portField;
    private JTextField locationField;
    private JTextField storeIdField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> paperStatusComboBox;
    private JTextField inkLevelField;
    private JTextArea remarkArea;
    private JPanel deviceGridPanel; // 设备网格面板，用于显示现有打印设备
    
    public PrinterManagementPanel() {
        printDeviceDAO = new PrintDeviceDAO();
        initComponents();
        addListeners();
    }
    
    private void initComponents() {
        // 初始化组件
        JLabel nameLabel = new JLabel("设备名称:");
        nameField = new JTextField(20);
        
        JLabel modelLabel = new JLabel("设备型号:");
        modelField = new JTextField(20);
        modelField.setText(""); // 默认爱普生4000
        
        JLabel typeLabel = new JLabel("设备类型:");
        typeField = new JTextField(20);
        typeField.setText("打印机");
        
        JLabel addressLabel = new JLabel("连接地址:");
        addressField = new JTextField(20);
        
        JLabel portLabel = new JLabel("端口:");
        portField = new JTextField(5);
        portField.setText("9100");
        
        JLabel locationLabel = new JLabel("设备位置:");
        locationField = new JTextField(20);
        
        JLabel storeIdLabel = new JLabel("门店ID:");
        storeIdField = new JTextField(10);
        // 从会话中获取当前选择的门店ID作为默认值
        com.cloudprint.model.TradeDeliveryPickUpStore currentStore = com.cloudprint.util.UserSession.getCurrentStore();
        if (currentStore != null) {
            storeIdField.setText(String.valueOf(currentStore.getId()));
        } else {
            storeIdField.setText("1");
        }
        
        JLabel statusLabel = new JLabel("设备状态:");
        statusComboBox = new JComboBox<>(new String[]{"online", "OFFLINE"});
        statusComboBox.setSelectedItem("online");
        
        JLabel paperStatusLabel = new JLabel("纸张状态:");
        paperStatusComboBox = new JComboBox<>(new String[]{"sufficient", "low", "empty"});
        paperStatusComboBox.setSelectedItem("sufficient");
        
        JLabel inkLevelLabel = new JLabel("墨水水平(%):");
        inkLevelField = new JTextField(5);
        inkLevelField.setText("100");
        
        JLabel remarkLabel = new JLabel("备注:");
        remarkArea = new JTextArea(5, 30);
        JScrollPane remarkScrollPane = new JScrollPane(remarkArea);
        
        JButton addButton = new JButton("添加打印机");
        JButton clearButton = new JButton("清空表单");
        
        // 创建表单面板
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        // 第一行
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; formPanel.add(nameField, gbc);
        gbc.gridx = 2; gbc.gridy = row; formPanel.add(modelLabel, gbc);
        gbc.gridx = 3; gbc.gridy = row; formPanel.add(modelField, gbc);
        
        row++;
        // 第二行
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(typeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; formPanel.add(typeField, gbc);
        gbc.gridx = 2; gbc.gridy = row; formPanel.add(addressLabel, gbc);
        gbc.gridx = 3; gbc.gridy = row; formPanel.add(addressField, gbc);
        
        row++;
        // 第三行
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(portLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; formPanel.add(portField, gbc);
        gbc.gridx = 2; gbc.gridy = row; formPanel.add(locationLabel, gbc);
        gbc.gridx = 3; gbc.gridy = row; formPanel.add(locationField, gbc);
        
        row++;
        // 第四行
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(storeIdLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; formPanel.add(storeIdField, gbc);
        gbc.gridx = 2; gbc.gridy = row; formPanel.add(statusLabel, gbc);
        gbc.gridx = 3; gbc.gridy = row; formPanel.add(statusComboBox, gbc);
        
        row++;
        // 第五行
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(paperStatusLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; formPanel.add(paperStatusComboBox, gbc);
        gbc.gridx = 2; gbc.gridy = row; formPanel.add(inkLevelLabel, gbc);
        gbc.gridx = 3; gbc.gridy = row; formPanel.add(inkLevelField, gbc);
        
        row++;
        // 第六行（备注）
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST; formPanel.add(remarkLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH; formPanel.add(remarkScrollPane, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        // 设置主面板布局为BorderLayout
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // 设置背景色为白色，确保面板正常显示

        
        // 创建控制面板，包含标题和按钮面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(10, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 搜索本机关联的打印机按钮
        JButton searchLocalButton = new JButton("搜索本机关联的打印机");
        searchLocalButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchLocalButton.setBackground(new Color(0xec, 0x84, 0x21)); // 爱马仕橙背景
        searchLocalButton.setForeground(Color.WHITE); // 白色文字
        searchLocalButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // 搜索局域网中的打印机按钮
        JButton searchLanButton = new JButton("搜索局域网中的打印机");
        searchLanButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchLanButton.setBackground(new Color(0xec, 0x84, 0x21)); // 爱马仕橙背景
        searchLanButton.setForeground(Color.WHITE); // 白色文字
        searchLanButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // 添加打印机按钮
        JButton addPrinterButton = new JButton("添加打印机");
        addPrinterButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        addPrinterButton.setBackground(new Color(0xec, 0x84, 0x21)); // 爱马仕橙背景
        addPrinterButton.setForeground(Color.WHITE); // 白色文字
        addPrinterButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // 创建按钮面板，容纳添加和搜索按钮
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(searchLocalButton);
        buttonPanel.add(searchLanButton);
        buttonPanel.add(addPrinterButton);
        controlPanel.add(buttonPanel, BorderLayout.EAST);
        
        // 创建设备网格面板，一行显示4个设备
        deviceGridPanel = new JPanel();
        // 使用BoxLayout，每行使用一个面板
        deviceGridPanel.setLayout(new BoxLayout(deviceGridPanel, BoxLayout.Y_AXIS));
        deviceGridPanel.setBackground(Color.WHITE);
        
        // 设备列表滚动面板
        JScrollPane devicesScrollPane = new JScrollPane(deviceGridPanel);
        devicesScrollPane.setPreferredSize(new Dimension(0, 400)); // 宽度自适应，固定高度
        devicesScrollPane.setMinimumSize(new Dimension(0, 400)); // 宽度自适应，固定最小高度
        devicesScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400)); // 宽度自适应，固定最大高度
        devicesScrollPane.setBackground(Color.WHITE);
        devicesScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        devicesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // 垂直滚动条按需显示
        devicesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 禁用水平滚动条
        
        // 创建主内容面板，使用BorderLayout让滚动面板填充宽度
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 5));
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.add(controlPanel, BorderLayout.NORTH);
        mainContentPanel.add(devicesScrollPane, BorderLayout.CENTER);
        
        // 创建包装面板，设置固定高度并让内容填充宽度
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setPreferredSize(new Dimension(0, 480)); // 宽度自适应，固定高度
        wrapperPanel.setMinimumSize(new Dimension(0, 480)); // 宽度自适应，固定最小高度
        wrapperPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        // 添加到主面板
        add(wrapperPanel, BorderLayout.CENTER);
        
        // 加载现有打印设备
        loadPrintDevices();
        
        // 添加打印机按钮事件
        addPrinterButton.addActionListener(e -> {
            // 打开添加打印机弹窗
            AddPrinterDialog dialog = new AddPrinterDialog((JFrame) SwingUtilities.getWindowAncestor(this), printDeviceDAO);
            dialog.setVisible(true);
            if (dialog.isSuccess()) {
                // 如果添加成功，重新加载设备列表
                loadPrintDevices();
            }
        });
        
        // 搜索本机关联打印机按钮事件
        searchLocalButton.addActionListener(e -> {
            // 打开本地打印机对话框
            LocalPrintersDialog dialog = new LocalPrintersDialog((JFrame) SwingUtilities.getWindowAncestor(this), printDeviceDAO);
            dialog.setVisible(true);
            if (dialog.isSuccess()) {
                // 如果添加成功，重新加载设备列表
                loadPrintDevices();
            }
        });
        
        // 搜索打印机按钮事件
        searchLanButton.addActionListener(e -> {
            // 打开扫描打印机对话框
            ScanPrintersDialog dialog = new ScanPrintersDialog((JFrame) SwingUtilities.getWindowAncestor(this), printDeviceDAO);
            dialog.setVisible(true);
            if (dialog.isSuccess()) {
                // 如果添加成功，重新加载设备列表
                loadPrintDevices();
            }
        });
    }
    
    private void addListeners() {
        // 不再需要这个方法，监听器已经在initComponents中直接添加
    }
    
    /**
     * 公共方法，用于刷新打印设备列表
     */
    public void refreshDevices() {
        loadPrintDevices();
    }
    
    /**
     * 加载现有打印设备并显示到网格面板中
     */
    private void loadPrintDevices() {
        // 清空现有设备卡片
        deviceGridPanel.removeAll();
        
        try {
            // 从会话中获取当前选择的门店ID
            com.cloudprint.model.TradeDeliveryPickUpStore currentStore = com.cloudprint.util.UserSession.getCurrentStore();
            Long storeId = currentStore != null ? currentStore.getId() : null;
            
            // 查询指定门店的所有打印设备
            List<TradePrintDevice> devices = printDeviceDAO.getAllPrintDevices(storeId);
            
            System.out.println("查询到 " + devices.size() + " 台打印设备，门店ID: " + (storeId != null ? storeId : "全部"));
            
            // 为每个设备创建卡片并添加到网格面板
            // 每行创建一个面板，包含最多4个卡片
            JPanel rowPanel = null;
            
            for (int i = 0; i < devices.size(); i++) {
                // 每行的第一个元素创建新行
                if (i % 4 == 0) {
                    rowPanel = new JPanel();
                    rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
                    rowPanel.setBackground(Color.WHITE);
                    
                    // 添加水平间距
                    rowPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                    deviceGridPanel.add(rowPanel);
                    deviceGridPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                }
                
                // 创建设备卡片
                JPanel deviceCard = createDeviceCard(devices.get(i));
                
                // 添加卡片到当前行
                rowPanel.add(deviceCard);
                
                // 如果不是行末，添加水平间距
                if (i % 4 != 3) {
                    rowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
                }
            }
            
            // 刷新面板
            deviceGridPanel.revalidate();
            deviceGridPanel.repaint();
            
        } catch (Exception e) {
            System.err.println("加载打印设备失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载打印设备失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 创建单个设备卡片
     * @param device 打印设备对象
     * @return 设备卡片面板
     */
    private JPanel createDeviceCard(TradePrintDevice device) {
        // 创建设备卡片面板
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(300, 250)); // 设置首选大小
        card.setMinimumSize(new Dimension(300, 250)); // 设置最小高度，确保卡片高度固定
        card.setMaximumSize(new Dimension(300, 250)); // 设置最大高度，确保卡片高度固定
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 设备名称
        JLabel nameLabel = new JLabel(device.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(JLabel.CENTER); // 设置文本居中显示
        
        // 创建设备名称面板并添加虚线边框方框
        JPanel namePanel = new JPanel();
        namePanel.setBackground(Color.WHITE);
        namePanel.setLayout(new BorderLayout());
        namePanel.add(nameLabel, BorderLayout.CENTER);
        
        // 设置面板的最小高度，确保设备名称完全显示
        namePanel.setMinimumSize(new Dimension(0, 40));
        namePanel.setPreferredSize(new Dimension(0, 40));
        
        // 创建虚线边框方框
        Border dashedBorder = BorderFactory.createDashedBorder(Color.LIGHT_GRAY, 1, 1);
        namePanel.setBorder(BorderFactory.createCompoundBorder(
            dashedBorder,
            BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        
        // 设备型号
        JLabel modelLabel = new JLabel("型号: " + device.getModel());
        modelLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 设备类型
        JLabel typeLabel = new JLabel("类型: " + device.getType());
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 设备位置
        JLabel locationLabel = new JLabel("位置: " + device.getLocation());
        locationLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        JLabel addressLabel = new JLabel("IP: " + device.getAddress());
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 设备状态
        JLabel statusLabel = new JLabel("状态: " + getStatusText(device.getStatus()));
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        // 根据状态设置不同的颜色
        switch (device.getStatus()) {
            case "OTHER":
                statusLabel.setForeground(Color.ORANGE);
                break;
            case "UNKNOWN":
                statusLabel.setForeground(Color.GRAY);
                break;
            case "IDLE":
                statusLabel.setForeground(Color.GREEN);
                break;
            case "PRINTING":
                statusLabel.setForeground(Color.BLUE);
                break;
            case "WARMUP":
                statusLabel.setForeground(Color.YELLOW);
                break;
            case "STOPPED":
                statusLabel.setForeground(Color.RED);
                break;
            case "OFFLINE":
                statusLabel.setForeground(Color.RED.darker());
                break;
            // 兼容旧状态值
            case "online":
                statusLabel.setForeground(Color.GREEN);
                break;
            case "offline":
                statusLabel.setForeground(Color.RED.darker());
                break;
            case "busy":
                statusLabel.setForeground(Color.BLUE);
                break;
            case "paused":
                statusLabel.setForeground(Color.RED);
                break;
            case "error":
                statusLabel.setForeground(Color.ORANGE);
                break;
            case "maintenance":
                statusLabel.setForeground(Color.ORANGE);
                break;
            default:
                statusLabel.setForeground(Color.GRAY);
        }

        // 纸张状态
        JLabel paperStatusLabel = new JLabel("纸张: " + getPaperStatusText(device.getPaperStatus()));
        paperStatusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        // 根据纸张状态设置不同的颜色
        switch (device.getPaperStatus()) {
            case "sufficient":
                paperStatusLabel.setForeground(Color.GREEN);
                break;
            case "low":
                paperStatusLabel.setForeground(Color.ORANGE);
                break;
            case "empty":
                paperStatusLabel.setForeground(Color.RED);
                break;
            default:
                paperStatusLabel.setForeground(Color.BLACK);
        }

        // 设备信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(0, 10));
        infoPanel.setBackground(Color.WHITE);
        
        // 创建设备详情面板，使用GridLayout排列其他信息
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(6, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.add(modelLabel);
        detailsPanel.add(typeLabel);
        detailsPanel.add(locationLabel);
        detailsPanel.add(addressLabel);
        detailsPanel.add(statusLabel);
        detailsPanel.add(paperStatusLabel);
        
        // 添加设备名称面板和详情面板到主信息面板
        infoPanel.add(namePanel, BorderLayout.NORTH);
        infoPanel.add(detailsPanel, BorderLayout.CENTER);
        
        // 添加信息面板到卡片
        card.add(infoPanel, BorderLayout.CENTER);
        
        // 添加点击事件监听器，支持编辑设备
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // 单击打开编辑对话框
                editPrinter(device);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // 鼠标悬停时改变背景色和边框，提供视觉反馈
                card.setBackground(new Color(0xf5, 0xf5, 0xf5));
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(0xec, 0x84, 0x21), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // 鼠标离开时恢复原状
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return card;
    }
    
    /**
     * 编辑打印机设备
     * @param device 要编辑的打印机设备
     */
    private void editPrinter(TradePrintDevice device) {
        // 打开添加打印机对话框，复用该对话框进行编辑
        AddPrinterDialog addDialog = new AddPrinterDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            printDeviceDAO, 
            device
        );
        addDialog.setVisible(true);
        
        if (addDialog.isSuccess()) {
            // 如果编辑成功，重新加载设备列表
            loadPrintDevices();
        }
    }
    
    /**
     * 获取状态的中文文本
     * @param status 状态值
     * @return 中文状态文本
     */
    private String getStatusText(String status) {
        switch (status) {
            case "OTHER":
                return "其他";
            case "UNKNOWN":
                return "未知";
            case "IDLE":
                return "空闲";
            case "PRINTING":
                return "打印中";
            case "WARMUP":
                return "预热";
            case "STOPPED":
                return "已停止";
            case "OFFLINE":
                return "离线";
            // 兼容旧状态值
            case "online":
                return "空闲";
            case "busy":
                return "打印中";
            case "paused":
                return "已停止";
            case "error":
                return "其他";
            case "maintenance":
                return "其他";
            default:
                return "未知";
        }
    }
    
    /**
     * 获取纸张状态的中文文本
     * @param paperStatus 英文纸张状态
     * @return 中文纸张状态文本
     */
    private String getPaperStatusText(String paperStatus) {
        switch (paperStatus) {
            case "sufficient":
                return "充足";
            case "low":
                return "不足";
            case "empty":
                return "缺纸";
            default:
                return "未知";
        }
    }

}