import java.sql.*;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/gamecenter";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static String getGifPath() {
        return getPathFromDatabase("SELECT file_path FROM gif where gif_id = 1");
    }

    public static String getBgmPath() {
        return getPathFromDatabase("SELECT file_path FROM bgm where bgm_id = 1");
    }

    private static String getPathFromDatabase(String query) {
        String path = null;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                path = rs.getString("file_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return path;
    }
}
