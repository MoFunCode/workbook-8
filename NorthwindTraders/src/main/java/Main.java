import java.sql.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException{

        Class.forName("com.mysql.cj.jdbc.Driver");
        String userName = "root";
        String password = "yearup24";
        String url = "jdbc:mysql://localhost:3306/sakila";
        String queryTest = "SELECT * FROM northwind.Products";

        try {

            Connection connectionToNordwind = DriverManager.getConnection(url, userName, password);
            Statement statement = connectionToNordwind.createStatement();
            ResultSet resultSet = statement.executeQuery(queryTest);


            while (resultSet.next()) {

                String firstName = resultSet.getString("ProductName");
                System.out.println(firstName);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

