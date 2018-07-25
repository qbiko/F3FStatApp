package pl.f3f_klif.f3fstatapp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class F3XVaultApiClient {
    private static final String BASE_URL = "http://www.f3xvault.com/api.php?";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(BASE_URL, params, responseHandler);
    }

    public static void post(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(BASE_URL, params, responseHandler);
    }
}
