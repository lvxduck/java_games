package circleGame;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GameDBUtils {
    public static void main(String[] args) {
        GameDBUtils.getInstance();
    }

    private static GameDBUtils instance;

    private static Connection connection;

    private String id="1234";

    public static GameDBUtils getInstance(){
        if(instance==null){
            instance = new GameDBUtils();
        }
        return instance;
    }

    public GameDBUtils(){
        connectToDB();
     //   readDB("abc");
    }
    public void writeDB(int score){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String time = dateFormat.format(date);
        try{
            System.out.println("Writing database.....");
            Statement statement = null;
            statement = connection.createStatement();
            String sql = "Insert into history(id,time,score) values ('"+id+"','"+time+"','"+score+"')";
            statement.executeUpdate(sql);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[][] readDB()  {
        System.out.println("read DB");
        Statement statement = null;
        String[][] dataTemp = new String[10][2];
        int n=0;

        try {
            statement = connection.createStatement();
            String sql = "select top 3 * from history where id='"+id+"' order by score desc";
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Kich thuoc"+resultSet.getRow());
            while (resultSet.next()){
                dataTemp[n][0]=resultSet.getString("time");
                dataTemp[n][1]=resultSet.getString("score");
                n+=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[][] data = new String[n][3];
        for(int i=0;i<n;i++){
            data[i][0]=dataTemp[i][0];
            data[i][1]=dataTemp[i][1];
        }

        return data;
    }

    private void connectToDB(){
        System.out.println("Chuan bi ket noi");
        connection = getJDBCConnectionSQLServer("GAME");
        if(connection!=null){
            System.out.println("thanh cong");
        }else {
            System.out.println("that bai");
        }
    }

    private Connection getJDBCConnectionSQLServer(String databaseName){
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
