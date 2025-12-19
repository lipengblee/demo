
package com.cloudprint.gui;

import com.cloudprint.dao.SystemUserDAO;
import com.cloudprint.model.SystemUser;
import com.cloudprint.util.UserSession;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录对话框
 */
public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private boolean authenticated = false;

    public LoginDialog(Frame parent) {
        super(parent, "云小象打印系统", true);

        // 设置FlatLaf主题
        FlatLightLaf.setup();

        // 设置对话框属性
        setSize(400, 420);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // 创建UI组件
        initComponents();

        // 设置布局
        layoutComponents();

        // 添加事件监听器
        setupEventHandlers();
    }

    /**
     * 初始化UI组件
     */
    private void initComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("登录");
        cancelButton = new JButton("取消");
    }

    /**
     * 布局UI组件
     */
    private void layoutComponents() {
        // 设置主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // 添加logo
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo-init.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledIcon);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(0, 0, 10, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            mainPanel.add(logoLabel, gbc);
        } catch (Exception e) {
            System.err.println("无法加载logo: " + e.getMessage());
        }
        
        // 标题标签
        JLabel titleLabel = new JLabel("云小象智能打印调度系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);
        mainPanel.add(usernameField, gbc);

        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 0, 5, 10);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);
        mainPanel.add(passwordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        // 设置内容面板
        setContentPane(mainPanel);
    }

    /**
     * 设置事件处理
     */
    private void setupEventHandlers() {
        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                        "用户名和密码不能为空", "登录错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 验证用户名和密码
                if (authenticate(username, password)) {
                    // 保存当前登录用户信息
                    SystemUserDAO userDAO = new SystemUserDAO();
                    SystemUser user = userDAO.findByUsername(username);
                    if (user != null) {
                        UserSession.setCurrentUser(user);
                    }

                    authenticated = true;
                    dispose(); // 关闭登录对话框
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                        "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                }
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticated = false;
                dispose(); // 关闭登录对话框
            }
        });

        // 回车键登录
        passwordField.addActionListener(loginButton.getActionListeners()[0]);
    }

    /**
     * 验证用户名和密码
     * @param username 用户名
     * @param password 密码
     * @return 验证是否成功
     */
    private boolean authenticate(String username, String password) {
        // 使用SystemUserDAO进行数据库验证
        SystemUserDAO userDAO = new SystemUserDAO();
        SystemUser user = userDAO.findByUsernameAndPassword(username, password);

        if (user != null) {
            // 验证成功，更新最后登录信息
            try {
                String loginIp = java.net.InetAddress.getLocalHost().getHostAddress();
                userDAO.updateLoginInfo(user.getId(), loginIp);
            } catch (Exception e) {
                System.err.println("获取本地IP失败: " + e.getMessage());
            }
            return true;
        }

        // 数据库验证失败，回退到硬编码验证（仅用于测试）
        return "admin".equals(username) && "123456".equals(password);
    }

    /**
     * 获取认证状态
     * @return 是否已认证
     */
    public boolean isAuthenticated() {
        return authenticated;
    }
}
