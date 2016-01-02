package org.iatoki.judgels.sealtiel;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.iatoki.judgels.play.migration.AbstractBaseDataMigrationServiceImpl;
import play.db.jpa.JPA;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

@Singleton
public final class SealtielDataMigrationServiceImpl extends AbstractBaseDataMigrationServiceImpl {

    @Override
    protected void onUpgrade(long databaseVersion, long codeDatabaseVersion) throws SQLException {
        if (databaseVersion < 2) {
            migrateV1toV2();
        }
        if (databaseVersion < 3) {
            migrateV2toV3();
        }
    }

    @Override
    public long getCodeDataVersion() {
        return 3;
    }

    private void migrateV1toV2() throws SQLException {
        SessionImpl session = (SessionImpl) JPA.em().unwrap(Session.class);
        Connection connection = session.getJdbcConnectionAccess().obtainConnection();

        Statement statement = connection.createStatement();

        statement.execute("ALTER TABLE sealtiel_client DROP adminName;");
        statement.execute("ALTER TABLE sealtiel_client DROP adminEmail;");
        statement.execute("ALTER TABLE sealtiel_client DROP lastDownloadTime;");
        statement.execute("ALTER TABLE sealtiel_client DROP totalDownload;");
    }

    private void migrateV2toV3() throws SQLException {
        SessionImpl session = (SessionImpl) JPA.em().unwrap(Session.class);
        Connection connection = session.getJdbcConnectionAccess().obtainConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM sealtiel_client");
        while (resultSet.next()) {
            String ipCreate = resultSet.getString("ipCreate");
            String ipUpdate = resultSet.getString("ipUpdate");
            Long timeCreate = resultSet.getLong("timeCreate");
            Long timeUpdate = resultSet.getLong("timeUpdate");
            String userCreate = resultSet.getString("userCreate");
            String userUpdate = resultSet.getString("userUpdate");
            String jid = resultSet.getString("jid");
            String acquaintancesString = resultSet.getString("acquaintances");
            List<String> acquaintancesList = Arrays.asList(acquaintancesString.split(","));

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sealtiel_client_acquaintances (userCreate, timeCreate, ipCreate, userUpdate, timeUpdate, ipUpdate, clientJid, acquaintance) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, userCreate);
            preparedStatement.setLong(2, timeCreate);
            preparedStatement.setString(3, ipCreate);
            preparedStatement.setString(4, userUpdate);
            preparedStatement.setLong(5, timeUpdate);
            preparedStatement.setString(6, ipUpdate);
            preparedStatement.setString(7, jid);

            for (String acquaintance : acquaintancesList) {
                preparedStatement.setString(8, acquaintance);
                preparedStatement.executeUpdate();
            }

        }

        statement.execute("ALTER TABLE sealtiel_client DROP acquaintances");
    }

}
