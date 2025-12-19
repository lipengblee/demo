package com.cloudprint.gui;

import com.cloudprint.dao.PrintDeviceDAO;
import com.cloudprint.util.NetworkPrinterScanner;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地打印机对话框，用于显示本机关联的打印机
 */
public class LocalPrintersDialog extends JDialog {
    private PrintDeviceDAO printDeviceDAO;
    private JTable printerTable;
    private DefaultTableModel printerTableModel;
    private JButton addButton;
    private JButton cancelButton;
    private boolean success = false;
    private List<NetworkPrinterScanner.NetworkPrinter> localPrinters; // 存储所有本地打印机

    public LocalPrintersDialog(JFrame parent, PrintDeviceDAO printDeviceDAO) {
        super(parent, "本机关联打印机", true);
        this.printDeviceDAO = printDeviceDAO;
        this.localPrinters = new ArrayList<>();
        initComponents();
        setupLayout();
        addListeners();
        setLocationRelativeTo(parent);
        
        // 开始获取本地打印机
        loadLocalPrinters();
    }

    private void initComponents() {
        // 初始化表格模型和表格
        String[] columnNames = {"打印机名称", "打印机服务名称"};
        printerTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        printerTable = new JTable(printerTableModel);
        printerTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        printerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        printerTable.setRowHeight(30);
        printerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // 设置列宽
        printerTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        printerTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        
        // 设置所有列内容居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < printerTable.getColumnCount(); i++) {
            printerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 初始化按钮
        addButton = new JButton("添加选中的打印机");
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        addButton.setBackground(new Color(0xec, 0x84, 0x21));
        addButton.setForeground(Color.WHITE);
        addButton.setEnabled(false); // 初始禁用
        
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.BLACK);
        
