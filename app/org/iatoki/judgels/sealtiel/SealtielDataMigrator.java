package org.iatoki.judgels.sealtiel;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.iatoki.judgels.play.migration.AbstractJudgelsDataMigrator;
import org.iatoki.judgels.play.migration.DataMigrationEntityManager;
import org.iatoki.judgels.play.migration.DataVersionDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

@Singleton
public final class SealtielDataMigrator extends AbstractJudgelsDataMigrator {

    private final EntityManager entityManager;

    @Inject
    public SealtielDataMigrator(DataVersionDao dataVersionDao) {
        super(dataVersionDao);
        this.entityManager = DataMigrationEntityManager.createEntityManager();
    }

    @Override
    protected void migrate(long currentDataVersion) throws SQLException {
        if (currentDataVersion < 2) {
            migrateV1toV2();
        }
        if (currentDataVersion < 3) {
            migrateV2toV3();
        }
    }

    @Override
    public long getLatestDataVersion() {
        return 4;
    }

    private void migrateV3toV4() throws SQLException {
        SessionImpl session = (SessionImpl) entityManager.unwrap(Session.class);
        Connection connection = session.getJdbcConnectionAccess().obtainConnection();

        Statement statement = connection.createStatement();

        statement.execute("ALTER TABLE sealtiel_client_acquaintance CHANGE acquaintance acquaintanceJid VARCHAR(255);");
    }

    private void migrateV2toV3() throws SQLException {
        SessionImpl session = (SessionImpl) entityManager.unwrap(Session.class);
        Connection connection = session.getJdbcConnectionAccess().obtainConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM sealtiel_client;");
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

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sealtiel_client_acquaintance (userCreate, timeCreate, ipCreate, userUpdate, timeUpdate, ipUpdate, clientJid, acquaintance) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
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

        statement.execute("ALTER TABLE sealtiel_client DROP acquaintances;");
    }

    private void migrateV1toV2() throws SQLException {
        SessionImpl session = (SessionImpl) entityManager.unwrap(Session.class);
        Connection connection = session.getJdbcConnectionAccess().obtainConnection();

        Statement statement = connection.createStatement();

        statement.execute("ALTER TABLE sealtiel_client DROP adminName;");
        statement.execute("ALTER TABLE sealtiel_client DROP adminEmail;");
        statement.execute("ALTER TABLE sealtiel_client DROP lastDownloadTime;");
        statement.execute("ALTER TABLE sealtiel_client DROP totalDownload;");
    }
}
