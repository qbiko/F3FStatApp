package pl.f3f_klif.f3fstatapp.handlers;

import android.os.Handler;
import android.os.Message;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.fragments.CurrentFlyFragment;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.COMPLETED_LAST_BASE;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.COMPLETED_NEXT_BASE;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.NEW_FLIGHT;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.PREPARE_TIME;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.START_TIME;

public class CurrentFlyHandler extends Handler {

    private final WeakReference<CurrentFlyFragment> mFragment;

    public CurrentFlyHandler(CurrentFlyFragment fragment) {
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UsbService.MESSAGE_FROM_SERIAL_PORT:
                String data = (String) msg.obj;
                readMessage(data);
                //mFragment.get().terminal.append(data);
                break;
            case UsbService.CTS_CHANGE:
                Toast.makeText(mFragment.get().getActivity(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                break;
            case UsbService.DSR_CHANGE:
                Toast.makeText(mFragment.get().getActivity(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void readMessage(String data) {

        String[] message = data.split(";");
        String typeOfMessage = data.split(";")[0];

        switch(typeOfMessage) {
            case NEW_FLIGHT: {

                break;
            }
            case PREPARE_TIME: {
                if(!mFragment.get().getString(R.string.flight_current_time).equals(mFragment.get()
                        .liveStatusTextView.getText().toString())) {
                    mFragment.get().liveStatusTextView.setText(R.string.prepare_period);
                }

                float currentTime = Float.parseFloat(message[1]);
                mFragment.get().currentTimeTextView.setText(mFragment.get().getString(R.string
                        .flight_current_time, currentTime));
                float prepareTime = 30 - currentTime;
                mFragment.get().prepareTimeResultTextView.setText(mFragment.get().getString(R
                        .string.prepare_time, prepareTime));

                break;
            }
            case START_TIME: {
                if(mFragment.get().prepareTimeResultTextView.getVisibility() != View.VISIBLE) {
                    mFragment.get().prepareTimeResultTextView.setVisibility(View.VISIBLE);
                    mFragment.get().liveStatusTextView.setText(R.string.start_period);
                }

                float currentTime = Float.parseFloat(message[1]);
                mFragment.get().currentTimeTextView.setText(mFragment.get().getString(R.string
                        .flight_current_time, currentTime));
                float startTime = 30 - currentTime;
                mFragment.get().startTimeResultTextView.setText(mFragment.get().getString(R
                        .string.start_time, startTime));

                break;
            }
            case COMPLETED_NEXT_BASE: {
                int baseNumber = Integer.parseInt(message[2]);
                if(baseNumber == 0) {
                    mFragment.get().startTimeResultTextView.setVisibility(View.VISIBLE);
                    mFragment.get().liveStatusTextView.setText(R.string.player_started);
                }
                else {
                    mFragment.get().liveStatusTextView.setText(mFragment.get().getString(R
                            .string.player_flight_by_base, baseNumber));
                    LinearLayout container;
                    if(baseNumber % 2 == 0) {
                        container = mFragment.get().getActivity().findViewById
                                (baseNumber-1);
                    }
                    else {
                        container = new LinearLayout(mFragment.get().getActivity());
                        container.setId(baseNumber);
                        container.setWeightSum(2);
                        container.setGravity(Gravity.CENTER_HORIZONTAL);
                        container.setOrientation(LinearLayout.HORIZONTAL);

                        mFragment.get().statsContainer.addView(container);
                    }

                    Float time = Float.parseFloat(message[3])/100;
                    TextView baseResult = new TextView(mFragment.get().getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.weight = 1;
                    layoutParams.width = 0;
                    baseResult.setLayoutParams(layoutParams);
                    baseResult.setGravity(Gravity.CENTER);
                    baseResult.setTextAppearance(mFragment.get().getActivity(), R.style
                            .TextAppearance_AppCompat_Large);

                    baseResult.setText(mFragment.get().getString(R
                            .string.base_result, baseNumber, time));

                    container.addView(baseResult);
                }
                break;
            }
            case COMPLETED_LAST_BASE: {
                int baseNumber = Integer.parseInt(message[2]);
                mFragment.get().liveStatusTextView.setText(R.string.player_flight_by_last_base);
                LinearLayout container;
                if(baseNumber % 2 == 0) {
                    container = mFragment.get().getActivity().findViewById
                            (baseNumber-1);
                }
                else {
                    container = new LinearLayout(mFragment.get().getActivity());
                    container.setId(baseNumber);
                    container.setWeightSum(2);
                    container.setGravity(Gravity.CENTER_HORIZONTAL);
                    container.setOrientation(LinearLayout.HORIZONTAL);

                    mFragment.get().statsContainer.addView(container);
                }

                Float time = Float.parseFloat(message[3])/100;
                TextView baseResult = new TextView(mFragment.get().getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1;
                layoutParams.width = 0;
                baseResult.setLayoutParams(layoutParams);
                baseResult.setGravity(Gravity.CENTER);
                baseResult.setTextAppearance(mFragment.get().getActivity(), R.style
                        .TextAppearance_AppCompat_Large);

                baseResult.setText(mFragment.get().getString(R
                        .string.base_result, baseNumber, time));

                container.addView(baseResult);

                mFragment.get().assignPilotButton.setClickable(true);

                break;
            }
        }

    }
}
