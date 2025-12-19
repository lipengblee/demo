package com.cloudprint.gui;

import com.cloudprint.dao.PrintDeviceDAO;
import com.cloudprint.model.TradePrintDevice;
import com.cloudprint.util.PrinterSNMPManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * 添加打印机弹窗
 */
public class AddPrinterDialog extends JDialog {
    private PrintDeviceDAO printDeviceDAO;
    private boolean success = false;
    private TradePrintDevice device; // 要编辑的设备，null表示添加新设备
    private boolean isLocal; // 标识是否为本地打印机

    // 表单组件
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
    private JButton deleteButton; // 删除按钮

    public AddPrinterDialog(JFrame parent, PrintDeviceDAO printDeviceDAO) {
        super(parent, "添加新打印机", true);
        this.printDeviceDAO = printDeviceDAO;
        initComponents();
        setupLayout();
        addListeners();
        setLocationRelativeTo(parent);
    }

    /**
     * 带预填信息的构造函数，用于从网络扫描结果或本地打印机添加打印机
     */
    public AddPrinterDialog(JFrame parent, PrintDeviceDAO printDeviceDAO, String name, String model, String type,
            String ipAddress, int port) {
        this(parent, printDeviceDAO, name, model, type, ipAddress, port, false);
    }
    
    /**
     * 带预填信息和本地标识的构造函数，用于从本地打印机添加打印机
     */
    public AddPrinterDialog(JFrame parent, PrintDeviceDAO printDeviceDAO, String name, String model, String type,
            String ipAddress, int port, boolean isLocal) {
        super(parent, "添加新打印机", true);
        this.printDeviceDAO = printDeviceDAO;
        this.isLocal = isLocal;
        initComponents();
        setupLayout();
        addListeners();
        setLocationRelativeTo(parent);

        // 预填打印机信息
        this.nameField.setText(name);
        this.modelField.setText(model);
        this.typeField.setText(type);
        this.addressField.setText(ipAddress);
        this.portField.setText(String.valueOf(port));
        
        if (isLocal) {
            this.locationField.setText("本地打印机");
        } else {
            this.locationField.setText("局域网打印机");
        }
    }

    /**
     * 编辑打印机构造函数
     */
    public AddPrinterDialog(JFrame parent, PrintDeviceDAO printDeviceDAO, TradePrintDevice device) {
        super(parent, "编辑打印机", true);
        this.printDeviceDAO = printDeviceDAO;
        this.device = device; // 设置要编辑的设备
        initComponents();
        setupLayout();
        addListeners();
        setLocationRelativeTo(parent);

        // 预填打印机信息
        this.nameField.setText(device.getName());
        this.modelField.setText(device.getModel());
        this.typeField.setText(device.getType());
        this.addressField.setText(device.getAddress());
        this.portField.setText(String.valueOf(device.getPort()));
        this.locationField.setText(device.getLocation());
        this.statusComboBox.setSelectedItem(device.getStatus());
        this.paperStatusComboBox.setSelectedItem(device.getPaperStatus());
        this.inkLevelField.setText(String.valueOf(device.getInkLevel()));
        this.remarkArea.setText(device.getRemark());
    }

