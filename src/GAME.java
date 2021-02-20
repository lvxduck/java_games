
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GAME {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
        Connection connection = getJDBCConnectionSQLServer();
        if(connection!=null){
            System.out.println("thanh cong");
            try {
                Statement statement = connection.createStatement();
                String sql = "Insert into playerinfor(name,score) values ('le duc thuan 5',134)";
                statement.executeUpdate(sql);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else {
            System.out.println("that bai");
        }
        System.out.println("fuck you");
    }

    public static Connection getJDBCConnectionSQLServer(){
        final String user = "sa";
        final String password = "1234";
        final String url = "jdbc:sqlserver://localhost:1433;databaseName=gametest2;user="+user+";password="+password;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getJDBCConnectionMySQL(){
        final String url = "jdbc:mysql://localhost:3306/game?autoReconnect=true&useSSL=false";
        final String user = "root";
        final String password = "";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url,user,password);
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("fuck you");
            e.printStackTrace();
        }
        return null;
    }
}
