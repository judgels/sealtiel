package org.iatoki.judgels.sealtiel;

import com.google.inject.AbstractModule;
import org.iatoki.judgels.Config;
import org.iatoki.judgels.play.ApplicationConfig;
import org.iatoki.judgels.play.general.GeneralName;
import org.iatoki.judgels.play.general.GeneralVersion;
import org.iatoki.judgels.play.migration.JudgelsDataMigrator;
import org.iatoki.judgels.sealtiel.account.AccountConfigSource;
import org.iatoki.judgels.sealtiel.queue.QueueThreadPool;

public final class SealtielModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(GeneralName.class).to("sealtiel");
        bindConstant().annotatedWith(GeneralVersion.class).to("0.9.0");
        bindConstant().annotatedWith(QueueThreadPool.class).to(10);

        bind(Config.class).annotatedWith(AccountConfigSource.class).toInstance(ApplicationConfig.getInstance());

        bind(JudgelsDataMigrator.class).to(SealtielDataMigrator.class);
    }
}
