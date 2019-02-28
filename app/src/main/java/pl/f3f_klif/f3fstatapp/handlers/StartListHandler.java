package pl.f3f_klif.f3fstatapp.handlers;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import pl.f3f_klif.f3fstatapp.activities.EventGroupsActivity;
import pl.f3f_klif.f3fstatapp.groups.fragments.CurrentFlyFragment;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.NEW_FLIGHT;

public class StartListHandler extends Handler {

    private final WeakReference<EventGroupsActivity> mActivity;

    public StartListHandler(EventGroupsActivity activity) {
        mActivity = new WeakReference<>(activity);
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

        if(NEW_FLIGHT.equals(typeOfMessage)) {
            int flightNumber = Integer.parseInt(message[1]);
            mActivity.get().showFragment(CurrentFlyFragment.newInstance(flightNumber));
        }

    }
}
