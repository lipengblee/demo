package com.cloudprint.dao;

import com.cloudprint.model.ProductPrintDocument;
import com.cloudprint.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductPrintDocumentDAO {
    
    /**
     * 根据文档ID查询文档信息
     * @param documentId 文档ID
     * @return 文档对象
     */
    public ProductPrintDocument getDocumentById(Long documentId) {
        ProductPrintDocument document = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询文档");
                return null;
            }
            
            String sql = "SELECT * FROM product_print_document WHERE id = ? AND deleted = b'0'";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, documentId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                document = new ProductPrintDocument();
                document.setId(rs.getLong("id"));
                document.setUserId(rs.getLong("user_id"));
                document.setName(rs.getString("name"));
                document.setOriginalName(rs.getString("original_name"));
                document.setFileType(rs.getString("file_type"));
                document.setFileSize(rs.getLong("file_size"));
                document.setFileUrl(rs.getString("file_url"));
                document.setTransferPdfFileUrl(rs.getString("transfer_pdf_file_url"));
                document.setThumbnailUrl(rs.getString("thumbnail_url"));
                document.setPageCount(rs.getInt("page_count"));
                document.setStatus(rs.getInt("status"));
                document.setFileHash(rs.getString("file_hash"));
                document.setRemark(rs.getString("remark"));
                document.setCreator(rs.getString("creator"));
                document.setCreateTime(rs.getTimestamp("create_time"));
                document.setUpdater(rs.getString("updater"));
                document.setUpdateTime(rs.getTimestamp("update_time"));
                document.setDeleted(rs.getBoolean("deleted"));
                document.setTenantId(rs.getLong("tenant_id"));
                document.setUserSoftDelete(rs.getInt("user_soft_delete"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return document;
    }
    
    /**
     * 根据spu_id和sku_id查询关联的文档ID
     * @param spuId SPU ID
     * @param skuId SKU ID
     * @return 文档ID
     */
    public Long getDocumentIdBySpuAndSku(Long spuId, Long skuId) {
        Long documentId = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询文档关联");
                return null;
            }
            
            // 只使用spu_id和sku_id进行精确匹配查询
            String sql = "SELECT document_id FROM product_print_document_spu WHERE spu_id = ? AND sku_id = ? AND deleted = b'0' LIMIT 1";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, spuId);
            ps.setLong(2, skuId);
            
            System.out.println("执行SQL: " + sql);
            System.out.println("spuId: " + spuId + ", skuId: " + skuId);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                documentId = rs.getLong("document_id");
                System.out.println("查询到的documentId: " + documentId);
            }
            
        } catch (SQLException e) {
            System.err.println("查询文档关联失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return documentId;
    }
}