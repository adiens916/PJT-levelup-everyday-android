package com.example.everydaylevelup.model;

public class TimeRecord {
    private long yesterdayGoal;
    private long increment;
    private long incrementUnit;
    private long todayGoal;
    private long todayRecord;
    private double todayPercentage;
    private long editAmount;

    private long startValue;
    private long lastValue;
    private long difference;
    private boolean recordingState;

    private long beforeYesterdayRecord;
    private long yesterdayRecord;
    private long tomorrowGoal;
    private double beforeYesterdayPercentage;
    private double yesterdayPercentage;
    private final double weight;
    private final double standard;

    public TimeRecord() {
        yesterdayGoal = 1000 * 60 * 60 * 3;
        increment = 1000 * 60;
        incrementUnit = 1000 * 5;
        todayGoal = 0;
        todayRecord = 0;
        todayPercentage = 0.0;
        editAmount = 0;

        startValue = 0;
        lastValue = 0;
        difference = 0;
        recordingState = false;

        beforeYesterdayRecord = 0;
        yesterdayRecord = 0;
        beforeYesterdayPercentage = 0.0;
        yesterdayPercentage = 0.0;
        weight = 0.7;
        standard = 120;
    }

    // region Getter & Setter

    public long getYesterdayGoal() { return yesterdayGoal; }

    public long getIncrement() { return increment; }

    public long getTodayGoal() {
        todayGoal = yesterdayGoal + increment;
        return todayGoal;
    }

    public long getTodayRecord() { return todayRecord; }

    public double getTodayPercentage() {
        try {
            todayPercentage = (double)(todayRecord + difference) / todayGoal * 100;
            return todayPercentage;
        } catch (ArithmeticException e) {
            return 0;
        }
    }

    public long getStartValue() { return startValue; }

    public long getLastValue() { return lastValue; }

    public long getDifference() { return difference; }

    public void setTodayRecord(long todayRecord) { this.todayRecord = todayRecord; }

    public void setStartValue(long startValue) { this.startValue = startValue; }

    public void setLastValue(long lastValue) { this.lastValue = lastValue; }

    public void setEditAmount(long editAmount) { this.editAmount = editAmount; }

    public void setRecordingState(boolean state) { recordingState = state; }

    // endregion Getter & Setter

    /* 어제 달성치가 0인 경우 = 새로 시작 -> 측정해야 함 */
    public boolean isNewSession() {
        return yesterdayGoal == 0;
    }

    public boolean isOnRecording() {
        return recordingState;
    }

    public void initDifference() {
        difference = 0;
    }

    public void calcDifference() {
        difference = lastValue - startValue;
    }

    public void addDifferenceToTodayRecord() {
        todayRecord += difference;
    }

    /* 시간 안 쓴 만큼 포인트 추가, 포인트에 따라 칭호 바뀜 */
    /* <- 몇 분에 몇 포인트인지 정해야 함 */
    /* -> 그런데 시간이 줄어들수록 오히려 벌 수 있는 포인트가 줄어듦? */
    /* -> 차라리 퍼센트로 따져야겠다. */
    /* 만약 2시간에서 1시간으로 줄어든 경우,
     * 2시간 전부 안 썼을 때 100 포인트였다면 <- 100%
     * 1시간 전부 안 썼을 땐 100 / (1/2) = 200 포인트 */

    /* 아, 이 포인트라는 게 EXP는 아닐까?
     * 그리고 제대로 할 때마다 100 포인트 = 1업이고...
     * 초과 달성시에는 추가 EXP를 주는 식으로 폭업하는 거지
     * 단, 달성 못 할시에는 포인트를 "깎자"! -> 레벨 감소?
     * 아니면 20% 못 채우면 감소, 20% 이상부터 조금씩 주는 형태로? */

    public void updateGoal() {
        if (isTripleSuccess() || isDoubleFailure()) {
            tomorrowGoal = getWeightedAverage();
            shiftRecords();
            increment = todayGoal - yesterdayGoal;
        } else {
            increment = getIncrementBySuccess();
            tomorrowGoal = todayGoal + increment;
            shiftRecords();
        }
    }

    private boolean isTripleSuccess() {
        return beforeYesterdayPercentage >= standard &&
                yesterdayPercentage >= standard &&
                todayPercentage >= standard;
    }

    private boolean isDoubleFailure() {
        return yesterdayPercentage < 100 && todayPercentage < 100;
    }

    private long getWeightedAverage() {
        double prevAverage;
        if (isTripleSuccess()) {
            prevAverage = weight * yesterdayRecord + (1 - weight) * beforeYesterdayRecord;
        } else {
            prevAverage = yesterdayRecord;
        }
        double average = weight * todayRecord + (1 - weight) * prevAverage;
        return (long)average;
    }

    /* 가감치는 일단 -1분씩 줄이기 */
    /* -> 성공실패 여부와 초과미만치에 따라서 가감치 결정 */
    /* <- 최소단위(unit) <- 유형에 따라 지정해주기 */
    private long getIncrementBySuccess() {
        if (todayPercentage >= 100) {
            return incrementUnit;
        } else {
            return -incrementUnit;
        }
    }

    private void shiftRecords() {
        beforeYesterdayRecord = yesterdayRecord;
        yesterdayRecord = todayRecord;
        todayRecord = 0;

        beforeYesterdayPercentage = yesterdayPercentage;
        yesterdayPercentage = todayPercentage;
        todayPercentage = 0.0;

        yesterdayGoal = todayGoal;
        todayGoal = tomorrowGoal;
    }
}
