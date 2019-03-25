package pl.f3f_klif.f3fstatapp.handlers;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import pl.f3f_klif.f3fstatapp.activities.EventGroupsActivity;
import pl.f3f_klif.f3fstatapp.groups.fragments.CurrentFlyFragment;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.NEW_FLIGHT;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.PREPARE_TIME;

public class StartListHandler extends Handler {

    private final WeakReference<EventGroupsActivity> mActivity;
    private Round round;

    private static int lastFlightNumber = 0;

    public StartListHandler(EventGroupsActivity activity, Round round) {
        mActivity = new WeakReference<>(activity);
        this.round = round;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UsbService.MESSAGE_FROM_SERIAL_PORT:
                String data = (String) msg.obj;
                readMessage(data);
                //mActivity.get().terminal.append(data);
                break;
            case UsbService.CTS_CHANGE:
                Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                break;
            case UsbService.DSR_CHANGE:
                Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void readMessage(String data) {

        String[] message = data.split(";");
        String typeOfMessage = data.split(";")[0];

        if(round.state == RoundState.STARTED) {
            if(NEW_FLIGHT.equals(typeOfMessage)) {
                int flightNumber = Integer.parseInt(message[1]);
                lastFlightNumber = flightNumber;
                mActivity.get().showFragment(CurrentFlyFragment.newInstance(round, flightNumber));
            }
            else if(PREPARE_TIME.equals(typeOfMessage)) {
                int flightNumber = lastFlightNumber + 1;
                lastFlightNumber = flightNumber;
                mActivity.get().showFragment(CurrentFlyFragment.newInstance(round, flightNumber));
            }
        }

    }
}
