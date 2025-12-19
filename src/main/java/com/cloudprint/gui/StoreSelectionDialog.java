package com.cloudprint.gui;

import com.cloudprint.dao.TradeDeliveryPickUpStoreDAO;
import com.cloudprint.model.TradeDeliveryPickUpStore;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 门店选择对话框
 */
@Slf4j
public class StoreSelectionDialog extends JDialog {
    private JList<TradeDeliveryPickUpStore> storeList;
    private DefaultListModel<TradeDeliveryPickUpStore> listModel;
    private JButton selectButton;
    private JButton cancelButton;
    private TradeDeliveryPickUpStore selectedStore = null;
    private Long userId;

    public StoreSelectionDialog(Frame parent, Long userId) {
        super(parent, "选择工厂店", true);
        this.userId = userId;

        // 设置FlatLaf主题
        FlatLightLaf.setup();

        // 设置对话框属性
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // 创建UI组件
        initComponents();

        // 加载门店数据
        loadStores();

        // 设置布局
        layoutComponents();

        // 添加事件监听器
        setupEventHandlers();
    }

    /**
     * 初始化UI组件
     */
    private void initComponents() {
        listModel = new DefaultListModel<>();
        storeList = new JList<>(listModel);
        storeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        storeList.setCellRenderer(new StoreListCellRenderer());

        selectButton = new JButton("选择");
        cancelButton = new JButton("取消");
    }

    /**
     * 加载用户关联的门店
     */
    private void loadStores() {
        TradeDeliveryPickUpStoreDAO storeDAO = new TradeDeliveryPickUpStoreDAO();
        log.info("加载用户{}关联的门店", userId);
        
        if (userId == null) {
            log.error("用户ID为null，无法加载门店");
            JOptionPane.showMessageDialog(this, 
                "用户ID无效，请重新登录", 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        List<TradeDeliveryPickUpStore> stores = storeDAO.getStoresByOperatorUserId(userId);
        log.info("查询到{}个关联门店", stores.size());

        listModel.clear();
        for (TradeDeliveryPickUpStore store : stores) {
            log.info("添加门店到列表: ID={}, 名称={}", store.getId(), store.getName());
            listModel.addElement(store);
        }

        // 如果没有门店，显示提示信息
        if (listModel.isEmpty()) {
            log.warn("用户{}没有关联的门店", userId);
            JOptionPane.showMessageDialog(this, 
                "您没有关联的工厂店，请联系管理员分配权限", 
                "权限提示", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    /**
     * 布局UI组件
     */
    private void layoutComponents() {
        // 设置主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 标题标签
        JLabel titleLabel = new JLabel("请选择要进入的工厂店");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 门店列表
        JScrollPane scrollPane = new JScrollPane(storeList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 设置内容面板
        setContentPane(mainPanel);
    }

    /**
     * 设置事件处理
     */
    private void setupEventHandlers() {
        // 选择按钮事件
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = storeList.getSelectedIndex();
                if (selectedIndex != -1) {
                    selectedStore = listModel.getElementAt(selectedIndex);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(StoreSelectionDialog.this,
                        "请选择一个工厂店", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedStore = null;
                dispose();
            }
        });

        // 双击选择
        storeList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = storeList.locationToIndex(evt.getPoint());
                    if (index != -1) {
                        storeList.setSelectedIndex(index);
                        selectButton.doClick();
                    }
                }
            }
        });
    }

    /**
     * 获取选择的门店
     * @return 选择的门店，如果没有选择则返回null
     */
    public TradeDeliveryPickUpStore getSelectedStore() {
        return selectedStore;
    }

    /**
     * 门店列表单元格渲染器
     */
    private static class StoreListCellRenderer extends JLabel implements ListCellRenderer<TradeDeliveryPickUpStore> {
        public StoreListCellRenderer() {
            setOpaque(true);
            setFont(new Font("微软雅黑", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends TradeDeliveryPickUpStore> list,
                TradeDeliveryPickUpStore store, int index, boolean isSelected, boolean cellHasFocus) {

            if (store != null) {
                String text = "<html><b>" + store.getName() + "</b><br>" +
                             "<small style='color:#666'>" + store.getDetailAddress() + "</small></html>";
                setText(text);
            } else {
                setText("");
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }
}
