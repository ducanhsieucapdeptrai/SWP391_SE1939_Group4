package DAO;

import dal.DBContext;
import model.MaterialStatistic;
import model.Statistic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StatisticDAO extends DBContext {

    public List<MaterialStatistic> getImportStatistics(List<Integer> materialIds, Date startDate, Date endDate) {
        List<MaterialStatistic> statistics = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT m.MaterialId, m.MaterialName, c.CategoryName, sc.SubCategoryName,
                   il.ImportDate, id.Quantity, u.FullName, il.Note
            FROM Materials m
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            JOIN ImportDetail id ON m.MaterialId = id.MaterialId
            JOIN ImportList il ON id.ImportId = il.ImportId
            JOIN Users u ON il.ImportedBy = u.UserId
            WHERE 1=1
        """);

        if (materialIds != null && !materialIds.isEmpty()) {
            sql.append(" AND m.MaterialId IN (")
               .append("?,".repeat(materialIds.size()))
               .deleteCharAt(sql.length() - 1) // remove last comma
               .append(")");
        }

        if (startDate != null) sql.append(" AND il.ImportDate >= ?");
        if (endDate != null) sql.append(" AND il.ImportDate <= ?");
        sql.append(" ORDER BY il.ImportDate DESC");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (materialIds != null && !materialIds.isEmpty()) {
                for (Integer id : materialIds) ps.setInt(paramIndex++, id);
            }
            if (startDate != null) ps.setDate(paramIndex++, new java.sql.Date(startDate.getTime()));
            if (endDate != null) ps.setDate(paramIndex++, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialStatistic stat = new MaterialStatistic();
                    stat.setMaterialId(rs.getInt("MaterialId"));
                    stat.setMaterialName(rs.getString("MaterialName"));
                    stat.setCategoryName(rs.getString("CategoryName"));
                    stat.setSubCategoryName(rs.getString("SubCategoryName"));
                    stat.setTransactionDate(rs.getDate("ImportDate"));
                    stat.setQuantity(rs.getInt("Quantity"));
                    stat.setPerformedBy(rs.getString("FullName"));
                    stat.setNote(rs.getString("Note"));
                    stat.setTransactionType("IMPORT");
                    statistics.add(stat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }

    public List<MaterialStatistic> getExportStatistics(List<Integer> materialIds, Date startDate, Date endDate) {
        List<MaterialStatistic> statistics = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT m.MaterialId, m.MaterialName, c.CategoryName, sc.SubCategoryName,
                   el.ExportDate, ed.Quantity, u.FullName, el.Note
            FROM Materials m
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            JOIN ExportDetail ed ON m.MaterialId = ed.MaterialId
            JOIN ExportList el ON ed.ExportId = el.ExportId
            JOIN Users u ON el.ExportedBy = u.UserId
            WHERE 1=1
        """);

        if (materialIds != null && !materialIds.isEmpty()) {
            sql.append(" AND m.MaterialId IN (")
               .append("?,".repeat(materialIds.size()))
               .deleteCharAt(sql.length() - 1)
               .append(")");
        }

        if (startDate != null) sql.append(" AND el.ExportDate >= ?");
        if (endDate != null) sql.append(" AND el.ExportDate <= ?");
        sql.append(" ORDER BY el.ExportDate DESC");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (materialIds != null && !materialIds.isEmpty()) {
                for (Integer id : materialIds) ps.setInt(paramIndex++, id);
            }
            if (startDate != null) ps.setDate(paramIndex++, new java.sql.Date(startDate.getTime()));
            if (endDate != null) ps.setDate(paramIndex++, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialStatistic stat = new MaterialStatistic();
                    stat.setMaterialId(rs.getInt("MaterialId"));
                    stat.setMaterialName(rs.getString("MaterialName"));
                    stat.setCategoryName(rs.getString("CategoryName"));
                    stat.setSubCategoryName(rs.getString("SubCategoryName"));
                    stat.setTransactionDate(rs.getDate("ExportDate"));
                    stat.setQuantity(rs.getInt("Quantity"));
                    stat.setPerformedBy(rs.getString("FullName"));
                    stat.setNote(rs.getString("Note"));
                    stat.setTransactionType("EXPORT");
                    statistics.add(stat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }

    public List<Statistic> getInventoryStatistics(List<Integer> materialIds, Date startDate, Date endDate) {
        List<Statistic> statistics = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT m.MaterialId, m.MaterialName, m.Quantity as CurrentStock
            FROM Materials m
            WHERE 1=1
        """);

        if (materialIds != null && !materialIds.isEmpty()) {
            sql.append(" AND m.MaterialId IN (")
               .append("?,".repeat(materialIds.size()))
               .deleteCharAt(sql.length() - 1)
               .append(")");
        }

        sql.append(" ORDER BY m.MaterialName");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (materialIds != null && !materialIds.isEmpty()) {
                for (Integer id : materialIds) ps.setInt(paramIndex++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Statistic stat = new Statistic();
                    stat.setMaterialId(rs.getInt("MaterialId"));
                    stat.setMaterialName(rs.getString("MaterialName"));
                    stat.setFinalStock(rs.getInt("CurrentStock"));
                    stat.setInitialStock(0);
                    stat.setTotalImported(0);
                    stat.setTotalExported(0);
                    statistics.add(stat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }
}
