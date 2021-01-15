package com.example.everydaylevelup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // region Variables

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

    TimeRecord record;
    Thread goalTracker;
    RecorderThread progressTracker;
    String _fileName = "not-to-do";

    // endregion Variables

    // region onCreate and onDestroy

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
        showTodayRecord();
        showCurrentRecord();
        changeButtonByState();

        /* 버튼 리스너 설정 */
        setStartButtonListener();
        setCancelButtonListener();
        setStopButtonListener();
//        setEditButtonListener();
//        setCompleteButtonListener();

        /* 목표 달성 시 알림 기능 */
        goalTracker = new Thread();
        goalTracker.start();

        /* 위젯으로 시작, 정지 */
        /* 상단바 기능 */
        /* amount 대신에 value 로 명칭 변경 */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // saveData();

        // progressWatcher.interrupt();
    }

    // endregion onCreate and onDestroy

    // region Initialization

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

    // endregion initialization

    // region View

    private void showGoalAndIncrement() {
        _yesterdayGoalAmount.setText(String.valueOf(record.getYesterdayGoal()));
        _incrementAmount.setText(String.valueOf(record.getIncrement()));
        _todayGoalAmount.setText(String.valueOf(record.getTodayGoal()));
    }

    private void showTodayRecord() {
        _todayRecordAmount.setText(String.valueOf(record.getTodayRecord()));
        _percentageAmount.setText(String.valueOf(record.getPercentage()));
    }

    private void showCurrentRecord() {
        _startCounter.setText(String.valueOf(record.getStartAmount()));
        _nowCounter.setText(String.valueOf(record.getCurrentAmount()));
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

    // endregion View

    // region Controller

    private void setStartButtonListener() {
        _recordStartButton.setOnClickListener(v -> {
//            record.saveStartValue();
            record.setRecordingState(true);
            // 스레드 시작
            progressTracker = new RecorderThread(record, _nowCounter);
            progressTracker.start();
            showCancelButton();
        });
    }

    private void setCancelButtonListener() {
        _recordCancelButton.setOnClickListener(v -> {
            // 재확인 알림창 띄우기
            progressTracker.setRecordingState(false);
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

    // endregion Controller
}