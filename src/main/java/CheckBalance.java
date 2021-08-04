import java.sql.*;

public class CheckBalance {

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/bank", "root", "danaja05");
            System.out.println(checkBalance(con, 13));
            madeTransaction(con, 13,14, 50);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void madeTransaction(Connection connection, int firstUserId, int secondUserId, int money){

        if(money >  checkBalance(connection, firstUserId)){
            System.out.println("There is not enough money on your bank account!");
        } else {
            CallableStatement cstmt = null;
            try {
                String SQL = "{call transaction(?,?,?)}";
                cstmt = connection.prepareCall(SQL);
                cstmt.setInt(1, firstUserId);
                cstmt.setInt(2, secondUserId);
                cstmt.setInt(3, money);
                cstmt.execute();
                System.out.println("Transaction made!");
                System.out.println("Your balance is " + checkBalance(connection, 13) + " euros");
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    public static int checkBalance(Connection connection, int id) {
        Statement statement = null;
        Integer accountBalance = 0;
        String showBalance = "select accounts.balance from users inner join accounts on accounts.user_id = users.id where users.id =" + id;
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
}
