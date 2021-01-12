package com.example.everydaylevelup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView _yesterdayGoalAmount;
    TextView _incrementAmount;
    TextView _todayGoalAmount;
    TextView _todayRecordAmount;
    TextView _percentageAmount;

    TextView _startCounter;
    TextView _nowCounter;
    EditText _recordEditAmount;

    Button _recordStartButton;
    Button _recordCancelButton;
    Button _recordStopButton;
    Button _recordEditButton;
    Button _completeButton;

    String _fileName = "not-to-do";
    Thread progressWatcher;
    TimeRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        // loadData();
        record = new TimeRecord();
        checkNewSession();

        /* 뷰에 보여주기 */
//        showGoalAndIncrement();
//        showTodayRecord();
//        showCurrentRecord();
        changeButtonByState();

        /* 버튼 리스너 설정 */
        setStartButtonListener();
        setCancelButtonListener();
//        setStopButtonListener();
//        setEditButtonListener();
//        setCompleteButtonListener();

        /* 측정 스레드 시작? */
        // progressWatcher = new Thread();
        // progressWatcher.start();

        /* 위젯으로 시작, 정지 */
        /* 목표 달성 시 알림 기능 */
        /* 상단바 기능 */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // saveData();

        // progressWatcher.interrupt();
    }

    private void findViews() {
        _yesterdayGoalAmount = findViewById(R.id.yesterdayGoalAmount);
        _incrementAmount = findViewById(R.id.incrementAmount);
        _todayGoalAmount = findViewById(R.id.todayGoalAmount);
        _todayRecordAmount = findViewById(R.id.todayRecordAmount);
        _percentageAmount = findViewById(R.id.percentageAmount);

        _startCounter = findViewById(R.id.startCounter);
        _nowCounter = findViewById(R.id.nowCounter);
        _recordEditAmount = findViewById(R.id.recordEdit);

        _recordStartButton = findViewById(R.id.recordStartButton);
        _recordCancelButton = findViewById(R.id.recordCancelButton);
        _recordStopButton = findViewById(R.id.recordStopButton);
        _recordEditButton = findViewById(R.id.recordEditButton);
        _completeButton = findViewById(R.id.completeButton);
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences(_fileName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        String startTime = _recordStartButton.getText().toString();
        editor.putString("startTime", startTime);

        // 저장
        editor.apply();
    }

    private void loadData() {
        

        SharedPreferences sharedPref = getSharedPreferences(_fileName, 0);
        String startTime = sharedPref.getString("startTime", "");



    }

    private void checkNewSession() {

    }

    /* 뷰 */

    private void showGoalAndIncrement() {
        _yesterdayGoalAmount.setText(record.getYesterdayGoal());
        _incrementAmount.setText(record.getIncrement());
        _todayGoalAmount.setText(record.getTodayGoal());
    }

    private void showTodayRecord() {
        _todayRecordAmount.setText(record.getTodayRecord());
        _percentageAmount.setText(record.getPercentage());
    }

    private void showCurrentRecord() {
        _startCounter.setText(record.getStartAmount());
        _nowCounter.setText(record.getCurrentAmount());
    }

    /* 기록 상태에 따라 버튼 변경 */
    private void changeButtonByState() {
        // 현재 기록 중인지 아닌지 따지기
        if (record.isOnRecording()) {
            showCancelButton();
        } else {
            showStartButton();
        }
    }

    private void showStartButton() {
        // 기록 상태가 아니면 시작 버튼 보이기
        _recordStartButton.setVisibility(View.VISIBLE);
        _recordCancelButton.setVisibility(View.INVISIBLE);
    }

    private void showCancelButton() {
        // 기록 상태인 경우 시작 버튼 대신 취소 버튼 보이기
        _recordStartButton.setVisibility(View.INVISIBLE);
        _recordCancelButton.setVisibility(View.VISIBLE);
    }

    /* 컨트롤러 */

    private void setStartButtonListener() {
        _recordStartButton.setOnClickListener(v -> {
            // record.setStartAmount();
            record.setRecordingState(true);
            showCancelButton();
            // thread.start?
        });
    }

    private void setCancelButtonListener() {
        _recordCancelButton.setOnClickListener(v -> {
            // 재확인 알림창 띄우기

            record.setRecordingState(false);
            showStartButton();
        });
    }

    private void setStopButtonListener() {
        _recordStopButton.setOnClickListener(v -> {

        });
    }

    private void setEditButtonListener() {
        _recordEditButton.setOnClickListener(v -> {
            String editAmount = _recordEditAmount.getText().toString();
            record.setEditAmount(Integer.parseInt(editAmount));
        });
    }

    private void setCompleteButtonListener() {
        _completeButton.setOnClickListener(v -> {

        });
    }


}