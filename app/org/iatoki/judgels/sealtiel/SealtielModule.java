package org.iatoki.judgels.sealtiel;

import com.google.inject.AbstractModule;
import org.iatoki.judgels.Config;
import org.iatoki.judgels.play.ApplicationConfig;
import org.iatoki.judgels.play.general.GeneralName;
import org.iatoki.judgels.play.general.GeneralVersion;
import org.iatoki.judgels.play.migration.BaseDataMigrationService;
import org.iatoki.judgels.sealtiel.account.AccountConfigSource;
import org.iatoki.judgels.sealtiel.queue.QueueThreadPool;

public final class SealtielModule extends AbstractModule {

    @Override
    protected void configure() {
        org.iatoki.judgels.sealtiel.BuildInfo$ buildInfo = org.iatoki.judgels.sealtiel.BuildInfo$.MODULE$;

        bindConstant().annotatedWith(GeneralName.class).to(buildInfo.name());
        bindConstant().annotatedWith(GeneralVersion.class).to(buildInfo.version());
        bindConstant().annotatedWith(QueueThreadPool.class).to(10);

        bind(Config.class).annotatedWith(AccountConfigSource.class).toInstance(ApplicationConfig.getInstance());

        bind(BaseDataMigrationService.class).to(SealtielDataMigrationServiceImpl.class);
    }
}
