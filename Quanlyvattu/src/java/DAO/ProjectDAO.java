package DAO;

import dal.DBContext;
import model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DBContext {

    public List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql = """
            SELECT p.*, u.FullName AS ManagerName, u.Email AS ManagerEmail
            FROM Project p
            LEFT JOIN Users u ON p.ManagerId = u.UserId
            WHERE p.Status != 'Deleted'
            ORDER BY p.ProjectId DESC
        """;

        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("ProjectId"));
                p.setProjectName(rs.getString("ProjectName"));
                p.setDescription(rs.getString("Description"));
                p.setStartDate(rs.getDate("StartDate"));
                p.setEndDate(rs.getDate("EndDate"));
                p.setManagerId(rs.getInt("ManagerId"));
                p.setStatus(rs.getString("Status"));
                p.setAttachmentPath(rs.getString("AttachmentPath"));
                p.setManagerName(rs.getString("ManagerName"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Project getProjectById(int id) {
        String sql = "SELECT p.*, u.FullName AS ManagerName FROM Project p " +
                     "LEFT JOIN Users u ON p.ManagerId = u.UserId WHERE p.ProjectId = ?";
        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("ProjectId"));
                p.setProjectName(rs.getString("ProjectName"));
                p.setDescription(rs.getString("Description"));
                p.setStartDate(rs.getDate("StartDate"));
                p.setEndDate(rs.getDate("EndDate"));
                p.setManagerId(rs.getInt("ManagerId"));
                p.setStatus(rs.getString("Status"));
                p.setAttachmentPath(rs.getString("AttachmentPath"));
                p.setManagerName(rs.getString("ManagerName"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertProject(Project p) {
        String sql = """
            INSERT INTO Project (ProjectName, Description, StartDate, EndDate, ManagerId, Status, AttachmentPath)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getProjectName());
            ps.setString(2, p.getDescription());
            ps.setDate(3, p.getStartDate());
            ps.setDate(4, p.getEndDate());
            ps.setInt(5, p.getManagerId());
            ps.setString(6, p.getStatus());
            ps.setString(7, p.getAttachmentPath());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProject(Project p) {
        String sql = """
            UPDATE Project
            SET ProjectName = ?, Description = ?, StartDate = ?, EndDate = ?, ManagerId = ?, Status = ?, AttachmentPath = ?
            WHERE ProjectId = ?
        """;
        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getProjectName());
            ps.setString(2, p.getDescription());
            ps.setDate(3, p.getStartDate());
            ps.setDate(4, p.getEndDate());
            ps.setInt(5, p.getManagerId());
            ps.setString(6, p.getStatus());
            ps.setString(7, p.getAttachmentPath());
            ps.setInt(8, p.getProjectId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void softDeleteProject(int id) {
        String sql = "UPDATE Project SET Status='Inactive' WHERE ProjectId=?";
        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Project> getFilteredProjects(String projectName, String managerName, String createdDate, String status) {
        List<Project> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.*, u.FullName as ManagerName " +
            "FROM Project p " +
            "JOIN Users u ON p.ManagerId = u.UserId " +
            "WHERE p.Status != 'Deleted'"
        );

        List<Object> params = new ArrayList<>();

        if (projectName != null && !projectName.isEmpty()) {
            sql.append(" AND p.ProjectName LIKE ?");
            params.add("%" + projectName + "%");
        }
        if (managerName != null && !managerName.isEmpty()) {
            sql.append(" AND u.FullName LIKE ?");
            params.add("%" + managerName + "%");
        }
        if (createdDate != null && !createdDate.isEmpty()) {
            sql.append(" AND p.StartDate = ?");
            params.add(Date.valueOf(createdDate));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND p.Status = ?");
            params.add(status);
        }

        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("ProjectId"));
                p.setProjectName(rs.getString("ProjectName"));
                p.setDescription(rs.getString("Description"));
                p.setStartDate(rs.getDate("StartDate"));
                p.setEndDate(rs.getDate("EndDate"));
                p.setManagerId(rs.getInt("ManagerId"));
                p.setStatus(rs.getString("Status"));
                p.setAttachmentPath(rs.getString("AttachmentPath"));
                p.setManagerName(rs.getString("ManagerName"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void markProjectAsCompleted(int projectId) {
        String sql = "UPDATE Project SET Status = 'Completed' WHERE ProjectId = ?";
        try (Connection conn = getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public boolean areAllRequestsCompleted(int projectId) {
    String sql = """
        SELECT COUNT(*) AS Total,
               SUM(CASE WHEN Status = 'Completed' THEN 1 ELSE 0 END) AS Completed
        FROM RequestList
        WHERE ProjectId = ?
    """;

    try (Connection conn = getNewConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, projectId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int total = rs.getInt("Total");
                int completed = rs.getInt("Completed");
                return total > 0 && total == completed;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
public int countAllProjects() {
    String sql = "SELECT COUNT(*) FROM Project WHERE Status != 'Deleted'";
    try (Connection conn = getNewConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public List<Project> getPagedProjects(int offset, int limit) {
    List<Project> list = new ArrayList<>();
    String sql = """
        SELECT p.*, u.FullName AS ManagerName
        FROM Project p
        LEFT JOIN Users u ON p.ManagerId = u.UserId
        WHERE p.Status != 'Deleted'
        ORDER BY p.ProjectId DESC
        LIMIT ? OFFSET ?
    """;

    try (Connection conn = getNewConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, limit);
        ps.setInt(2, offset);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project p = new Project();
            p.setProjectId(rs.getInt("ProjectId"));
            p.setProjectName(rs.getString("ProjectName"));
            p.setDescription(rs.getString("Description"));
            p.setStartDate(rs.getDate("StartDate"));
            p.setEndDate(rs.getDate("EndDate"));
            p.setManagerId(rs.getInt("ManagerId"));
            p.setStatus(rs.getString("Status"));
            p.setAttachmentPath(rs.getString("AttachmentPath"));
            p.setManagerName(rs.getString("ManagerName"));
            list.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
public int countFilteredProjects(String projectName, String managerName, String createdDate, String status) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Project p JOIN Users u ON p.ManagerId = u.UserId WHERE p.Status != 'Deleted'");
    List<Object> params = new ArrayList<>();

    if (projectName != null && !projectName.isEmpty()) {
        sql.append(" AND p.ProjectName LIKE ?");
        params.add("%" + projectName + "%");
    }
    if (managerName != null && !managerName.isEmpty()) {
        sql.append(" AND u.FullName LIKE ?");
        params.add("%" + managerName + "%");
    }
    if (createdDate != null && !createdDate.isEmpty()) {
        sql.append(" AND p.StartDate = ?");
        params.add(Date.valueOf(createdDate));
    }
    if (status != null && !status.isEmpty()) {
        sql.append(" AND p.Status = ?");
        params.add(status);
    }

    try (Connection conn = getNewConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public List<Project> getFilteredProjectsPaged(String projectName, String managerName, String createdDate, String status, int offset, int limit) {
    List<Project> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder(
        "SELECT p.*, u.FullName AS ManagerName FROM Project p JOIN Users u ON p.ManagerId = u.UserId WHERE p.Status != 'Deleted'"
    );
    List<Object> params = new ArrayList<>();

    if (projectName != null && !projectName.isEmpty()) {
        sql.append(" AND p.ProjectName LIKE ?");
        params.add("%" + projectName + "%");
    }
    if (managerName != null && !managerName.isEmpty()) {
        sql.append(" AND u.FullName LIKE ?");
        params.add("%" + managerName + "%");
    }
    if (createdDate != null && !createdDate.isEmpty()) {
        sql.append(" AND p.StartDate = ?");
        params.add(Date.valueOf(createdDate));
    }
    if (status != null && !status.isEmpty()) {
        sql.append(" AND p.Status = ?");
        params.add(status);
    }

    sql.append(" ORDER BY p.ProjectId DESC LIMIT ? OFFSET ?");
    params.add(limit);
    params.add(offset);

    try (Connection conn = getNewConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project p = new Project();
            p.setProjectId(rs.getInt("ProjectId"));
            p.setProjectName(rs.getString("ProjectName"));
            p.setDescription(rs.getString("Description"));
            p.setStartDate(rs.getDate("StartDate"));
            p.setEndDate(rs.getDate("EndDate"));
            p.setManagerId(rs.getInt("ManagerId"));
            p.setStatus(rs.getString("Status"));
            p.setAttachmentPath(rs.getString("AttachmentPath"));
            p.setManagerName(rs.getString("ManagerName"));
            list.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
public boolean isProjectNameExists(String projectName) {
    String sql = "SELECT COUNT(*) FROM Project WHERE ProjectName = ? AND IsDeleted = 0";
    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, projectName);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;    
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
public List<Project> getAllActiveProjects() {
    List<Project> list = new ArrayList<>();
    String sql = "SELECT p.*, u.FullName AS ManagerName FROM Project p LEFT JOIN Users u ON p.ManagerId = u.UserId WHERE p.Status = 'Active'";

    try (Connection conn = getNewConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Project p = new Project();
            p.setProjectId(rs.getInt("ProjectId"));
            p.setProjectName(rs.getString("ProjectName"));
            p.setDescription(rs.getString("Description"));
            p.setStartDate(rs.getDate("StartDate"));
            p.setEndDate(rs.getDate("EndDate"));
            p.setManagerId(rs.getInt("ManagerId"));
            p.setStatus(rs.getString("Status"));
            p.setAttachmentPath(rs.getString("AttachmentPath"));
            p.setManagerName(rs.getString("ManagerName"));
            list.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


}
