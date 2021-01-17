package com.example.everydaylevelup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.everydaylevelup.model.RecordingState;
import com.example.everydaylevelup.model.TimeRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // region Variables

    TextView yesterdayGoalAmount;
    TextView incrementAmount;
    TextView todayGoalAmount;
    TextView todayRecordAmount;
    TextView percentageAmount;

    TextView startTimeCounter;
    TextView lastTimeCounter;
    EditText recordEditAmount;

    Button recordStartButton;
    Button recordCancelButton;
    Button recordStopButton;
    Button recordEditButton;
    Button completeButton;

    TimeRecord record;
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
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
        yesterdayGoalAmount = findViewById(R.id.yesterdayGoalAmount);
        incrementAmount = findViewById(R.id.incrementAmount);
        todayGoalAmount = findViewById(R.id.todayGoalAmount);
        todayRecordAmount = findViewById(R.id.todayRecordAmount);
        percentageAmount = findViewById(R.id.percentageAmount);

        startTimeCounter = findViewById(R.id.startCounter);
        lastTimeCounter = findViewById(R.id.nowCounter);
        recordEditAmount = findViewById(R.id.recordEdit);

        recordStartButton = findViewById(R.id.recordStartButton);
        recordCancelButton = findViewById(R.id.recordCancelButton);
        recordStopButton = findViewById(R.id.recordStopButton);
        recordEditButton = findViewById(R.id.recordEditButton);
        completeButton = findViewById(R.id.completeButton);
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences(_fileName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        String startTime = recordStartButton.getText().toString();
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
        yesterdayGoalAmount.setText(String.valueOf(record.getYesterdayGoal()));
        incrementAmount.setText(String.valueOf(record.getIncrement()));
        todayGoalAmount.setText(String.valueOf(record.getTodayGoal()));
    }

    private void showTodayRecord() {
        todayRecordAmount.setText(String.valueOf(record.getTodayRecord()));
        percentageAmount.setText(String.valueOf(record.getPercentage()));
    }

    private void showCurrentRecord() {
        startTimeCounter.setText(String.valueOf(record.getStartValue()));
        lastTimeCounter.setText(String.valueOf(record.getLastValue()));
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
        recordStartButton.setVisibility(View.VISIBLE);
        recordCancelButton.setVisibility(View.INVISIBLE);
    }

    private void showCancelButton() {
        // 기록 상태인 경우 시작 버튼 대신 취소 버튼 보이기
        recordStartButton.setVisibility(View.INVISIBLE);
        recordCancelButton.setVisibility(View.VISIBLE);
    }

    // endregion View

    // region Controller

    private void setStartButtonListener() {
        recordStartButton.setOnClickListener(v -> {
            // 측정 스레드 시작
            progressTracker = new RecorderThread(this);
            progressTracker.start();
            record.setRecordingState(true);
            showCancelButton();
        });
    }

    private void setCancelButtonListener() {
        recordCancelButton.setOnClickListener(v -> {
            // 재확인 알림창 띄우기
            postProcessByState(null);
        });
    }

    private void setStopButtonListener() {
        recordStopButton.setOnClickListener(v -> {
            postProcessByState(RecordingState.OFF);
        });
    }

    private void postProcessByState(RecordingState state) {
        // 측정 스레드 없을 때의 오류 방지
        if (progressTracker == null) {
            return;
        }

        // 스레드 종료
        progressTracker.setRecordingState(state);
        progressTracker.interrupt();
        progressTracker = null;
        record.setRecordingState(false);
        showStartButton();
    }

    private void setEditButtonListener() {
        recordEditButton.setOnClickListener(v -> {
            String editAmount = recordEditAmount.getText().toString();
            record.setEditAmount(Integer.parseInt(editAmount));
        });
    }

    private void setCompleteButtonListener() {
        completeButton.setOnClickListener(v -> {

        });
    }

    // endregion Controller
}