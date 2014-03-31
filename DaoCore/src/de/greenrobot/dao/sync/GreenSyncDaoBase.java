package de.greenrobot.dao.sync;

import java.util.Map;

/**
 * Created by saulhoward on 3/27/14.
 */
public interface GreenSyncDaoBase {

    Map getUpdatedObjects();
    Map getDeletedObjects();
    Map getCreatedObjects();
}
