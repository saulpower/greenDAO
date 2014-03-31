package de.greenrobot.dao.sync;

import java.util.List;
import java.util.Map;

/**
 * Created by saulhoward on 3/27/14.
 */
public interface GreenSyncDaoBase<T> {

    Map<String, List<GreenSyncBase>> getUpdatedObjects();
    Map<String, List<GreenSyncBase>> getDeletedObjects();
    Map<String, List<GreenSyncBase>> getCreatedObjects();
    void update(T entity);
}
