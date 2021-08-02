import java.sql.*;

public class CheckBalance {

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/Bank", "root", "Oxford1984");
            checkBalance(con, 2);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void checkBalance(Connection connection, int id) {
        Statement statement = null;
        String showBalance = "select users.id, users.name, accounts.account_number, accounts.balance from users inner join accounts on accounts.user_id = users.id where users.id =" + id;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(showBalance);
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + " " + rs.getInt(4));
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }
}
