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
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.NON_STATUTORY_WEATHER_CONDITION;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.PREPARE_TIME;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.START_TIME;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.WIND_MEASUREMENT;

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

                    Float time = Float.parseFloat(message[3])/100;
                    Float partTime = time - mFragment.get().flightTimeResult;
                    mFragment.get().flightTimeResult = time;
                    if(baseNumber % 2 == 0) {
                        TextView textView = mFragment.get().getActivity().findViewById
                                (baseNumber);
                        textView.setText(mFragment.get().getString(R
                                .string.base_result, baseNumber, partTime));

                    }
                    else {
                        LinearLayout container = new LinearLayout(mFragment.get().getActivity());
                        container.setWeightSum(2);
                        container.setGravity(Gravity.CENTER_HORIZONTAL);
                        container.setOrientation(LinearLayout.HORIZONTAL);

                        mFragment.get().statsContainer.addView(container);

                        TextView baseResult1 = new TextView(mFragment.get().getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.weight = 1;
                        layoutParams.width = 0;
                        baseResult1.setLayoutParams(layoutParams);
                        baseResult1.setGravity(Gravity.CENTER);
                        baseResult1.setTextAppearance(mFragment.get().getActivity(), R.style
                                .TextAppearance_AppCompat_Large);

                        baseResult1.setText(mFragment.get().getString(R
                                .string.base_result, baseNumber, partTime));

                        container.addView(baseResult1);

                        TextView baseResult2 = new TextView(mFragment.get().getActivity());
                        baseResult2.setId(baseNumber+1);
                        baseResult2.setLayoutParams(layoutParams);
                        baseResult2.setGravity(Gravity.CENTER);
                        baseResult2.setTextAppearance(mFragment.get().getActivity(), R.style
                                .TextAppearance_AppCompat_Large);

                        container.addView(baseResult2);
                    }

                    mFragment.get().currentTimeTextView.setText(mFragment.get().getString(R.string
                            .flight_current_time, time));
                }
                break;
            }
            case COMPLETED_LAST_BASE: {
                int baseNumber = Integer.parseInt(message[2]);
                mFragment.get().liveStatusTextView.setText(R.string.player_flight_by_last_base);

                Float time = Float.parseFloat(message[3])/100;
                Float partTime = time - mFragment.get().flightTimeResult;
                mFragment.get().flightTimeResult = time;
                if(baseNumber % 2 == 0) {
                    TextView textView = mFragment.get().getActivity().findViewById
                            (baseNumber);
                    textView.setText(mFragment.get().getString(R
                            .string.base_result, baseNumber, partTime));

                }
                else {
                    LinearLayout container = new LinearLayout(mFragment.get().getActivity());
                    container.setWeightSum(2);
                    container.setGravity(Gravity.CENTER_HORIZONTAL);
                    container.setOrientation(LinearLayout.HORIZONTAL);

                    mFragment.get().statsContainer.addView(container);

                    TextView baseResult1 = new TextView(mFragment.get().getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.weight = 1;
                    layoutParams.width = 0;
                    baseResult1.setLayoutParams(layoutParams);
                    baseResult1.setGravity(Gravity.CENTER);
                    baseResult1.setTextAppearance(mFragment.get().getActivity(), R.style
                            .TextAppearance_AppCompat_Large);

                    baseResult1.setText(mFragment.get().getString(R
                            .string.base_result, baseNumber, partTime));

                    container.addView(baseResult1);

                    TextView baseResult2 = new TextView(mFragment.get().getActivity());
                    baseResult2.setId(baseNumber+1);
                    baseResult2.setLayoutParams(layoutParams);
                    baseResult2.setGravity(Gravity.CENTER);
                    baseResult2.setTextAppearance(mFragment.get().getActivity(), R.style
                            .TextAppearance_AppCompat_Large);

                    container.addView(baseResult2);
                }

                mFragment.get().assignPilotButton.setClickable(true);

                mFragment.get().currentTimeTextView.setText(mFragment.get().getString(R.string
                        .flight_current_time, time));

                mFragment.get().statusImageView.setImageResource(R.drawable.finish);

                break;
            }
            case NON_STATUTORY_WEATHER_CONDITION: {
                mFragment.get().liveStatusTextView.setText(R.string.non_statutory_weather_condition);
                break;
            }
            case WIND_MEASUREMENT: {
                float currentSpeed = Float.parseFloat(message[1])/10;
                float avgSpeed = Float.parseFloat(message[3])/10;
                int currentDirection = Integer.parseInt(message[2]);
                int avgDirection = Integer.parseInt(message[4]);

                mFragment.get().currentWindSpeedTextView.setText(mFragment.get().getString(R
                        .string.current_wind_speed, currentSpeed));
                mFragment.get().avgWindSpeedTextView.setText(mFragment.get().getString(R
                        .string.avg_wind_speed, avgSpeed));
                mFragment.get().currentWindDirectionTextView.setText(mFragment.get().getString(R
                        .string.current_wind_direction, currentDirection));
                mFragment.get().avgWindDirectionTextView.setText(mFragment.get().getString(R
                        .string.avg_wind_direction, avgDirection));

                //TODO collect wind data and write to sqlite
                break;
            }
        }

    }
}
