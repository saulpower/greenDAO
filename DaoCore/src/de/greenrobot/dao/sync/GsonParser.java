package de.greenrobot.dao.sync;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by saulhoward on 7/11/14.
 */
public class GsonParser implements JsonParser {

    private Gson mGson;

    public GsonParser(GreenSync greenSync) {

        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        builder.registerTypeAdapterFactory(new EntityTypeAdapterFactory(greenSync));
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        mGson = builder.create();
    }

    @Override
    public String toJson(Object src) {
        return mGson.toJson(src);
    }

    @Override
    public <T> T fromJson(String json, Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }
}
