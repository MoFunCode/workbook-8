import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final String userName = "root";
    private static final String password = "y=hehhe";
    private static final String url = "jdbc:mysql://localhost:3306/Sakila";
    private static BasicDataSource dataSource;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        setUpDataSourceCon();

        System.out.println("Please enter a last name of an actor you like:");
        String lastName = sc.nextLine().trim();
        displayActorsByLastName(lastName);
    }

    public static void setUpDataSourceCon() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
    }

    public static void displayActorsByLastName(String lastName) {
        String sqlQuery = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ? ORDER BY first_name";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sqlQuery)) {

            statement.setString(1, lastName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Actors with last name '" + lastName + "':");
                System.out.println("ID | First Name | Last Name");

                do {
                    int actorID = resultSet.getInt("actor_id");
                    String firstName = resultSet.getString("first_name");
                    String actorLastName = resultSet.getString("last_name");
                    System.out.println(actorID + " | " + firstName + " | " + actorLastName);
                } while (resultSet.next());

            } else {
                System.out.println("No actors found with last name: " + lastName);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public int getIntInput() {
        try {
            String input = sc.nextLine().trim();
            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("invalid input: Please enter a number");
            return -1;
        }
    }
}