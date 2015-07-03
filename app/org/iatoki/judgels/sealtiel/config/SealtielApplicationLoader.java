package org.iatoki.judgels.sealtiel.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.iatoki.judgels.commons.JudgelsProperties;
import org.iatoki.judgels.sealtiel.SealtielProperties;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

public final class SealtielApplicationLoader extends GuiceApplicationLoader {

    @Override
    public GuiceApplicationBuilder builder(Context context) {
        org.iatoki.judgels.sealtiel.BuildInfo$ buildInfo = org.iatoki.judgels.sealtiel.BuildInfo$.MODULE$;
        JudgelsProperties.buildInstance(buildInfo.name(), buildInfo.version(), ConfigFactory.load());

        Config config = ConfigFactory.load();
        SealtielProperties.buildInstance(config);

        return super.builder(context);
    }
}
