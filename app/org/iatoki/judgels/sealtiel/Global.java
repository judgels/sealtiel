package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.play.AbstractGlobal;
import org.iatoki.judgels.play.services.BaseDataMigrationService;
import org.iatoki.judgels.sealtiel.services.impls.SealtielDataMigrationServiceImpl;
import play.Application;

public final class Global extends AbstractGlobal {

    @Override
    public void onStart(Application application) {
        super.onStart(application);
    }

    @Override
    protected BaseDataMigrationService getDataMigrationService() {
        return new SealtielDataMigrationServiceImpl();
    }
}