package pl.f3f_klif.f3fstatapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;

import static pl.f3f_klif.f3fstatapp.activities.SettingsActivity.responseText;
import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigationView;

    private boolean isActiveEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ObjectBox.init(this);

        isActiveEvent = DatabaseRepository.restoreAndInit();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer_desc,
                R.string.close_drawer_desc);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id)
            {
                case R.id.start_event:
                    if(isActiveEvent) {
                        Intent intent = new Intent(getApplicationContext(), EventRoundsActivity.class);
                        intent.putExtra("responseText", responseText);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.enter_to_start_event_with_incorrect_data, Toast.LENGTH_LONG).show();
                    }
                    return true;
                case R.id.settings:
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.synchronize_players:
                    if(isActiveEvent) {

                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    synchronizePlayersRequest();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(R.string.warning_synchronize_players).setPositiveButton(R.string.yes, dialogClickListener)
                                .setNegativeButton(R.string.no, dialogClickListener).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.enter_to_start_event_with_incorrect_data, Toast.LENGTH_LONG).show();
                    }
                    return true;
                default:
                    return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        isActiveEvent = DatabaseRepository.restoreAndInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void synchronizePlayersRequest() {
        Account currentAccount = DatabaseRepository.getAccount();
        Event event = DatabaseRepository.getEvent();
        RequestParams params = new RequestParams();
        String mail = currentAccount.getMail();
        String password = currentAccount.getPassword();
        params.put("login", mail);
        params.put("password", password);
        params.put("function", "getEventInfo");
        params.put("event_id", event.getF3fId());
        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseText = new String(responseBody);
                if (isSuccess(responseText)) {


                    String[] lines = responseText.split(System.getProperty("line.separator"));
                    DatabaseRepository.initNew((int)event.getF3fId(),
                            event.getMinGroupAmount(), lines,
                            getApplicationContext());

                    DatabaseRepository.createAccount(mail, password);

                    Toast.makeText(getApplicationContext(), R.string.player_list_was_updated,
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), R.string.player_list_was_not_updated,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}
