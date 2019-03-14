package pl.f3f_klif.f3fstatapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    EditText eventIdEdtiText;
    @BindView(R.id.min_group_amount_editText)
    EditText minGroupAmountEditText;
    @BindView(R.id.result_image)
    ImageView resultImageView;

    Box<Account> accountBox;
    Box<Event> eventBox;

    public static String responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        accountBox = ObjectBox.get().boxFor(Account.class);
        eventBox = ObjectBox.get().boxFor(Event.class);


        if(!accountBox.isEmpty()) {
            Account account = accountBox.getAll().get(0);
            emailEditText.setText(account.getMail());
            passwordEditText.setText((account.getPassword()));
        }

        if(!eventBox.isEmpty()) {
            Event event = eventBox.getAll().get(0);
            eventIdEdtiText.setText(String.valueOf(event.getF3fId()));

            DatabaseRepository.init();
        }
    }

    @OnClick(R.id.cancel_button)
    void onClickCancelButton() {
        finish();
    }

    @OnClick(R.id.confirm_button)
    void onClickConfirmButton() { //http://loopj.com/android-async-http/
        int eventId = Integer.parseInt(eventIdEdtiText.getText().toString());
        String mail = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

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
                    int groupsCount = 3;

                    String[] lines = responseText.split(System.getProperty("line.separator"));
                    DatabaseRepository.initNew(eventId, groupsCount, lines);

                    accountBox.removeAll();
                    Account account = new Account(mail, password);
                    accountBox.put(account);


                    responseTextView.setText(R.string.success_authorization_text);
                    resultImageView.setImageResource(R.drawable.success);
                } else {
                    responseTextView.setText(R.string.failure_authorization_text);
                    resultImageView.setImageResource(R.drawable.error);
                }
                //responseTextView.append(responseText);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}