    private void initComponents() {
        // 初始化组件
        JLabel nameLabel = new JLabel("设备名称:");
        nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        nameField = new JTextField(20);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel modelLabel = new JLabel("设备型号:");
        modelLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        modelField = new JTextField(20);
        modelField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        modelField.setText(""); // 默认爱普生4000

        JLabel typeLabel = new JLabel("设备类型:");
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        typeField = new JTextField(20);
        typeField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        typeField.setText("打印机");

        JLabel addressLabel = new JLabel("连接地址:");
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        addressField = new JTextField(20);
        addressField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel portLabel = new JLabel("端口:");
        portLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        portField = new JTextField(5);
        portField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        portField.setText("9100");

        JLabel locationLabel = new JLabel("设备位置:");
        locationLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        locationField = new JTextField(20);
        locationField.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 门店ID字段，从会话中获取，隐藏不显示
        storeIdField = new JTextField(10);
        // 从会话中获取当前选择的门店ID作为默认值
        com.cloudprint.model.TradeDeliveryPickUpStore currentStore = com.cloudprint.util.UserSession.getCurrentStore();
        if (currentStore != null) {
            storeIdField.setText(String.valueOf(currentStore.getId()));
        } else {
            storeIdField.setText("1");
        }
        // 隐藏门店ID字段
        storeIdField.setVisible(false);

        JLabel statusLabel = new JLabel("设备状态:");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusComboBox = new JComboBox<>(new String[] { "online", "OFFLINE" });
        statusComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statusComboBox.setSelectedItem("online");

        JLabel paperStatusLabel = new JLabel("纸张状态:");
        paperStatusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        paperStatusComboBox = new JComboBox<>(new String[] { "sufficient", "low", "empty" });
        paperStatusComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        paperStatusComboBox.setSelectedItem("sufficient");

        JLabel inkLevelLabel = new JLabel("墨水水平(%):");
        inkLevelLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inkLevelField = new JTextField(5);
        inkLevelField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inkLevelField.setText("100");

        JLabel remarkLabel = new JLabel("备注:");
        remarkLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        remarkArea = new JTextArea(5, 30);
        remarkArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        remarkArea.setBorder(new JTextField().getBorder());

        // 创建按钮，根据模式设置不同的按钮文本
        String buttonText = (this.device == null) ? "添加打印机" : "更新打印机";
        JButton addButton = new JButton(buttonText);
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        addButton.setBackground(new Color(0xec, 0x84, 0x21)); // 爱马仕橙背景
        addButton.setForeground(Color.WHITE);

        // 创建获取详细数据按钮
        JButton getDetailsButton = new JButton("获取详细数据");
        getDetailsButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        getDetailsButton.setBackground(new Color(0x4a, 0x90, 0xe2)); // 蓝色背景
        getDetailsButton.setForeground(Color.WHITE);

        // 创建取消按钮
        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.BLACK);

        // 创建删除按钮
        deleteButton = new JButton("删除打印机");
        deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        deleteButton.setBackground(Color.RED); // 红色背景
        deleteButton.setForeground(Color.WHITE);

