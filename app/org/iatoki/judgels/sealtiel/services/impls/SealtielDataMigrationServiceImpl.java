package org.iatoki.judgels.sealtiel.services.impls;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.iatoki.judgels.play.services.impls.AbstractBaseDataMigrationServiceImpl;
import play.db.jpa.JPA;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class SealtielDataMigrationServiceImpl extends AbstractBaseDataMigrationServiceImpl {

    @Override
    protected void onUpgrade(long databaseVersion, long codeDatabaseVersion) throws SQLException {
        if (databaseVersion < 2) {
            migrateV1toV2();
        }
    }

    @Override
    public long getCodeDataVersion() {
        return 2;
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
}
