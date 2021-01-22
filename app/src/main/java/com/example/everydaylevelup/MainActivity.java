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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // region Variables

    TextView yesterdayGoalViewer;
    TextView incrementViewer;
    TextView todayGoalViewer;
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
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
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
        showGoalAndIncrement();
        showTodayRecord();
        showStartTime();
        showLastTime();
        changeButtonByState();

        /* 버튼 리스너 설정 */
        setStartButtonListener();
        setCancelButtonListener();
        setStopButtonListener();
        setEditButtonListener();
        setCompleteButtonListener();

        /* 목표 도달 시 알림(notification) */
        /* 만약 개수를 카운팅하는 경우엔, 목표 달성하자마자 바로 축하 메시지 */
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
        yesterdayGoalViewer = findViewById(R.id.yesterdayGoalViewer);
        incrementViewer = findViewById(R.id.incrementViewer);
        todayGoalViewer = findViewById(R.id.todayGoalViewer);
        todayRecordAmount = findViewById(R.id.todayRecordViewer);
        percentageAmount = findViewById(R.id.percentageViewer);

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

    // region View - Text

    private void showGoalAndIncrement() {
        long yesterdayGoal = record.getYesterdayGoal();
        yesterdayGoalViewer.setText(literalAsHMS(yesterdayGoal));
        long increment = record.getIncrement();
        incrementViewer.setText(literalAsSignedHMS(increment));
        long todayGoal = record.getTodayGoal();
        todayGoalViewer.setText(literalAsHMS(todayGoal));
    }

    public void showTodayRecord() {
        long todayRecordWithDifference = record.getTodayRecord() + record.getDifference();
        todayRecordAmount.setText(String.valueOf(literalAsHMS(todayRecordWithDifference)));
        double percentage = Math.round(record.getTodayPercentage());
        percentageAmount.setText(String.valueOf(percentage));
    }

    public void showStartTime() {
        long startTime = record.getStartValue();
        startTimeCounter.setText(literalAsHMS(startTime));
    }

    public void showLastTime() {
        long lastTime = record.getLastValue();
        lastTimeCounter.setText(literalAsHMS(lastTime));
    }

    private void setCounterAsZero() {
        startTimeCounter.setText("0");
        lastTimeCounter.setText("0");
    }

    private String literalAsHMS(long dateTime) {
        return timeFormat.format(dateTime);
    }

    private String literalAsSignedHMS(long increment) {
        if (increment < 0) {
            return "-" + literalAsHMS(Math.abs(increment));
        } else {
            return literalAsHMS(increment);
        }
    }

    // endregion View - Text

    // region View - Button

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

    // endregion View - Button

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
            // 재확인 알림창 띄우기 -> 예/아니오 알림 대화상자 처리
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
        showTodayRecord();
        // 처음과 마지막 시간 초기화, 기록 상태 0으로 바꾸기
        setCounterAsZero();
    }

    /* 오늘 달성량 직접 수정 */
    private void setEditButtonListener() {
        recordEditButton.setOnClickListener(v -> {
            String editAmount = recordEditAmount.getText().toString();
            record.setEditAmount(Integer.parseInt(editAmount));
        });
    }

    /* 현재 날짜를 받아와서 오늘 날짜와 같은지 비교 -> 달라질 때만 기록 바뀜 */
    /* : 일단 수동으로 날짜 넘기는 버튼을 추가해놓을까 */
    /* : "이대로 오늘을 마치시겠습니까?" */
    /* -> 그러면 두구두구둔...! 축하합니다! 레벨업! */
    private void setCompleteButtonListener() {
        completeButton.setOnClickListener(v -> {
            // 기록 중이었던 경우, 자동으로 정지하기
            postProcessByState(RecordingState.OFF);
            record.updateGoal();
            showGoalAndIncrement();
            showTodayRecord();
        });
    }

    // endregion Controller
}