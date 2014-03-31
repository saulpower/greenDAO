package de.greenrobot.daoexample;

import android.util.Base64;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saulhoward on 3/31/14.
 */
public class StringRequest extends JsonRequest<String> {

    /**
     * Creates a new request
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param payload The body of the request
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public StringRequest(int method, String url, String payload,
                         Listener<String> listener, ErrorListener errorListener) {
        super(method, url, payload, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        HashMap<String, String> params = new HashMap<String, String>();

        String creds = String.format("%s:%s","asdfjkl;qweruiop","");
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);

        return params;
    }
}
