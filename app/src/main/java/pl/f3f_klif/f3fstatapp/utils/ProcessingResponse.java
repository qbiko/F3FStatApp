package pl.f3f_klif.f3fstatapp.utils;

import android.content.Intent;

public class ProcessingResponse {

    public static String[] receiveExtraAndDividePerLine(Intent intent) {
        String responseText = intent.getStringExtra("responseText");
        String[] lines = responseText.split(System.getProperty("line.separator"));
        return lines;
    }
}
