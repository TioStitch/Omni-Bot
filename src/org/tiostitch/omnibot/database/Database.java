package org.tiostitch.omnibot.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.OmniLogger;
import org.tiostitch.omnibot.abstraction.DataRequestImpl;
import org.tiostitch.omnibot.configuration.types.DatabaseConfiguration;
import org.tiostitch.omnibot.database.types.SQLRequest;
import org.tiostitch.omnibot.abstraction.Userdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

@Getter @Setter
@RequiredArgsConstructor
public class Database {

    private final OmniCore omniCore;
    private DataRequestImpl request;
    private final HashMap<String, Userdata> loaded_users = new HashMap<>();

    public void init() {

        final Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS omnibot_userdata (\n" +
                        "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "    name CHAR(64) NOT NULL,\n" +
                        "    discId CHAR(64) NOT NULL,\n" +
                        "    extra_data TEXT,\n" +
                        "    bag TEXT\n" +
                        ");")) {

            statement.executeUpdate();

        } catch (SQLException e) {
            return;
        }
    }

    public Connection getConnection() {
        try {

            final DatabaseConfiguration dbCfg = omniCore.getConfig().getDatabaseConfiguration();
            final String dataType = dbCfg.getDATA_TYPE();

            if (dataType.contains("SQL")) {
                request = new SQLRequest(omniCore);
            }

            if (dataType.equalsIgnoreCase("SQLite")) {
                return getSQLite();
            } else {
                return getMySQL();
            }

        } catch (SQLException e) {
            return null;
        }
    }

    public Connection getSQLite() throws SQLException {
        try {
        Class.forName("com.mysql.jdbc.Driver");

        OmniLogger.success("Carregando Banco de Dados (SQLITE)!");

        final DatabaseConfiguration.SQLITEConfiguration dbCfg = omniCore.getConfig()
                .getDatabaseConfiguration()
                .getSQLITE_CONFIG();

        final String url = "jdbc:sqlite:" + dbCfg.getFILE_NAME() + ".db";
        return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getMySQL() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            OmniLogger.success("Carregando Banco de Dados (MYSQL)!");

            final DatabaseConfiguration.MYSQLConfiguration dbCfg = omniCore.getConfig()
                    .getDatabaseConfiguration()
                    .getMYSQL_CONFIG();

            final String host = dbCfg.getMYSQL_HOST();
            final String database = dbCfg.getMYSQL_DATABASE();
            final String username = dbCfg.getMYSQL_USERNAME();
            final String password = dbCfg.getMYSQL_PASSWORD();

            String url = "jdbc:mysql://" + host + "/" + database;

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
