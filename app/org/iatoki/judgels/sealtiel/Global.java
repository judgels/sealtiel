package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.sealtiel.controllers.ApplicationController;
import org.iatoki.judgels.sealtiel.controllers.ClientController;
import org.iatoki.judgels.sealtiel.controllers.MessageController;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Global extends org.iatoki.judgels.commons.Global {

    private Map<Class, Controller> cache;

    @Override
    public void onStart(Application app) {
        cache = new HashMap<>();

        boolean result = false;

        File file = new File(Play.application().configuration().getString("dir.conf"));
        if (!file.exists()) {
            file.mkdirs();
            file.setWritable(true);
        }
        result = file.canWrite();

        file = new File(Play.application().configuration().getString("dir.sealtiel"));
        if (!file.exists()) {
            file.mkdirs();
            file.setWritable(true);
        }

        if (!result) {
            System.out.println("Sealtiel can't start, please createMessage directory \""+Play.application().configuration().getString("dir.judgels")+"\" and make sure it is writable.");
            System.exit(0);
        }

        super.onStart(app);
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        if (!cache.containsKey(controllerClass)) {
            if (controllerClass.equals(ClientController.class)) {
                ClientDao clientDao = new ClientHibernateDao();
                ClientService clientService = new ClientServiceImpl(clientDao);
                ClientController clientController = new ClientController(clientService);

                cache.put(ClientController.class, clientController);
            } else if (controllerClass.equals(MessageController.class)) {
                MessageDao messageDao = new MessageHibernateDao();
                MessageService messageService = new MessageServiceImpl(messageDao);
                ClientDao clientDao = new ClientHibernateDao();
                ClientService clientService = new ClientServiceImpl(clientDao);
                MessageController messageController = new MessageController(messageService, clientService, 10);

                cache.put(MessageController.class, messageController);
            } else if (controllerClass.equals(ApplicationController.class)) {
                cache.put(ApplicationController.class, new ApplicationController());
            }
        }
        return controllerClass.cast(cache.get(controllerClass));
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader requestHeader) {
        System.out.println(requestHeader.path());
        return super.onHandlerNotFound(requestHeader);
    }
}