package de.greenrobot.dao.sync;

import android.util.Log;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.DaoEnum;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saulhoward on 3/21/14.
 */
public class GreenSync {

    private static Map<String, Type> sListTypeTokensMap = new HashMap<String, Type>();
    private static Map<String, Class<?>> sClassMap = new HashMap<String, Class<?>>();

    public static final String CREATED = "Created";
    public static final String UPDATED = "Updated";
    public static final String DELETED = "Deleted";

    private Gson mGson;
    private GreenSyncDaoBase mGreenSyncDaoBase;
    private AbstractDaoSession mSession;
    private SyncService mSyncService;

    private final Map<Class<? extends DaoEnum>, EnumAdapter<? extends DaoEnum>> enumAdapters =
            new LinkedHashMap<Class<? extends DaoEnum>, EnumAdapter<? extends DaoEnum>>();

    public static void registerListTypeToken(String key, Type type) {
        sListTypeTokensMap.put(key, type);
    }

    public static void registerTypeToken(String key, Class<?> clazz) {
        sClassMap.put(key, clazz);
    }

    public static Type getTypeToken(String key) {
        return sListTypeTokensMap.get(key);
    }

    public GreenSync(AbstractDaoSession session, SyncService syncService) {

        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        builder.registerTypeAdapterFactory(new EntityTypeAdapterFactory(this));
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        mGson = builder.create();
        mSession = session;
        mSyncService = syncService;

        for (AbstractDao dao : mSession.getDaos().values()) {
            if (dao instanceof GreenSyncDaoBase) {
                mGreenSyncDaoBase = (GreenSyncDaoBase) dao;
            }
        }

        if (mGreenSyncDaoBase == null) throw new ExceptionInInitializerError("Missing sync base");
    }

    /**
     * Returns an enum adapter for {@code enumClass}.
     */
    @SuppressWarnings("unchecked")
    synchronized <E extends DaoEnum> EnumAdapter<E> enumAdapter(Class<E> enumClass) {
        EnumAdapter<E> adapter = (EnumAdapter<E>) enumAdapters.get(enumClass);
        if (adapter == null) {
            adapter = new EnumAdapter<E>(enumClass);
            enumAdapters.put(enumClass, adapter);
        }
        return adapter;
    }

    public void sync() {
        syncAction(mGreenSyncDaoBase.getCreatedObjects(), CREATED);
        syncAction(mGreenSyncDaoBase.getUpdatedObjects(), UPDATED);
        syncAction(mGreenSyncDaoBase.getDeletedObjects(), DELETED);
    }

    private void syncAction(Map<String, List<GreenSyncBase>> objects, String action) {

        for (Map.Entry<String, List<GreenSyncBase>> object : objects.entrySet()) {

            String className = object.getKey();
            List<GreenSyncBase> items = object.getValue();

            for (final GreenSyncBase item : items) {
                String json = mGson.toJson(item);

                SyncService.Callback callback = new SyncService.Callback() {
                    @Override
                    public void onSuccess(String externalId) {
                        item.clean();
                        item.setExternalId(externalId);
                        mGreenSyncDaoBase.update(item);
                    }

                    @Override
                    public void onFail(String errorMessage) {
                        Log.e("GreenSync", "Failed Sync: " + errorMessage);
                    }
                };

                if (action.equals(CREATED)) {
                    mSyncService.create(className, json, callback);
                } else if (action.equals(UPDATED)) {
                    mSyncService.update(className, json, callback);
                } else if (action.equals(DELETED)) {
                    mSyncService.delete(className, json, callback);
                }
            }
        }
    }

    public String syncBatch() {

        Map<String, Map> syncObjects = new HashMap<String, Map>();

        syncObjects.put(CREATED, mGreenSyncDaoBase.getCreatedObjects());
        syncObjects.put(UPDATED, mGreenSyncDaoBase.getUpdatedObjects());
        syncObjects.put(DELETED, mGreenSyncDaoBase.getDeletedObjects());

        return mGson.toJson(syncObjects);
    }

    public void processResponse(String json) {

        Type listType = new TypeToken<Map<String, List>>() {}.getType();
        Map<String, Map> map =  mGson.fromJson(json, listType);

        for (Map.Entry<String, Map> action : map.entrySet()) {
            if (action.getKey().equals(CREATED)) {
                processCreated(action.getValue());
            } else if (action.getKey().equals(UPDATED)) {
                processUpdated(action.getValue());
            } else if (action.getKey().equals(DELETED)) {
                processDeleted(action.getValue());
            }
        }
    }

    private void processCreated(Map<String, List> objects) {
        for (Map.Entry<String, List> object : objects.entrySet()) {
            Class<?> clazz = sClassMap.get(object.getKey());
            mSession.getDao(clazz).insertInTx(object.getValue());
        }
    }

    private void processUpdated(Map<String, List> objects) {
        for (Map.Entry<String, List> object : objects.entrySet()) {
            Class<?> clazz = sClassMap.get(object.getKey());
            mSession.getDao(clazz).updateInTx(object.getValue());
        }
    }

    private void processDeleted(Map<String, List> objects) {
        for (Map.Entry<String, List> object : objects.entrySet()) {
            Class<?> clazz = sClassMap.get(object.getKey());
            mSession.getDao(clazz).deleteInTx(object.getValue());
        }
    }

    private void loadPrimaryKey(AbstractDao dao, List<GreenSyncBase> objects) {
        dao.getDatabase();
    }
}
