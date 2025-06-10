import java.sql.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String userName = "root";
        String password = "yearup24";
        String url = "jdbc:mysql://localhost:3306/sakila";

        String queryTest = "SELECT ProductID, ProductName, UnitPrice, UnitsINStock FROM northwind.Products";

        try {

            Connection connectionToNordwind = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connectionToNordwind.prepareStatement(queryTest);
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
            System.out.println(e.getMessage());
        }
    }
}

