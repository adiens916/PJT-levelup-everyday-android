package com.example.everydaylevelup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String _fileName = "sleep";
    TextView _yesterdayGoalView;
    TextView _incrementView;
    TextView _todayGoalView;
    TextView _todayRecordView;
    TextView _percentView;
    Button _recordButton;
    EditText _startInput;
    EditText _endInput;

    Thread progressWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        /* 뷰에 보여주기 */
        showTodayObjective();
        showTodayAchievement();
        showProgress();
        continueProgress();

        listenToStart();
        listenToStop();
        listenToEndDay();

        progressWatcher = new Thread();
        // progressWatcher.start();

        /* 위젯으로 시작, 정지, 알림 기능 */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();

        // progressWatcher.interrupt();
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences(_fileName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        String startTime = _startInput.getText().toString();
        editor.putString("startTime", startTime);

        String endTime = _endInput.getText().toString();
        editor.putString("endTime", endTime);


        // 저장
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences(_fileName, 0);
        String startTime = sharedPref.getString("startTime", "");



    }

    private void showTodayObjective() {

    }

    private void showTodayAchievement() {

    }

    private void showProgress() {

    }

    private void continueProgress() {
        /* 현재 기록 중인지 아닌지 따지기 */
        /* 기록 상태에 따라 버튼 변경
         * 데이터는 그대로 불러오기 <- 중지할 때 초기화해주니까 괜찮음
         */


        /* 기록 상태가 아니면 시작 버튼 보이기 */


        /* 기록 상태인 경우 시작 버튼 대신 취소 버튼
         */

    }

    private void listenToStart() {

    }

    private void listenToStop() {

    }

    private void listenToEndDay() {

    }
}