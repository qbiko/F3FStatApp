package pl.f3f_klif.f3fstatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class SettingsActivity extends AppCompatActivity {

    private Button confirmButton;
    private Button cancelButton;
    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        responseTextView = findViewById(R.id.settings_response_textView);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //http://loopj.com/android-async-http/
                RequestParams params = new RequestParams();
                params.put("login", "kubiko13@gmail.com");
                params.put("password", "haslo");
                params.put("function", "getEventInfo");
                params.put("event_id", 1310);
                F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseText = new String(responseBody);
                        responseTextView.append(responseText);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        responseTextView.append(responseBody.toString());
                    }
                });
            }
        });


    }
}
