package main.java;

import java.sql.*;
import java.util.Scanner;

public class UserClass {
    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/bank", "root", "Oxford1984");
            addUser(con);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addUser(Connection connection) {

        Scanner scan = new Scanner(System.in);
        boolean correct = false;
        String userName = "";
        String password = "";
        int balance = 0;
        while (!correct) {
            System.out.println("Enter your username: ");
            userName = scan.nextLine();

            String fetchUserQuery = "Select id from bank.users where name = '" + userName + "';";
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(fetchUserQuery);
                if (rs.next()) {
                    System.out.println("Please try another username, this username is already used");
                    continue;
                }
            } catch (SQLException err) {
                err.printStackTrace();
            }
            if (userName.isEmpty() || userName.length() > 25) {
                System.out.println("Username is not valid, must be more than 0 and up to 25 characters");
                continue;
            } else {
                while (!correct) {
                    System.out.println("Enter password: ");
                    password = scan.nextLine();
                    if (password.isEmpty() || password.length() > 10) {
                        System.out.println("Password is not valid, must be more than 0 and up to 10 characters");
                        continue;
                    } else {
                        System.out.println("New account is created");
                        correct = true;
                        continue;
                    }
                }
                System.out.println("Top up your balance: ");
                balance = scan.nextInt();
                while (balance <= 0) {
                    System.out.println("That number is invalid!");

                    System.out.println("Please enter the amount > 0 euro: ");
                    balance = scan.nextInt();
                }
                System.out.println(balance + " euro are credited to your balance");
            }
        }

        String insertUser = "INSERT INTO bank.users (name, password) VALUES(?,?)";
        try (PreparedStatement insertQuery = connection.prepareStatement((insertUser))) {
            insertQuery.setString(1, userName);
            insertQuery.setString(2, password);

            insertQuery.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }

        int userId = 0;
        String fetchUserIdQuery = "SELECT id FROM bank.users ORDER BY id desc limit 1; ";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(fetchUserIdQuery);
            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        String accountNumber = getAlphaNumericString(21);

        String insertAccount = "INSERT INTO bank.accounts (account_number, user_id, balance) VALUES(?,?,?)";
        try (PreparedStatement insertQuery = connection.prepareStatement((insertAccount))) {
            insertQuery.setString(1, accountNumber);
            insertQuery.setInt(2, userId);
            insertQuery.setInt(3, balance);
            insertQuery.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
