package de.greenrobot.dao.sync;

import java.util.List;

/**
 * Created by saulhoward on 3/31/14.
 */
public interface SyncService {
    void create(String className, String payload, final Callback callback);
    void read(String className, String id, final Callback callback);
    void update(String className, String payload, final Callback callback);
    void delete(String className, String id, final Callback callback);

    public interface Callback {
        void onSuccess(String response);
        void onFail(String errorMessage);
    }

    public interface ObjectListener<T> {
        void onObjectsLoaded(List<T> objects);
    }
}
