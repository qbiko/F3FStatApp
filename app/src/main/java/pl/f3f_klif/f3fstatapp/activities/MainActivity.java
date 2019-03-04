package pl.f3f_klif.f3fstatapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;

import static pl.f3f_klif.f3fstatapp.activities.SettingsActivity.isAccountCorrect;

public class MainActivity extends AppCompatActivity {

    private Box<Event> eventBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ObjectBox.init(this);
    }

    @OnClick(R.id.start_event_button)
    void onClickStartEventButton() {
        if(isAccountCorrect) {
            Intent intent = new Intent(getApplicationContext(), StartEventValidatorActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.enter_to_start_event_with_incorrect_data, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.settings_button)
    void onClickSettingsButton() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.uart_receiving_button)
    void onUartTestButtonClick() {
        Intent intent = new Intent(getApplicationContext(), ChooseEventActivity.class);
        startActivity(intent);
    }
}
