import java.sql.*;

public class CheckBalance {

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/Bank", "root", "Oxford1984");
            System.out.println(checkBalance(con, 2));
            madeTransaction(con, 2,3, 20);
            getInfo(con, 2);
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void madeTransaction(Connection connection, int firstUserId, int secondUserId, int money){

        if(money >  checkBalance(connection, firstUserId)){
            System.out.println("There is not enough money on your bank account!");
        } else {
            try(Statement statement = connection.createStatement()){
                connection.setAutoCommit(false);
                statement.execute("UPDATE accounts inner join users on users.id = accounts.user_id SET accounts.balance = accounts.balance - " + money + " where users.id =" + firstUserId + ";");
                statement.execute("UPDATE accounts inner join users on users.id = accounts.user_id SET accounts.balance = accounts.balance + " + money + " where users.id =" + secondUserId + ";");
                connection.commit();
            }catch (SQLException err){
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
}
