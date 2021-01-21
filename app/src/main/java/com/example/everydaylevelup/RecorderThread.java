package com.example.everydaylevelup;

import com.example.everydaylevelup.model.RecordingState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RecorderThread extends Thread {
    MainActivity main;
    RecordingState onRecording;
    long lastTime;

    RecorderThread(MainActivity main) {
        this.main = main;
        onRecording = RecordingState.ON;
    }

    public void setRecordingState(RecordingState state) {
        onRecording = state;
    }

    @Override
    public void run() {
        // 현재 시간을 시작 시간으로 저장
        if (onRecording == RecordingState.CONTINUE) {
            onRecording = RecordingState.ON;
        } else {
            saveStartValue();
            main.showStartTime();
        }

        while (onRecording == RecordingState.ON) {
            try {
                saveLastValue();
                main.showLastTime();
                saveDifference();
                main.showTodayRecord();
                // 1초마다 반복
                sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        if (onRecording == RecordingState.OFF) {
            main.record.addDifferenceToTodayRecord();
        }
        main.record.initDifference();
    }

    private void saveStartValue() {
        // 현재 시간 얻어오기
        long startTime = Calendar.getInstance().getTimeInMillis();
        // 현재 시간 저장
        main.record.setStartValue(startTime);
    }

    private void saveLastValue() {
        lastTime = Calendar.getInstance().getTimeInMillis();
        main.record.setLastValue(lastTime);
    }

    private void saveDifference() {
        main.record.calcDifference();
    }
}

