package de.greenrobot.dao.sync;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.greenrobot.dao.DaoEnum;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saulhoward on 3/25/14.
 */
public class EntityTypeAdapterFactory implements TypeAdapterFactory {

    public final String TAG = getClass().getSimpleName();

    private GreenSync mGreenSync;

    public EntityTypeAdapterFactory(GreenSync greenSync) {
        mGreenSync = greenSync;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        Class<T> rawType = (Class<T>) type.getRawType();

        if (rawType.isEnum()) {
            return new EnumTypeAdapter<T>((Class<? extends DaoEnum>) rawType);
        }

        if (Map.class.isAssignableFrom(rawType)) {
            return new MapTypeAdapter<T>(gson, gson.getDelegateAdapter(this, type));
        }

        return null;
    }

    private class EnumTypeAdapter<T> extends TypeAdapter<T> {

        private EnumAdapter adapter;

        public EnumTypeAdapter(Class<? extends DaoEnum> rawType) {
            adapter = mGreenSync.enumAdapter(rawType);
        }

        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(adapter.toInt((DaoEnum) value));
            }
        }

        public T read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            } else {
                return (T) adapter.fromInt(reader.nextInt());
            }
        }
    };

    private class MapTypeAdapter<T> extends TypeAdapter<T> {

        private TypeAdapter<T> mDefault;
        private Gson mGson;

        public MapTypeAdapter(Gson gson, TypeAdapter<T> mDefault) {
            mGson = gson;
            this.mDefault = mDefault;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            mDefault.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {

            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            Map<String, Map> map = new HashMap<String, Map>();

            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                map.put(name, readAction(in));
            }
            in.endObject();

            return (T) map;
        }

        private Map readAction(JsonReader in) throws IOException {

            Map<String, List> map = new HashMap<String, List>();

            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                Type type = GreenSync.getTypeToken(name);
                List list = mGson.fromJson(in, type);
                map.put(name, list);
            }
            in.endObject();

            return map;
        }
    }
}