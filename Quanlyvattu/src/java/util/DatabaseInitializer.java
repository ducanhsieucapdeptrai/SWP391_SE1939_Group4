package util;

import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "DatabaseInitializer", loadOnStartup = 1)
public class DatabaseInitializer extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            DBContext dbContext = new DBContext();
            try (Connection conn = dbContext.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet tables = meta.getTables(null, null, "audit_log", null);
                if (!tables.next()) {
                    System.out.println("Creating 'audit_log' table...");
                    try (Statement stmt = conn.createStatement()) {
                        String createTableSql = "CREATE TABLE `audit_log` (" +
                            "`id` INT AUTO_INCREMENT PRIMARY KEY, " +
                            "`action` VARCHAR(255) NOT NULL, " +
                            "`user_id` INT NOT NULL, " +
                            "`timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "`details` TEXT, " +
                            "FOREIGN KEY (user_id) REFERENCES Users(UserId))";
                        stmt.executeUpdate(createTableSql);
                        System.out.println("'audit_log' table created successfully.");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("Failed to initialize the database.", e);
        }
    }
}
