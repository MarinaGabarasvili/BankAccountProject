import java.sql.*;

public class CheckBalance {

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/bank", "root", "danaja05");
            getInfo(con, 14);
            getInfo(con, 15);
            madeTransaction(con, getAccountNumber(con,14), getAccountNumber(con,15), 50);
            getInfo(con, 14);
            getInfo(con, 15);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void madeTransaction(Connection connection, String firstAccNumber, String secondAccNumber, int money){

        if(money >  checkBalance(connection, firstAccNumber)){
            System.out.println("There is not enough money on your bank account!");
        } else {
            CallableStatement cstmt = null;
            try {
                String SQL = "{call transaction(?,?,?)}";
                cstmt = connection.prepareCall(SQL);
                cstmt.setString(1, firstAccNumber);
                cstmt.setString(2, secondAccNumber);
                cstmt.setInt(3, money);
                cstmt.execute();
                System.out.println("Transaction made!");
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    public static int checkBalance(Connection connection, String accNumber) {
        Statement statement = null;
        Integer accountBalance = 0;
        String showBalance = "select balance from accounts where account_number ='" + accNumber + "';";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(showBalance);
            while (rs.next())
                accountBalance = rs.getInt("balance");

        } catch (SQLException err) {
            err.printStackTrace();
        }
        return accountBalance;
    }

    public static void getInfo(Connection connection, int id) {
        Statement statement = null;
        String showUserInfo = "select accounts.balance, accounts.account_number, users.name, users.id from users inner join accounts on accounts.user_id = users.id where users.id =" + id;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(showUserInfo);
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + " " + rs.getInt(4));
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    public static String getAccountNumber(Connection connection, int id) {
        Statement statement = null;
        String accountNumber = "";
        String showUserInfo = "select accounts.account_number from users inner join accounts on accounts.user_id = users.id where users.id =" + id;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(showUserInfo);
            while (rs.next()){
                accountNumber = rs.getString("account_number");
            }

        } catch (SQLException err) {
            err.printStackTrace();
        }
        return accountNumber;
    }
}
