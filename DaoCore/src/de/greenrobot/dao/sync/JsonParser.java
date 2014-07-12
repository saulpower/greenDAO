package de.greenrobot.dao.sync;

import java.lang.reflect.Type;

/**
 * Created by saulhoward on 7/11/14.
 */
public interface JsonParser {
    String toJson(Object src);
    <T> T fromJson(String json, Type typeOfT);
}
