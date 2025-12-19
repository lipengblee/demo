package com.cloudprint;

import com.cloudprint.dao.TradeDeliveryPickUpStoreDAO;
import com.cloudprint.gui.LoginDialog;
import com.cloudprint.gui.MainFrame;
import com.cloudprint.gui.StoreSelectionDialog;
import com.cloudprint.model.SystemUser;
import com.cloudprint.model.TradeDeliveryPickUpStore;
import com.cloudprint.util.UserSession;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class FactoryServerApplication {
    // 开发模式开关 - 设为true时跳过登录直接进入主界面
    private static final boolean DEVELOPMENT_MODE = true;

    public static void main(String[] args) {
        System.out.println("开始启动云小象打印机管理系统");

        // 先进行简单的Swing测试，确认Swing正常工作
        SwingUtilities.invokeLater(() -> {
            System.out.println("在Swing EDT线程中创建登录界面");

            try {
                // 设置FlatLaf主题
                FlatLightLaf.setup();

                // 根据开发模式决定是否显示登录对话框
                if (DEVELOPMENT_MODE) {
                    System.out.println("开发模式：跳过登录，直接创建主界面");
                    // 创建ID为3的门店对象
                    TradeDeliveryPickUpStoreDAO storeDAO = new TradeDeliveryPickUpStoreDAO();
                    TradeDeliveryPickUpStore defaultStore = storeDAO.getStoreById(3L);
                    
                    if (defaultStore == null) {
                        System.err.println("无法获取ID为3的门店信息，使用默认值");
                        defaultStore = new TradeDeliveryPickUpStore();
                        defaultStore.setId(3L);
                        defaultStore.setName("默认门店");
                    }
                    
                    // 创建主界面
                    new MainFrame(defaultStore);
                    System.out.println("主界面创建完成，使用门店: " + defaultStore.getName());
                } else {
                    // 创建并显示登录对话框
                    LoginDialog loginDialog = new LoginDialog(null);
                    loginDialog.setVisible(true);

                    // 检查登录结果
                    if (loginDialog.isAuthenticated()) {
                        System.out.println("用户登录成功，显示门店选择界面");

                        // 获取当前登录用户ID
                        SystemUser currentUser = UserSession.getCurrentUser();
                        if (currentUser == null) {
                            System.err.println("当前用户为null，无法获取用户ID");
                            return;
                        }
                        Long userId = currentUser.getId();
                        System.out.println("当前登录用户ID: " + userId);

                        // 创建并显示门店选择对话框
                        StoreSelectionDialog storeSelectionDialog = new StoreSelectionDialog(null, userId);
                        storeSelectionDialog.setVisible(true);

                        // 检查门店选择结果
                        com.cloudprint.model.TradeDeliveryPickUpStore selectedStore = storeSelectionDialog.getSelectedStore();
                        if (selectedStore != null) {
                            System.out.println("用户选择了门店: " + selectedStore.getName());
                            // 保存选择的门店
                            com.cloudprint.util.UserSession.setCurrentStore(selectedStore);

                            // 创建主界面
                            new MainFrame(selectedStore);
                            System.out.println("主界面创建完成");
                        } else {
                            System.out.println("用户未选择门店，退出应用");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("用户取消登录或登录失败，退出应用");
                        System.exit(0);
                    }
                }

            } catch (Exception e) {
                System.err.println("创建界面失败: " + e.getMessage());
                e.printStackTrace();

                // 如果界面创建失败，显示一个简单的错误窗口
                JFrame errorFrame = new JFrame("启动失败");
                errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                errorFrame.setSize(600, 400);
                errorFrame.setLocationRelativeTo(null);

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(Color.WHITE);

                JLabel errorLabel = new JLabel("启动失败: " + e.getMessage(), JLabel.CENTER);
                errorLabel.setForeground(Color.RED);
                errorLabel.setFont(new Font("宋体", Font.BOLD, 16));
                panel.add(errorLabel, BorderLayout.CENTER);

                errorFrame.setContentPane(panel);
                errorFrame.setVisible(true);
            }
        });
    }
}
