package org.iatoki.judgels.sealtiel.config;

import org.iatoki.judgels.commons.config.JudgelsAbstractModule;
import org.iatoki.judgels.sealtiel.SealtielProperties;
import org.iatoki.judgels.sealtiel.services.QueueService;
import org.iatoki.judgels.sealtiel.services.impls.RabbitmqImpl;

public class SealtielModule extends JudgelsAbstractModule {

    @Override
    protected void manualBinding() {
        bind(QueueService.class).annotatedWith(RabbitMQService.class).toInstance(queueService());
        bindConstant().annotatedWith(QueueThreadPool.class).to(10);
    }

    @Override
    protected String getDaosImplPackage() {
        return "org.iatoki.judgels.sealtiel.models.daos.impls";
    }

    @Override
    protected String getServicesImplPackage() {
        return "org.iatoki.judgels.sealtiel.services.impls";
    }

    private SealtielProperties sealtielProperties() {
        return SealtielProperties.getInstance();
    }

    private QueueService queueService() {
        RabbitmqImpl.buildInstance(sealtielProperties().getRabbitmqHost(), sealtielProperties().getRabbitmqPort(), sealtielProperties().getRabbitmqUsername(), sealtielProperties().getRabbitmqPassword(), sealtielProperties().getRabbitmqVirtualHost());
        return RabbitmqImpl.getInstance();
    }
}
