package com.example.everydaylevelup;

import com.example.everydaylevelup.model.RecordingState;

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
                // 마지막 시간 = 항상 현재 시간 가져오기
                saveLastValue();
                main.showLastTime();
                // 기간 = 마지막 시간 - 시작 시간
                // 달성도도 바로 바로 갱신
                saveDifference();
                main.showTodayRecord();
                // 1초마다 반복
                sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        if (onRecording == RecordingState.OFF) {
            // 시간 간격을 기존의 달성량에 추가하기
            main.record.addDifferenceToTodayRecord();
        }
        main.record.initDifference();
    }

    private void saveStartValue() {
        // 현재 시간 얻어오기
        long startTime = System.currentTimeMillis();
        // 현재 시간 저장
        main.record.setStartValue(startTime);
    }

    private void saveLastValue() {
        lastTime = System.currentTimeMillis();
        main.record.setLastValue(lastTime);
    }

    private void saveDifference() {
        main.record.calcDifference();
    }
}

