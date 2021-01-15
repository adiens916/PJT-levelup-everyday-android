package com.example.everydaylevelup;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RecorderThread extends Thread {
    TimeRecord record;
    TextView lastTimeCounter;
    Calendar calendar;
    SimpleDateFormat timeFormat;
    boolean onRecording;

    RecorderThread(TimeRecord record, TextView lastTimeCounter) {
        this.record = record;
        this.lastTimeCounter = lastTimeCounter;
        timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        onRecording = true;
    }

    public void setRecordingState(boolean value) {
        onRecording = value;
    }

    @Override
    public void run() {
        while (onRecording) {
            try {
                // 현재 시간 얻어오고, 저장
                calendar = Calendar.getInstance();
                long lastTime = calendar.getTimeInMillis();
                record.setCurrentAmount(lastTime);

                // 현재 시간을 출력
                String lastTimeString = timeFormat.format(lastTime);
                lastTimeCounter.setText(lastTimeString);

                // 1초마다 반복
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
