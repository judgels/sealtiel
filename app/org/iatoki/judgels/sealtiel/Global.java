package org.iatoki.judgels.sealtiel;

import com.google.common.collect.ImmutableMap;
import org.iatoki.judgels.commons.JudgelsProperties;
import org.iatoki.judgels.sealtiel.controllers.ApplicationController;
import org.iatoki.judgels.sealtiel.controllers.ClientController;
import org.iatoki.judgels.sealtiel.controllers.apis.MessageAPIController;
import org.iatoki.judgels.sealtiel.models.dao.hibernate.ClientHibernateDao;
import org.iatoki.judgels.sealtiel.models.dao.hibernate.MessageHibernateDao;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.ClientDao;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.MessageDao;
import play.Application;
import play.Play;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Map;

public class Global extends org.iatoki.judgels.commons.Global {
    private static final String CONF_LOCATION = "conf/application.conf";

    private ClientDao clientDao;
    private MessageDao messageDao;

    private SealtielProperties sealtielProps;

    private ClientService clientService;
    private MessageService messageService;

    private Map<Class<?>, Controller> controllersCache;

    @Override
    public void onStart(Application application) {
        buildDaos();
        buildProperties();
        buildServices();
        buildControllers();
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        @SuppressWarnings("unchecked")
        A controller = (A) controllersCache.get(controllerClass);

        return controller;
    }

    private void buildDaos() {
        clientDao = new ClientHibernateDao();
        messageDao = new MessageHibernateDao();
    }

    private void buildProperties() {
        org.iatoki.judgels.sealtiel.BuildInfo$ buildInfo = org.iatoki.judgels.sealtiel.BuildInfo$.MODULE$;
        JudgelsProperties.buildInstance(buildInfo.name(), buildInfo.version(), Play.application().configuration(), CONF_LOCATION);

        SealtielProperties.buildInstance(Play.application().configuration(), CONF_LOCATION);
        sealtielProps = SealtielProperties.getInstance();
    }

    private void buildServices() {
        clientService = new ClientServiceImpl(clientDao);
        messageService = new MessageServiceImpl(messageDao);
    }

    private void buildControllers() {
        controllersCache = ImmutableMap.<Class<?>, Controller> builder()
                .put(ApplicationController.class, new ApplicationController())
                .put(ClientController.class, new ClientController(clientService))
                .put(MessageAPIController.class, new MessageAPIController(messageService, clientService, 10))
                .build();
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader requestHeader) {
        System.out.println(requestHeader.path());
        return super.onHandlerNotFound(requestHeader);
    }
}