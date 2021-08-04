import java.sql.*;
import java.util.Scanner;

public class UserClass {
    public static void main(String[] args) {

        try
        {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/bank", "root", "danaja05");
            addUser(con);

        }

        catch(Exception e)
        {
            System.out.println(e);
        }


    }


    public static void addUser(Connection connection){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your username:");
        String userName = scan.nextLine();

        System.out.println("Enter password:");
        String password = scan.nextLine();

        String insertUser = "INSERT into bank.users (name, password) VALUES(?,?)";
            try(PreparedStatement insertQuery = connection.prepareStatement((insertUser))){
                insertQuery.setString(1, userName);
                insertQuery.setString(2, password);

                insertQuery.executeUpdate();
            }catch (SQLException err){
                err.printStackTrace();
            }
        Integer userId = 0;

            String fetchProductInfoQuery = "Select id from bank.users order by id desc limit 1; ";
            try(Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(fetchProductInfoQuery);
                if(rs.next()) {
                    userId = rs.getInt("id");
                }
            }catch (SQLException err){
                err.printStackTrace();
            }

        String accountNumber =  getAlphaNumericString(21);

        String insertAccount = "INSERT into bank.accounts (account_number, user_id) VALUES(?,?)";
        try(PreparedStatement insertQuery = connection.prepareStatement((insertAccount))){
            insertQuery.setString(1, accountNumber);
            insertQuery.setInt(2, userId);
            insertQuery.executeUpdate();
        }catch (SQLException err){
            err.printStackTrace();
        }

    }

    static String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
