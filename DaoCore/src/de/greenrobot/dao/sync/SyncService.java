package de.greenrobot.dao.sync;

import android.net.Uri;

import java.util.List;

/**
 * Created by saulhoward on 3/31/14.
 */
public interface SyncService {
    void create(SyncUri uri, String payload, final Callback callback);
    void read(SyncUri uri, final Callback callback);
    void update(SyncUri uri, String payload, final Callback callback);
    void delete(SyncUri uri, final Callback callback);

    public interface Callback {
        void onSuccess(String response);
        void onFail(String errorMessage);
    }

    public interface ObjectListener<T> {
        void onObjectsLoaded(List<T> objects);
    }
}
