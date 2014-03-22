package de.greenrobot.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.greenrobot.dao.DaoEnum;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by saulhoward on 3/21/14.
 */
public class GreenSync {

    private Gson gson;

    private final Map<Class<? extends DaoEnum>, EnumAdapter<? extends DaoEnum>> enumAdapters =
            new LinkedHashMap<Class<? extends DaoEnum>, EnumAdapter<? extends DaoEnum>>();

    public GreenSync() {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        gson = builder.create();
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

    public <T> boolean sync(T object) {
        return false;
    }

    public <T> String sync(Class<T> clazz, List<T> objects) {

        Map<String, List<T>> map = new HashMap<String, List<T>>();
        map.put(clazz.getSimpleName(), objects);

        return gson.toJson(map);
    }
}
