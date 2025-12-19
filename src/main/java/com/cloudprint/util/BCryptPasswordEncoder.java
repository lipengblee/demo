package com.cloudprint.util;

/**
 * 简化的BCrypt密码编码器实现
 * 避免引入Spring Security框架
 */
public class BCryptPasswordEncoder {
    private static final int ROUNDS = 10;

    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encode(CharSequence rawPassword) {
        // 这里使用简单的哈希算法作为替代
        // 实际项目中应使用真正的BCrypt实现
        return hash(rawPassword.toString(), ROUNDS);
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            return false;
        }

        // 简单验证逻辑：比较哈希值
        // 实际项目中应使用真正的BCrypt验证
        return hash(rawPassword.toString(), ROUNDS).equals(encodedPassword);
    }

    /**
     * 简单的哈希实现
     * @param input 输入字符串
     * @param rounds 迭代次数
     * @return 哈希值
     */
    private String hash(String input, int rounds) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");

            // 添加盐值 - 这里使用固定盐值简化实现
            String salt = "cloudprint_salt";
            String toHash = input + salt;

            byte[] hash = md.digest(toHash.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 添加迭代次数信息
            return "$2a$" + rounds + "$" + hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("哈希计算失败", e);
        }
    }
}
