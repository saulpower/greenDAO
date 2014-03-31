package de.greenrobot.dao.sync;

/**
 * Created by saulhoward on 3/31/14.
 */
public interface SyncService {
    void create(String className, String payload, final Callback callback);
    void read(String className, String id, final Callback callback);
    void update(String className, String payload, final Callback callback);
    void delete(String className, String id, final Callback callback);

    public interface Callback {
        void onSuccess(String externalId);
        void onFail(String errorMessage);
    }
}
