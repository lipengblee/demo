package com.cloudprint.gui;

import com.cloudprint.dao.PrintDeviceDAO;
import com.cloudprint.util.NetworkPrinterScanner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描打印机对话框，用于显示搜索到的局域网打印机
 */
public class ScanPrintersDialog extends JDialog {
    private PrintDeviceDAO printDeviceDAO;
    private JTable printerTable;
    private DefaultTableModel printerTableModel;
    private JButton addButton;
    private JButton cancelButton;
    private JButton refreshButton;
    private boolean success = false;
    private List<NetworkPrinterScanner.NetworkPrinter> allPrinters; // 存储所有搜索到的打印机
    private JLabel loadingLabel; // 加载指示器
    private JProgressBar loadingProgressBar; // 进度条
    private JPanel loadingPanel; // 加载面板
    private CardLayout cardLayout; // 卡片布局，用于切换视图
    private JPanel contentPanel; // 内容面板

    public ScanPrintersDialog(JFrame parent, PrintDeviceDAO printDeviceDAO) {
        super(parent, "扫描局域网打印机", true);
        this.printDeviceDAO = printDeviceDAO;
        this.allPrinters = new ArrayList<>();
        initComponents();
        setupLayout();
        addListeners();
        setLocationRelativeTo(parent);
        
        // 开始扫描打印机
        scanPrinters();
    }

    private void initComponents() {
        // 初始化表格模型和表格
        String[] columnNames = {"打印机名称", "IP地址", "端口"};
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
        printerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        printerTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        printerTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        
        // 设置所有列内容居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < printerTable.getColumnCount(); i++) {
            printerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 初始化加载指示器
        loadingLabel = new JLabel("正在扫描局域网中的打印机...");
        loadingLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loadingLabel.setForeground(Color.GRAY);
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // 初始化进度条
        loadingProgressBar = new JProgressBar();
        loadingProgressBar.setIndeterminate(true); // 无限循环进度条
        loadingProgressBar.setPreferredSize(new Dimension(300, 20));
        
        // 初始化加载面板
        loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout(10, 10));
        loadingPanel.setBackground(Color.WHITE);
        loadingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        loadingPanel.add(loadingLabel, BorderLayout.NORTH);
        loadingPanel.add(loadingProgressBar, BorderLayout.CENTER);
        loadingPanel.setVisible(false); // 初始隐藏
        
        // 初始化按钮
        addButton = new JButton("添加选中的打印机");
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        addButton.setBackground(new Color(0xec, 0x84, 0x21));
        addButton.setForeground(Color.WHITE);
        addButton.setEnabled(false); // 初始禁用
        
        refreshButton = new JButton("重新扫描");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setForeground(Color.BLACK);
        
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.BLACK);
        
        // 设置按钮大小
        Dimension buttonSize = new Dimension(150, 35);
        addButton.setPreferredSize(buttonSize);
        refreshButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
    }

    private void setupLayout() {
        // 主面板
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // 标题
        JLabel titleLabel = new JLabel("搜索到的打印机");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // 表格滚动面板
        JScrollPane tableScrollPane = new JScrollPane(printerTable);
        tableScrollPane.setBorder(new EmptyBorder(5, 15, 5, 15));
        tableScrollPane.setPreferredSize(new Dimension(500, 300));
        
        // 创建卡片布局和内容面板
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // 添加视图到卡片布局
        contentPanel.add(loadingPanel, "loading");
        contentPanel.add(tableScrollPane, "table");
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
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
        add(contentPanel, BorderLayout.CENTER);
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
        
        // 重新扫描按钮
        refreshButton.addActionListener(e -> {
            scanPrinters();
        });
        
        // 添加按钮
        addButton.addActionListener(e -> {
            int selectedRow = printerTable.getSelectedRow();
            if (selectedRow != -1) {
                NetworkPrinterScanner.NetworkPrinter selectedPrinter = allPrinters.get(selectedRow);
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
     * 扫描局域网中的打印机
     */
    private void scanPrinters() {
        // 清空表格
        printerTableModel.setRowCount(0);
        addButton.setEnabled(false);
        allPrinters.clear();
        
        // 显示加载视图
        cardLayout.show(contentPanel, "loading");
        
        // 在后台线程中扫描打印机
        SwingWorker<List<NetworkPrinterScanner.NetworkPrinter>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<NetworkPrinterScanner.NetworkPrinter> doInBackground() {
                NetworkPrinterScanner scanner = new NetworkPrinterScanner();
                return scanner.scanLocalNetworkPrinters();
            }
            
            @Override
            protected void done() {
                try {
                    List<NetworkPrinterScanner.NetworkPrinter> printers = get();
                    allPrinters.addAll(printers);
                    
                    // 打印所有数据到控制台
                    System.out.println("=== 局域网打印机扫描结果 ===");
                    System.out.printf("%-20s %-15s %-6s %-55s %-10s\n", "打印机名称", "IP地址", "端口", "设备型号", "设备类型");
                    System.out.println("-".repeat(70));
                    
                    // 清空表格并添加扫描结果
                    printerTableModel.setRowCount(0);
                    if (printers.isEmpty()) {
                        // 没有找到打印机
                        JOptionPane.showMessageDialog(ScanPrintersDialog.this, "未搜索到局域网中的打印机", "提示", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("未搜索到任何打印机");
                    } else {
                        for (NetworkPrinterScanner.NetworkPrinter printer : printers) {
                            // 添加到表格
                            Object[] rowData = {
                                printer.getName(),
                                printer.getIpAddress(),
                                printer.getPort()
                            };
                            printerTableModel.addRow(rowData);
                            
                            // 打印到控制台
                            System.out.printf("%-20s %-15s %-6d %-55s %-10s\n",
                                printer.getName(), 
                                printer.getIpAddress(), 
                                printer.getPort(),
                                printer.getModel(),
                                printer.getType());
                        }
                        System.out.println("-".repeat(70));
                        System.out.printf("共搜索到 %d 台打印机\n", printers.size());
                    }
                    System.out.println("==========================");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ScanPrintersDialog.this, "扫描打印机失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    printerTableModel.setRowCount(0);
                } finally {
                    // 显示表格视图
                    cardLayout.show(contentPanel, "table");
                }
            }
        };
        worker.execute();
    }

    /**
     * 将选中的打印机添加到系统
     * @param networkPrinter 网络打印机
     */
    private void addPrinterToSystem(NetworkPrinterScanner.NetworkPrinter networkPrinter) {
        // 创建添加打印机对话框，使用带预填信息的构造函数
        AddPrinterDialog addDialog = new AddPrinterDialog(
                (JFrame) getParent(), 
                printDeviceDAO, 
                networkPrinter.getName(),
                networkPrinter.getModel(),
                networkPrinter.getType(),
                networkPrinter.getIpAddress(),
                networkPrinter.getPort()
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
