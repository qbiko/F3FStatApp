package pl.f3f_klif.f3fstatapp.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;

public class F3XVaultApiClient {
    private static final String BASE_URL = "http://www.f3xvault.com/api.php?";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(BASE_URL, params, responseHandler);
    }

    public static void post(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(BASE_URL, params, responseHandler);
    }

    public static boolean isSuccess(String responseBody) {
        return "1".equals(responseBody.substring(0,1));
    }
}
