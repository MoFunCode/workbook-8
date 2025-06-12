import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final String userName = "root";
    private static final String password = "y=hehhe";
    private static final String url = "jdbc:mysql://localhost:3306/sakila";
    private static final Scanner sc = new Scanner(System.in);
    private static BasicDataSource dataSource;

    public static void main(String[] args) {

        setUpDataSourceCon();

        System.out.println("What would you like to do?");
        System.out.println("1. Search actors by last name");
        System.out.println("2. Search movies by actor name");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.println("Please enter a last name of an actor you like:");
                String lastName = sc.nextLine().trim();
                displayActorsByLastName(lastName);
                break;
            case 2:
                System.out.println("Enter first name of actor:");
                String actorFirstName = sc.nextLine().trim();
                System.out.println("Enter last name of actor:");
                String actorLastName = sc.nextLine().trim();
                displayMoviesByActorFirstAndLastName(actorFirstName, actorLastName);
                break;
        }
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

    public static void displayMoviesByActorFirstAndLastName(String actorFirstName, String actorLastName) {
        String sqlQuery = """
                SELECT f.film_id, f.title, f.description, f.release_year
                FROM film f
                INNER JOIN film_actor fa ON f.film_id = fa.film_id
                INNER JOIN actor a ON fa.actor_id = a.actor_id
                WHERE a.first_name = ? AND a.last_name = ?
                ORDER BY f.title
                """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sqlQuery)) {

            statement.setString(1, actorFirstName);
            statement.setString(2, actorLastName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                do {
                    int filmId = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    int releaseYear = resultSet.getInt("release_year");

                    System.out.println("Film Id: " + filmId);
                    System.out.println("Title: " + title);
                    System.out.println("Description: " + description);
                    System.out.println("Release Year: " + releaseYear);

                } while (resultSet.next());

            } else {
                System.out.println("No movies found for actor: " + actorFirstName + " " + actorLastName);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static int getIntInput() {
        try {
            String input = sc.nextLine().trim();
            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("invalid input: Please enter a number");
            return -1;
        }
    }

}
