import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String userName = "root";
    private static final String password = "yearup24";
    private static final String url = "jdbc:mysql://localhost:3306/northwind";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        int userChoice; // Moved declaration outside do-while loop

        do {
            System.out.println("What would you like to do? ");
            System.out.println("1. Display all products");
            System.out.println("2. Display all customers");
            System.out.println("3. Display all categories");
            System.out.println("0. Exit");
            System.out.println("select an option: ");

            //int userChoice; - Only accibel within do Block, nothing else.
            // Notes: When you declare a variable inside a block of code (like inside the do { } block),
            // that variable only exists within that block

            userChoice = getIntInput();

            switch (userChoice) {
                case 1:
                    displayAllProducts();
                    break;
                case 2:
                    displayAllCustomers();
                    break;
                case 3:
                    displayAllCategories();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (userChoice != 0);
        sc.close();
    }

    public static void displayAllProducts() {
        String produtTableQuery = "SELECT ProductID, ProductName, UnitPrice, UnitsINStock FROM northwind.Products";

        try {

            Connection connectionToProductTable = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connectionToProductTable.prepareStatement(produtTableQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            //Statement statement = connectionToNordwind.createStatement();
            //ResultSet resultSet = statement.executeQuery(queryTest);

            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                double unitPrice = resultSet.getDouble("UnitPrice");
                int unitsInStock = resultSet.getInt("UnitsInStock");

                System.out.println("Product Id: " + productID);
                System.out.println("Name: " + productName);
                System.out.println("Price: " + unitPrice);
                System.out.println("Stock: " + unitsInStock);
                // String firstName = resultSet.getString("ProductName");
                // System.out.println(firstName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void displayAllCustomers() {

        String customerTableQuery =
                "SELECT ContactName, CompanyName, City, Country, Phone FROM northwind.Customers ORDER BY Country";
        try (Connection connectionToCustomerTable = DriverManager.getConnection(url, userName, password)) {
            //Connection connectionToCustomerTable = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connectionToCustomerTable.prepareStatement(customerTableQuery);
            ResultSet resultSetForCustomerTable = preparedStatement.executeQuery();

            while (resultSetForCustomerTable.next()) {
                String contactName = resultSetForCustomerTable.getString("ContactName");
                String companyName = resultSetForCustomerTable.getString("CompanyName");
                String city = resultSetForCustomerTable.getString("City");
                String country = resultSetForCustomerTable.getString("Country");
                String phone = resultSetForCustomerTable.getString("Phone");

                System.out.println("Contact: " + contactName);
                System.out.println("Company: " + companyName);
                System.out.println("City: " + city);
                System.out.println("Country: " + country);
                System.out.println("Phone: " + phone);


            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void displayAllCategories() {

        String categoriesTableQuery = "SELECT * FROM Categories ORDER BY CategoryID";

        try {
            Connection connectionToCategoriesTable = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connectionToCategoriesTable.prepareStatement(categoriesTableQuery);
            ResultSet resultSetForCategoriesable = preparedStatement.executeQuery();

            System.out.println("Available Categories: ");
            while (resultSetForCategoriesable.next()) {
                String CategoryID = resultSetForCategoriesable.getString("CategoryID");
                String CategoryName = resultSetForCategoriesable.getString("CategoryName");

                System.out.println("CategoryID: " + CategoryID);
                System.out.println("CategoryName: " + CategoryName);
            }

            displayProductsByCategories();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

    }

    public static void displayProductsByCategories() {
        System.out.println("Please enter a categoryId to see all products:");
        int userChoice = getIntInput();


        String productsByCategoriesQuery =
                "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM northwind.Products WHERE CategoryID = ?";

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement preparedStatement = connection.prepareStatement(productsByCategoriesQuery)) {

            preparedStatement.setInt(1, userChoice);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Products in Category " + userChoice + ":");
            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                double unitPrice = resultSet.getDouble("UnitPrice");
                int unitsInStock = resultSet.getInt("UnitsInStock");

                System.out.println("Product ID: " + productID);
                System.out.println("Product Name: " + productName);
                System.out.println("Unit Price: " + unitPrice);
                System.out.println("Units in Stock: " + unitsInStock);
            }

            if (!hasResults) {
                System.out.println("No products found for category ID: " + userChoice);
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
            System.out.println("Invalid input: Please enter a number");
            return -1;

        }
    }
}


