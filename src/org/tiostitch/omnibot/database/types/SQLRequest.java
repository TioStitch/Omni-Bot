package org.tiostitch.omnibot.database.types;

import lombok.RequiredArgsConstructor;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.OmniLogger;
import org.tiostitch.omnibot.abstraction.DataRequestImpl;
import org.tiostitch.omnibot.abstraction.UserExtradata;
import org.tiostitch.omnibot.abstraction.Userdata;
import org.tiostitch.omnibot.utilities.Serializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class SQLRequest
implements DataRequestImpl {

    private final OmniCore omniCore;

    @Override
    public Userdata loadUserdata(String key, long discId) {

        if (existUser(key)) {
            return getUserdata(key);
        }

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO omnibot_userdata (name, discId, extra_data, bag)" +
                        " VALUES (?, ?, ?, ?)")) {

            statement.setString(1, key);
            statement.setLong(2, discId);
            statement.setString(3, Serializer.extraDataToJson(new UserExtradata()));
            statement.setString(4, "{}");

            statement.executeUpdate();

            connection.close();

            return new Userdata().getEmptyUserdata(key);
        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'loadUserdata()'. Abra um ticket em nosso suporte!");
        }

        return null;
    }

    @Override
    public Userdata updateUserdata(String key, Userdata userdata) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE omnibot_userdata SET name = ?, extra_data = ?, bag = ?" +
                        " WHERE name = ?")) {

            statement.setString(1, userdata.getName());
            statement.setString(2, Serializer.extraDataToJson(userdata.getExtradata()));
            statement.setString(3, Serializer.bagToJson(userdata.getBag()));

            statement.setString(4, key);

            statement.executeUpdate();

            connection.close();

            return userdata;
        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'updateUserdata()'. Abra um ticket em nosso suporte! \n\nExceção:" + exception.getMessage());
        }

        return null;
    }
    @Override
    public Userdata updateUserdataById(long key, Userdata userdata) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE omnibot_userdata SET name = ?, extra_data = ?, bag = ?" +
                        " WHERE discId = ?")) {

            statement.setString(1, userdata.getName());
            statement.setString(2, Serializer.extraDataToJson(userdata.getExtradata()));
            statement.setString(3, Serializer.bagToJson(userdata.getBag()));

            statement.setLong(4, key);

            statement.executeUpdate();

            connection.close();

            return userdata;
        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'updateUserdataById()'. Abra um ticket em nosso suporte!");
        }

        return null;
    }


    @Override
    public void deleteUserdata(String key) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM omnibot_userdata WHERE name = ?")) {

            statement.setString(1, key);
            statement.executeUpdate();
            connection.close();

        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'deleteUserdata()'. Abra um ticket em nosso suporte!");
        }
    }
    @Override
    public void deleteUserdataById(long key) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM omnibot_userdata WHERE discId = ?")) {

            statement.setLong(1, key);
            statement.executeUpdate();
            connection.close();

        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'deleteUserdataById()'. Abra um ticket em nosso suporte!");
        }
    }


    @Override
    public boolean existUser(String key) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM omnibot_userdata WHERE name = ?")) {

            statement.setString(1, key);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

            connection.close();

        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'existUser()'. Abra um ticket em nosso suporte!");
        }

        return false;
    }
    @Override
    public boolean existUserById(long key) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM omnibot_userdata WHERE discId = ?")) {

            statement.setLong(1, key);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

            connection.close();

        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'existUserById()'. Abra um ticket em nosso suporte!");
        }

        return false;
    }


    @Override
    public Userdata getUserdata(String key) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM omnibot_userdata WHERE name = ?")) {

            statement.setString(1, key);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                final Userdata userdata = new Userdata();

                userdata.setName(resultSet.getString("name"));
                userdata.setExtradata(Serializer.extraDataFromJson(resultSet.getString("extra_data")));
                userdata.setBag(Serializer.bagFromJson(resultSet.getString("bag")));

                return userdata;
            }

            connection.close();

        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'getUserdata()'. Abra um ticket em nosso suporte!");
        }

        return null;
    }
    @Override
    public Userdata getUserdataById(long key) {

        final Connection connection = omniCore.getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM omnibot_userdata WHERE name = ?")) {

            statement.setLong(1, key);

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                final Userdata userdata = new Userdata();

                userdata.setName(resultSet.getString("name"));
                userdata.setExtradata(Serializer.extraDataFromJson(resultSet.getString("extra_data")));
                userdata.setBag(Serializer.bagFromJson(resultSet.getString("bag")));

                return userdata;
            }

            connection.close();

        } catch (SQLException exception) {
            OmniLogger.critical("Ocorreu um erro na função 'getUserdataById()'. Abra um ticket em nosso suporte!");
        }

        return null;
    }
}
