package pl.f3f_klif.f3fstatapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;

import static pl.f3f_klif.f3fstatapp.activities.SettingsActivity.email;
import static pl.f3f_klif.f3fstatapp.activities.SettingsActivity.isAccountCorrect;
import static pl.f3f_klif.f3fstatapp.activities.SettingsActivity.password;
import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class StartEventValidatorActivity extends AppCompatActivity {

    @BindView(R.id.confirm_button)
    Button confirmButton;
    @BindView(R.id.cancel_button)
    Button cancelButton;
    @BindView(R.id.result_image)
    ImageView resultImageView;
    @BindView(R.id.response_textView)
    TextView responseTextView;
    @BindView(R.id.event_id_editText)
    EditText eventIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_event_validator);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.cancel_button)
    void onClickCancelButton() {
        finish();
    }

    @OnClick(R.id.confirm_button)
    void onClickConfirmButton() { //http://loopj.com/android-async-http/
        int eventId = Integer.parseInt(eventIdEditText.getText().toString());
        RequestParams params = new RequestParams();
        params.put("login", email);
        params.put("password", password);
        params.put("function", "getEventInfo");
        params.put("event_id", eventId);
        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseText = new String(responseBody);
                if(isSuccess(responseText)) {
                    responseTextView.setText(R.string.success_authorization_text);
                    resultImageView.setImageResource(R.drawable.success);
                }
                else {
                    responseTextView.setText(R.string.failure_authorization_text);
                    resultImageView.setImageResource(R.drawable.error);
                }
                //responseTextView.append(responseText);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}
