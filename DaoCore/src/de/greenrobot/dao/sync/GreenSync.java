package de.greenrobot.dao.sync;

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

    public GreenSync(AbstractDaoSession session) {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        builder.registerTypeAdapterFactory(new EntityTypeAdapterFactory(this));
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        mGson = builder.create();
        mSession = session;

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

    public String sync() {

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
