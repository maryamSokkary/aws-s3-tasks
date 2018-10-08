import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataBase {

    private final String url = "jdbc:postgresql://localhost/s3BucketFiles";
    private final String user = "postgres";
    private final String password = "password";
    private static DataBase dataBase = null;

    public static DataBase dataBase(){
        if (dataBase == null) dataBase = new DataBase();
        return dataBase;
    }


    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void insertFile(String fileName, LocalDate date, LocalTime time) throws SQLException {

        Connection conn = connect();
        String query = "insert into data (file_name, upload_date, upload_time) " +
                "values (?, ?, ?);";


        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, fileName);
        stmt.setObject(2, date);
        stmt.setObject(3, time);
        stmt.execute();
        stmt.close();

    }


}
