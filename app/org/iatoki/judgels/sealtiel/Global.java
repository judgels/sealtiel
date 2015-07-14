package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.sealtiel.services.impls.SealtielDataMigrationServiceImpl;
import play.Application;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

public final class Global extends org.iatoki.judgels.play.Global {

    public Global() {
        super(new SealtielDataMigrationServiceImpl());
    }

    @Override
    public void onStart(Application application) {
        super.onStart(application);
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader requestHeader) {
        return super.onHandlerNotFound(requestHeader);
    }
}