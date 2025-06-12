package DAO;

import dal.DBContext;
import model.RequestList;
import model.RequestType;
import model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public static List<RequestList> getPendingRequests() {
        List<RequestList> list = new ArrayList<>();

        String sql = "SELECT rl.RequestId, rl.RequestTypeId, rt.RequestTypeName, u.FullName "
                + "FROM RequestList rl "
                + "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "JOIN Users u ON rl.RequestedBy = u.UserId "
                + "WHERE rl.Status = 'Pending'";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));

                // Set user
                Users user = new Users();
                user.setFullName(rs.getString("FullName"));
                r.setRequester(user);

                // Set request type
                RequestType type = new RequestType();
                type.setRequestTypeId(rs.getInt("RequestTypeId"));
                type.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setRequestType(type);

                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