        // 设置按钮大小
        Dimension buttonSize = new Dimension(150, 35);
        addButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
    }

    private void setupLayout() {
        // 主面板
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // 标题
        JLabel titleLabel = new JLabel("本机关联的打印机");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // 表格滚动面板
        JScrollPane tableScrollPane = new JScrollPane(printerTable);
        tableScrollPane.setBorder(new EmptyBorder(5, 15, 5, 15));
        tableScrollPane.setPreferredSize(new Dimension(500, 300));
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        // 说明文本
        JLabel infoLabel = new JLabel("提示：选择一个打印机，点击'添加'按钮将其添加到系统中");
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        // 组装面板
        add(titleLabel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);
        
        // 设置对话框属性
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(550, 450);
        setResizable(true);
    }

    private void addListeners() {
        // 表格选择事件
        printerTable.getSelectionModel().addListSelectionListener(e -> {
            addButton.setEnabled(printerTable.getSelectedRow() != -1);
        });
        
        // 添加按钮
        addButton.addActionListener(e -> {
            int selectedRow = printerTable.getSelectedRow();
            if (selectedRow != -1) {
                NetworkPrinterScanner.NetworkPrinter selectedPrinter = localPrinters.get(selectedRow);
                // 添加打印机到系统
                addPrinterToSystem(selectedPrinter);
            }
        });
        
        // 取消按钮
        cancelButton.addActionListener(e -> {
            dispose();
        });
    }

    /**
     * 获取本机关联的打印机
     */
    private void loadLocalPrinters() {
        // 清空表格
        printerTableModel.setRowCount(0);
        addButton.setEnabled(false);
        localPrinters.clear();
        
        // 使用Java Print Service API获取本地打印机
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        
        System.out.println("=== 开始获取本机关联打印机信息 ===");
        
        if (printServices.length == 0) {
            // 没有找到本地打印机
            JOptionPane.showMessageDialog(this, "未找到本机关联的打印机", "提示", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("未找到本机关联的打印机");
        } else {
            for (int i = 0; i < printServices.length; i++) {
                PrintService printService = printServices[i];
                String printerName = printService.getName();
                String serviceName = printService.toString();
                
                // 从PrintService中获取更多详细信息
                String model = extractPrinterModel(printService);
                String type = extractPrinterType(printService);
                
                // 输出完整的PrintService信息到控制台
                System.out.printf("\n--- 打印机 %d 详细信息 ---", i + 1);
                System.out.printf("\n打印服务名称: %s", printService.getName());
                System.out.printf("\n打印服务类: %s", printService.getClass().getName());
                System.out.printf("\n打印服务字符串表示: %s", printService.toString());
                System.out.printf("\n打印服务哈希码: %d", printService.hashCode());
                
                // 获取并输出打印服务属性
                try {
                    javax.print.attribute.PrintServiceAttributeSet attributes = printService.getAttributes();
                    System.out.printf("\n打印服务属性数量: %d", attributes.size());
                    System.out.println("\n属性列表:");
                    for (javax.print.attribute.Attribute attr : attributes.toArray()) {
                        System.out.printf("  - %s: %s\n", attr.getName(), attr.toString());
                    }
                } catch (Exception e) {
                    System.out.printf("\n获取打印服务属性失败: %s", e.getMessage());
                }
                
                // 创建NetworkPrinter对象（本地打印机没有IP，使用localhost）
                NetworkPrinterScanner.NetworkPrinter localPrinter = new NetworkPrinterScanner.NetworkPrinter("localhost");
                localPrinter.setName(printerName);
                localPrinter.setPort(0);
                localPrinter.setModel(model);
                localPrinter.setType(type);
                
                localPrinters.add(localPrinter);
                
                // 添加到表格
                Object[] rowData = {
                    printerName,
                    serviceName
                };
                printerTableModel.addRow(rowData);
                
                System.out.printf("\n简化输出: %s - %s\n", printerName, serviceName);
            }
            System.out.printf("\n=== 共找到 %d 台本机关联打印机 ===\n", printServices.length);
        }
    }
    
    /**
     * 从PrintService中提取打印机型号
     * @param printService 打印服务对象
     * @return 打印机型号
     */
    private String extractPrinterModel(PrintService printService) {
        try {
            String serviceStr = printService.toString();
            // 尝试从服务字符串中提取型号信息
            if (serviceStr.contains(":")) {
                // 常见格式：Printer Name (Model: XXXX)
                // 或：Printer Name, Model: XXXX
                // 或：Printer Name [XXXX]
                // 尝试多种格式提取
                if (serviceStr.contains("Model:") || serviceStr.contains("model:")) {
                    int modelIndex = serviceStr.indexOf("Model:") != -1 ? serviceStr.indexOf("Model:") : serviceStr.indexOf("model:");
                    String modelPart = serviceStr.substring(modelIndex + 6).trim();
                    if (modelPart.contains(",") || modelPart.contains(")") || modelPart.contains("]")) {
                        modelPart = modelPart.split(",|\\)|\\]")[0].trim();
                    }
                    return modelPart.isEmpty() ? "本地打印机" : modelPart;
                } else if (serviceStr.contains("(")) {
                    String modelPart = serviceStr.substring(serviceStr.indexOf("(") + 1);
                    if (modelPart.contains(")")) {
                        modelPart = modelPart.split("")[0].trim();
                        return modelPart.isEmpty() ? "本地打印机" : modelPart;
                    }
                }
                // 如果无法提取，返回服务名称的后半部分
                String[] parts = serviceStr.split(":");
                if (parts.length > 1) {
                    return parts[parts.length - 1].trim();
                }
            }
        } catch (Exception e) {
            System.out.printf("提取打印机型号失败: %s\n", e.getMessage());
        }
        return "本地打印机";
    }
    
    /**
     * 从PrintService中提取打印机类型
     * @param printService 打印服务对象
     * @return 打印机类型
     */
    private String extractPrinterType(PrintService printService) {
        try {
            String serviceStr = printService.toString().toLowerCase();
            if (serviceStr.contains("pdf") || serviceStr.contains("虚拟")) {
                return "虚拟打印机";
            } else if (serviceStr.contains("激光") || serviceStr.contains("laser")) {
                return "激光打印机";
            } else if (serviceStr.contains("喷墨") || serviceStr.contains("inkjet")) {
                return "喷墨打印机";
            } else if (serviceStr.contains("热敏") || serviceStr.contains("thermal")) {
                return "热敏打印机";
            } else if (serviceStr.contains("针式") || serviceStr.contains("dot") || serviceStr.contains("matrix")) {
                return "针式打印机";
            }
        } catch (Exception e) {
            System.out.printf("提取打印机类型失败: %s\n", e.getMessage());
        }
        return "本地打印机";
    }

    /**
     * 将选中的打印机添加到系统
     * @param localPrinter 本地打印机
     */
    private void addPrinterToSystem(NetworkPrinterScanner.NetworkPrinter localPrinter) {
        // 创建添加打印机对话框，使用带预填信息的构造函数，并标记为本地打印机
        AddPrinterDialog addDialog = new AddPrinterDialog(
                (JFrame) getParent(), 
                printDeviceDAO, 
                localPrinter.getName(),
                localPrinter.getModel(),
                localPrinter.getType(),
                localPrinter.getIpAddress(),
                localPrinter.getPort(),
                true // 标记为本地打印机
        );
        addDialog.setVisible(true);
        
        if (addDialog.isSuccess()) {
            success = true;
            JOptionPane.showMessageDialog(this, "打印机添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 是否添加成功
     * @return 成功返回true，否则返回false
     */
    public boolean isSuccess() {
        return success;
    }
}