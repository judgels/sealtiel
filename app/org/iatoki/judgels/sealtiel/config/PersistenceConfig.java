package org.iatoki.judgels.sealtiel.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.iatoki.judgels.commons.JudgelsProperties;
import org.iatoki.judgels.sealtiel.SealtielProperties;
import org.iatoki.judgels.sealtiel.services.QueueService;
import org.iatoki.judgels.sealtiel.services.impls.RabbitmqImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "org.iatoki.judgels.sealtiel.models.daos",
        "org.iatoki.judgels.sealtiel.services",
})
public class PersistenceConfig {

    @Bean
    public JudgelsProperties judgelsProperties() {
        org.iatoki.judgels.sealtiel.BuildInfo$ buildInfo = org.iatoki.judgels.sealtiel.BuildInfo$.MODULE$;
        JudgelsProperties.buildInstance(buildInfo.name(), buildInfo.version(), ConfigFactory.load());
        return JudgelsProperties.getInstance();
    }

    @Bean
    public SealtielProperties sealtielProperties() {
        Config config = ConfigFactory.load();
        SealtielProperties.buildInstance(config);
        return SealtielProperties.getInstance();
    }

    @Bean
    public QueueService queueService() {
        RabbitmqImpl.buildInstance(sealtielProperties().getRabbitmqHost(), sealtielProperties().getRabbitmqPort(), sealtielProperties().getRabbitmqUsername(), sealtielProperties().getRabbitmqPassword(), sealtielProperties().getRabbitmqVirtualHost());
        return RabbitmqImpl.getInstance();
    }

    @Bean
    public int threadPool() {
        return 10;
    }
}
