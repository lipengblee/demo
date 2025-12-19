package com.cloudprint.dao;

import com.cloudprint.model.SystemUser;
import com.cloudprint.util.ConnectionPool;

import java.sql.*;

/**
 * 系统用户数据访问对象
 */
public class SystemUserDAO {
    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，如果不存在返回null
     */
    public SystemUser findByUsernameAndPassword(String username, String password) {
        // 首先根据用户名查询用户
        String sql = "SELECT * FROM system_users WHERE username = ? AND deleted = 0";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SystemUser user = extractUserFromResultSet(rs);
                    // 使用PasswordValidator验证密码
                    if (!com.cloudprint.util.PasswordValidator.validatePassword(password, user.getPassword())) {
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("查询用户失败: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("密码验证失败: " + e.getMessage());
        }

        return null;
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，如果不存在返回null
     */
    public SystemUser findByUsername(String username) {
        String sql = "SELECT * FROM system_users WHERE username = ? AND deleted = 0";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("查询用户失败: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // PasswordValidator已移至util包，避免代码重复

    /**
     * 更新用户最后登录信息
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 是否更新成功
     */
    public boolean updateLoginInfo(Long userId, String loginIp) {
        String sql = "UPDATE system_users SET login_ip = ?, login_date = NOW() WHERE id = ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loginIp);
            stmt.setLong(2, userId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("更新登录信息失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String rawPassword) {
        return com.cloudprint.util.PasswordValidator.encodePassword(rawPassword);
    }

    /**
     * 从ResultSet中提取用户信息
     * @param rs 结果集
     * @return 用户对象
     * @throws SQLException SQL异常
     */
    private SystemUser extractUserFromResultSet(ResultSet rs) throws SQLException {
        SystemUser user = new SystemUser();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setRemark(rs.getString("remark"));
        user.setDeptId(rs.getLong("dept_id"));
        user.setPostIds(rs.getString("post_ids"));
        user.setEmail(rs.getString("email"));
        user.setMobile(rs.getString("mobile"));
        user.setSex(rs.getInt("sex"));
        user.setAvatar(rs.getString("avatar"));
        user.setStatus(rs.getInt("status"));
        user.setLoginIp(rs.getString("login_ip"));

        Timestamp loginDate = rs.getTimestamp("login_date");
        if (loginDate != null) {
            user.setLoginDate(loginDate.toLocalDateTime());
        }

        user.setCreator(rs.getString("creator"));

        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            user.setCreateTime(createTime.toLocalDateTime());
        }

        user.setUpdater(rs.getString("updater"));

        Timestamp updateTime = rs.getTimestamp("update_time");
        if (updateTime != null) {
            user.setUpdateTime(updateTime.toLocalDateTime());
        }

        user.setDeleted(rs.getBoolean("deleted"));
        user.setTenantId(rs.getLong("tenant_id"));

        return user;
    }
}