        // 添加获取详细数据按钮的事件监听器
        getDetailsButton.addActionListener(e -> {
            // 只有在编辑模式下才执行获取详细数据操作
            if (this.device != null) {
                // System.out.println("\n=== 打印机详细数据 ===");
                // System.out.println("设备ID: " + this.device.getId());
                // System.out.println("设备名称: " + this.device.getName());
                // System.out.println("设备型号: " + this.device.getModel());
                // System.out.println("设备类型: " + this.device.getType());
                // System.out.println("连接地址: " + this.device.getAddress());
                // System.out.println("端口: " + this.device.getPort());
                // System.out.println("设备位置: " + this.device.getLocation());
                // System.out.println("门店ID: " + this.device.getStoreId());
                // System.out.println("设备状态: " + this.device.getStatus());
                // System.out.println("纸张状态: " + this.device.getPaperStatus());
                // System.out.println("墨水水平: " + this.device.getInkLevel() + "%");
                // System.out.println("备注: " + this.device.getRemark());
                // System.out.println("是否删除: " + this.device.getDeleted());
                // System.out.println("创建者: " + this.device.getCreator());
                // System.out.println("更新者: " + this.device.getUpdater());
                // System.out.println("创建时间: " + this.device.getCreateTime());
                // System.out.println("更新时间: " + this.device.getUpdateTime());
                // System.out.println("队列计数: " + this.device.getQueueCount());
                // System.out.println("今日计数: " + this.device.getTodayCount());
                // System.out.println("成功率: " + this.device.getSuccessRate() + "%");
                // System.out.println("最后连接时间: " + this.device.getLastConnectTime());
                // System.out.println("连接类型: " + this.device.getConnectionType());
                // System.out.println("=====================\n");

                // 方式1: 查询单个打印机
                try {
                    PrinterSNMPManager snmpManager = new PrinterSNMPManager(this.device.getAddress());
                    // 获取单个OID
                    String sysDescr = snmpManager.getSingleOID(
                            PrinterSNMPManager.PrinterOIDs.SYS_DESCR);
                    System.out.println("系统描述: " + sysDescr);

                    // 获取完整信息
                    PrinterSNMPManager.PrinterInfo info = snmpManager.getPrinterInfo();
                    System.out.println("打印机型号: " + info.getModel());
                    System.out.println("序列号: " + info.getSerialNumber());
                    System.out.println("状态: " + info.getStatus().getDescription());
                    System.out.println("总打印页数: " + info.getTotalPages());

                    // 输出JSON格式
                    System.out.println("JSON格式:");
                    System.out.println(info.toJson());

                    snmpManager.close();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                System.out.println("当前是添加模式，没有打印机详细数据可获取\n");
            }
        });

        // 只有在编辑模式下才显示获取详细数据按钮
        getDetailsButton.setVisible(this.device != null);

        // 设置按钮样式
        Dimension buttonSize = new Dimension(100, 30);
        addButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        getDetailsButton.setPreferredSize(buttonSize);

        // 只有在编辑模式下才显示删除按钮
        deleteButton.setVisible(this.device != null);

        // 表单面板
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // 第一行
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        formPanel.add(nameField, gbc);
        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(modelLabel, gbc);
        gbc.gridx = 3;
        gbc.gridy = row;
        formPanel.add(modelField, gbc);

        row++;
        // 第二行
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        formPanel.add(typeField, gbc);
        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 3;
        gbc.gridy = row;
        formPanel.add(addressField, gbc);

        row++;
        // 第三行
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(portLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        formPanel.add(portField, gbc);
        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(locationLabel, gbc);
        gbc.gridx = 3;
        gbc.gridy = row;
        formPanel.add(locationField, gbc);

        row++;
        // 第四行
        // 门店ID标签和字段已隐藏，不添加到面板中
        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 3;
        gbc.gridy = row;
        formPanel.add(statusComboBox, gbc);

        row++;
        // 第五行
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(paperStatusLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        formPanel.add(paperStatusComboBox, gbc);
        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(inkLevelLabel, gbc);
        gbc.gridx = 3;
        gbc.gridy = row;
        formPanel.add(inkLevelField, gbc);

        row++;
        // 第六行（备注）
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(remarkLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(remarkArea), gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // 添加删除按钮（仅在编辑模式下显示）到左侧
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(deleteButton);
        buttonPanel.add(leftPanel, BorderLayout.WEST);

        // 添加其他按钮到右侧
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(getDetailsButton);
        rightPanel.add(addButton);
        rightPanel.add(cancelButton);
        buttonPanel.add(rightPanel, BorderLayout.EAST);

        // 设置面板
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(contentPanel);
        pack();
        setResizable(false);

        // 添加按钮事件
        addButton.addActionListener(e -> {
            if (validateForm()) {
                boolean result;
                String successMessage;

                if (this.device == null) {
                    // 新增打印机模式
                    TradePrintDevice newDevice = new TradePrintDevice();
                    newDevice.setName(nameField.getText().trim());
                    newDevice.setModel(modelField.getText().trim());
                    newDevice.setType(typeField.getText().trim());
                    newDevice.setAddress(addressField.getText().trim());
                    newDevice.setPort(Integer.parseInt(portField.getText().trim()));
                    newDevice.setLocation(locationField.getText().trim());
                    newDevice.setStoreId(Long.parseLong(storeIdField.getText().trim()));
                    newDevice.setStatus((String) statusComboBox.getSelectedItem());
                    newDevice.setPaperStatus((String) paperStatusComboBox.getSelectedItem());
                    newDevice.setInkLevel(Integer.parseInt(inkLevelField.getText().trim()));
                    newDevice.setRemark(remarkArea.getText().trim());
                    newDevice.setDeleted(false);
                    newDevice.setCreator("admin");
                    newDevice.setUpdater("admin");
                    newDevice.setQueueCount(0);
                    newDevice.setTodayCount(0);
                    newDevice.setSuccessRate(100);
                    newDevice.setIsLocal(this.isLocal); // 设置是否为本地打印机

                    result = printDeviceDAO.addPrintDevice(newDevice);
                    successMessage = "打印机添加成功";
                } else {
                    // 编辑打印机模式
                    // 更新现有设备的属性
                    this.device.setName(nameField.getText().trim());
                    this.device.setModel(modelField.getText().trim());
                    this.device.setType(typeField.getText().trim());
                    this.device.setAddress(addressField.getText().trim());
                    this.device.setPort(Integer.parseInt(portField.getText().trim()));
                    this.device.setLocation(locationField.getText().trim());
                    this.device.setStatus((String) statusComboBox.getSelectedItem());
                    this.device.setPaperStatus((String) paperStatusComboBox.getSelectedItem());
                    this.device.setInkLevel(Integer.parseInt(inkLevelField.getText().trim()));
                    this.device.setRemark(remarkArea.getText().trim());

                    result = printDeviceDAO.updatePrintDevice(this.device);
                    successMessage = "打印机更新成功";
                }

                if (result) {
                    success = true;
                    JOptionPane.showMessageDialog(this, successMessage, "成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "操作失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 删除按钮事件
        deleteButton.addActionListener(e -> {
            if (this.device != null) {
                // 显示确认对话框
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "确定要删除打印机'" + this.device.getName() + "'吗？\n此操作不可恢复！",
                        "删除确认",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    // 执行删除操作
                    boolean deleteResult = printDeviceDAO.deletePrintDevice(this.device.getId());
                    if (deleteResult) {
                        success = true;
                        JOptionPane.showMessageDialog(this, "打印机删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "打印机删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancelButton.addActionListener(e -> {
            dispose();
        });

        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void setupLayout() {
        // 设置布局
    }

    private void addListeners() {
        // 添加监听器
    }

    /**
     * 验证表单
     * 
     * @return 是否验证通过
     */
    private boolean validateForm() {
        // 设备名称验证
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入设备名称", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 设备型号验证
        if (modelField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入设备型号", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 设备类型验证
        if (typeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入设备类型", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // IP地址验证
        String address = addressField.getText().trim();
        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入连接地址", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 验证IP地址格式
        if (!isValidIpAddress(address)) {
            JOptionPane.showMessageDialog(this, "请输入有效的IP地址", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 端口验证
        try {
            int port = Integer.parseInt(portField.getText().trim());
            if (port < 0 || port > 65535) {
                JOptionPane.showMessageDialog(this, "端口必须在1-65535之间", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "端口必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 门店ID验证
        try {
            Long.parseLong(storeIdField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "门店ID必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 墨水水平验证
        try {
            Integer inkLevel = Integer.parseInt(inkLevelField.getText().trim());
            if (inkLevel < 0 || inkLevel > 100) {
                JOptionPane.showMessageDialog(this, "墨水水平必须在0-100之间", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "墨水水平必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * 验证IP地址格式是否正确
     * 
     * @param ipAddress IP地址字符串
     * @return 是否为有效的IP地址
     */
    private boolean isValidIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }

        // 允许localhost和127.0.0.1
        if (ipAddress.equalsIgnoreCase("localhost") || ipAddress.equals("127.0.0.1")) {
            return true;
        }

        // 简单的IP地址正则表达式验证
        String ipRegex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ipAddress.matches(ipRegex);
    }

    /**
     * 是否添加成功
     * 
     * @return 成功返回true，否则返回false
     */
    public boolean isSuccess() {
        return success;
    }
}