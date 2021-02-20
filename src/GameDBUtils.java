
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GameDBUtils {

    public static void main(String[] args) {
        GameDBUtils.getInstance().connectToDB();
    }

    private static GameDBUtils instance;

    public static GameDBUtils getInstance(){
        if(instance==null){
            instance = new GameDBUtils();
        }
        return instance;
    }

    public void connectToDB(){
        System.out.println("Chuan bi ket noi");
        Connection connection = getJDBCConnectionSQLServer("gametest2");
        if(connection!=null){
            System.out.println("thanh cong");
        }else {
            System.out.println("that bai");
        }
    }

    public static Connection getJDBCConnectionSQLServer(String databaseName){
        final String user = "sa";
        final String password = "1234";
        final String url = "jdbc:sqlserver://localhost:1433;databaseName="+databaseName+";user="+user+";password="+password;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
