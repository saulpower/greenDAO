package de.greenrobot.daoexample;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import de.greenrobot.dao.sync.SyncService;

/**
 * Created by saulhoward on 3/31/14.
 */
public class MySyncServce implements SyncService {

    private static MySyncServce sSyncService;

    private final String BASE_URL = "http://smartcare-api.azurewebsites.net";

    private RequestQueue mRequestQueue;
    private String urlPattern = "%s/api/v1/%d/%s";

    public static MySyncServce get(Context context) {

        if (sSyncService == null) {
            sSyncService = new MySyncServce(context);
        }

        return sSyncService;
    }

    MySyncServce(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void create(String className, String payload, final Callback callback) {
        sendRequest(Request.Method.POST, className, payload, null, callback);
    }

    @Override
    public void read(String className, String id, final Callback callback) {
        sendRequest(Request.Method.GET, className, null, id, callback);
    }

    @Override
    public void update(String className, String payload, Callback callback) {
        sendRequest(Request.Method.PATCH, className, payload, null, callback);
    }

    @Override
    public void delete(String className, String id, Callback callback) {
        sendRequest(Request.Method.DELETE, className, null, id, callback);

    }

    private void sendRequest(int method, String className, String payload, String id, final Callback callback) {

        StringBuilder urlBuilder = new StringBuilder(String.format(urlPattern, BASE_URL, 0, className));

        if (id != null) {
            urlBuilder.append("/");
            urlBuilder.append(id);
        }

        StringRequest request = new StringRequest(method, urlBuilder.toString(), payload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFail(new String(error.networkResponse.data));
            }
        });

        mRequestQueue.add(request);
    }
}
