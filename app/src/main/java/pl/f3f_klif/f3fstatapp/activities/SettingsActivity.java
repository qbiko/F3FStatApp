package pl.f3f_klif.f3fstatapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;

import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.confirm_button)
    Button confirmButton;
    @BindView(R.id.cancel_button)
    Button cancelButton;
    @BindView(R.id.settings_response_textView)
    TextView responseTextView;
    @BindView(R.id.email_editText)
    EditText emailEditText;
    @BindView(R.id.password_editText)
    EditText passwordEditText;
    @BindView(R.id.event_id_editText)
    EditText eventIdEditText;
    @BindView(R.id.min_group_amount_editText)
    EditText minGroupAmountEditText;
    @BindView(R.id.result_image)
    ImageView resultImageView;
    @BindView(R.id.wind_dir_switch)
    Switch windDirSwitch;
    @BindView(R.id.wind_speed_switch)
    Switch windSpeedSwitch;

    Event event;
    Account account;

    public static String responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        account = DatabaseRepository.getAccount();
        if(account != null) {
            emailEditText.setText(account.getMail());
            passwordEditText.setText((account.getPassword()));
        }

        event = DatabaseRepository.getEvent();
        if(event != null) {
            eventIdEditText.setText(String.valueOf(event.getF3fId()));
            minGroupAmountEditText.setText(String.valueOf(event.getMinGroupAmount()));
            windDirSwitch.setChecked(account.isWindDir());
            windSpeedSwitch.setChecked(account.isWindSpeed());
        }
    }

    @OnClick(R.id.cancel_button)
    void onClickCancelButton() {
        finish();
    }

    @OnClick(R.id.confirm_button)
    void onClickConfirmButton() { //http://loopj.com/android-async-http/
        responseTextView.setText("");
        int eventId = Integer.parseInt(eventIdEditText.getText().toString());
        String mail = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        int minGroupAmount = Integer.parseInt(minGroupAmountEditText.getText().toString());
        boolean windDir = windDirSwitch.isChecked();
        boolean windSpeed = windSpeedSwitch.isChecked();

        if(event == null) {
            RequestParams params = new RequestParams();
            params.put("login", mail);
            params.put("password", password);
            params.put("function", "getEventInfo");
            params.put("event_id", eventId);
            F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    responseText = new String(responseBody);
                    if (isSuccess(responseText)) {


                        String[] lines = responseText.split(System.getProperty("line.separator"));
                        DatabaseRepository.initNew(eventId, minGroupAmount, lines, getApplicationContext());

                        account = DatabaseRepository.createAccount(mail, password, windDir, windSpeed);


                        responseTextView.setText(R.string.success_authorization_text);
                        resultImageView.setImageResource(R.drawable.success);
                    } else {
                        responseTextView.setText(R.string.failure_authorization_text);
                        resultImageView.setImageResource(R.drawable.error);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        }
        else if(eventId != event.getF3fId() || minGroupAmount != event.getMinGroupAmount()) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        RequestParams params = new RequestParams();
                        params.put("login", mail);
                        params.put("password", password);
                        params.put("function", "getEventInfo");
                        params.put("event_id", eventId);
                        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                responseText = new String(responseBody);
                                if (isSuccess(responseText)) {


                                    String[] lines = responseText.split(System.getProperty("line.separator"));
                                    DatabaseRepository.initNew(eventId, minGroupAmount, lines, getApplicationContext());

                                    account = DatabaseRepository.createAccount(mail, password, windDir, windSpeed);


                                    responseTextView.setText(R.string.success_authorization_text);
                                    resultImageView.setImageResource(R.drawable.success);
                                } else {
                                    responseTextView.setText(R.string.failure_authorization_text);
                                    resultImageView.setImageResource(R.drawable.error);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setMessage(R.string.warning_create_event).setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();

        }
        else if(account == null || (!account.getMail().equals(mail) || !account.getPassword().equals(password))) {
            RequestParams params = new RequestParams();
            params.put("login", mail);
            params.put("password", password);
            params.put("function", "showParameters");
            params.put("function_name", "getEventInfo");
            F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    responseText = new String(responseBody);
                    if (isSuccess(responseText)) {
                        DatabaseRepository.cleanAccount();
                        account = DatabaseRepository.createAccount(mail, password, windDir, windSpeed);

                        responseTextView.setText(R.string.success_authorization_text);
                        resultImageView.setImageResource(R.drawable.success);
                    } else {
                        //DatabaseRepository.cleanAccount();

                        responseTextView.setText(R.string.failure_authorization_text);
                        resultImageView.setImageResource(R.drawable.error);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        }
        else if(account.isWindDir() != windDir) {
            account.setWindDir(windDir);
            Toast.makeText(getApplicationContext(),
                    R.string.correctly_update_wind_settings, Toast.LENGTH_SHORT).show();
        }
        else if(account.isWindSpeed() != windSpeed) {
            account.setWindSpeed(windSpeed);
            Toast.makeText(getApplicationContext(),
                    R.string.correctly_update_wind_settings, Toast.LENGTH_SHORT).show();
        }
        else {
            responseTextView.setText(R.string.not_necessary_authorization_text);
            //resultImageView.setImageResource(R.drawable.error);
        }

    }
}
