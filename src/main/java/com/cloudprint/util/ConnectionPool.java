package com.cloudprint.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 数据库连接池实现
 * 提高数据库连接效率，避免频繁创建和关闭连接
 */
public class ConnectionPool {
    private static final String URL = "jdbc:mysql://223.6.252.164:3306/star_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&connectTimeout=5000";
    private static final String USER = "star_test";
    private static final String PASSWORD = "htKkJfrdJWXXLpAw";

    // 连接池配置
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 20;

    // 连接池
    private static List<Connection> connectionPool = new ArrayList<>();
    private static List<Connection> usedConnections = new ArrayList<>();

    private static boolean driverLoaded = false;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
            System.out.println("MySQL驱动加载成功");

            // 初始化连接池
            initializePool();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL驱动加载失败: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "MySQL驱动加载失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 初始化连接池
     */
    private static void initializePool() {
        if (!driverLoaded) return;

        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                connectionPool.add(conn);
                System.out.println("初始化连接池，创建连接: " + conn);
            } catch (SQLException e) {
                System.err.println("创建数据库连接失败: " + e.getMessage());
            }
        }

        System.out.println("连接池初始化完成，初始连接数: " + connectionPool.size());
    }

    /**
     * 从连接池获取连接
     * @return 数据库连接
     */
    public static synchronized Connection getConnection() {
        if (!driverLoaded) {
            System.err.println("MySQL驱动未加载");
            return null;
        }

        // 如果连接池中没有可用连接，且连接池大小未达到最大值，创建新连接
        if (connectionPool.isEmpty() && usedConnections.size() < MAX_POOL_SIZE) {
            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("连接池为空，创建新连接: " + conn);
                usedConnections.add(conn);
                return conn;
            } catch (SQLException e) {
                System.err.println("创建数据库连接失败: " + e.getMessage());
                showErrorDialog("数据库连接失败: " + e.getMessage());
                return null;
            }
        }

        // 如果连接池为空且已达到最大连接数，等待一段时间
        while (connectionPool.isEmpty()) {
            try {
                System.out.println("连接池已满，等待可用连接...");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("等待连接被中断: " + e.getMessage());
                return null;
            }
        }

        // 从连接池中获取连接
        Connection conn = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(conn);

        try {
            // 检查连接是否有效
            if (conn.isClosed() || !conn.isValid(1)) {
                System.out.println("连接无效，重新创建: " + conn);
                conn.close();
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                usedConnections.remove(conn);
                usedConnections.add(conn);
            }
        } catch (SQLException e) {
            System.err.println("检查连接有效性失败: " + e.getMessage());
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println("关闭无效连接失败: " + ex.getMessage());
            }
            usedConnections.remove(conn);
            return getConnection(); // 递归调用，获取新连接
        }

        System.out.println("从连接池获取连接: " + conn + "，当前可用连接数: " + connectionPool.size() + "，已用连接数: " + usedConnections.size());
        return conn;
    }

    /**
     * 将连接返回连接池
     * @param conn 要返回的连接
     */
    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                // 检查连接是否有效
                if (!conn.isClosed() && conn.isValid(1)) {
                    usedConnections.remove(conn);
                    connectionPool.add(conn);
                    System.out.println("连接返回连接池: " + conn + "，当前可用连接数: " + connectionPool.size() + "，已用连接数: " + usedConnections.size());
                } else {
                    System.out.println("连接无效，不返回连接池: " + conn);
                    usedConnections.remove(conn);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("检查连接有效性失败: " + e.getMessage());
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.err.println("关闭无效连接失败: " + ex.getMessage());
                }
                usedConnections.remove(conn);
            }
        }
    }

    /**
     * 关闭所有连接
     */
    public static synchronized void closeAllConnections() {
        // 关闭连接池中的连接
        for (Connection conn : connectionPool) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("关闭连接池连接: " + conn);
                }
            } catch (SQLException e) {
                System.err.println("关闭连接失败: " + e.getMessage());
            }
        }

        // 关闭正在使用的连接
        for (Connection conn : usedConnections) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("关闭使用中的连接: " + conn);
                }
            } catch (SQLException e) {
                System.err.println("关闭连接失败: " + e.getMessage());
            }
        }

        connectionPool.clear();
        usedConnections.clear();
        System.out.println("所有数据库连接已关闭");
    }

    /**
     * 测试连接池
     * @return 是否连接成功
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            return false;
        } finally {
            releaseConnection(conn);
        }
    }

    /**
     * 显示错误对话框
     * @param message 错误信息
     */
    private static void showErrorDialog(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, "数据库连接错误", JOptionPane.ERROR_MESSAGE);
        });
    }
}
