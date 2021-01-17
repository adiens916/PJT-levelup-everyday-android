package com.example.everydaylevelup.model;

public class TimeRecord {
    private long yesterdayGoal;
    private long increment;
    private long incrementUnit;
    private long todayGoal;
    private long todayRecord;
    private long percentage;
    private long editAmount;

    private long startValue;
    private long lastValue;
    private long difference;
    private boolean recordingState;

    public TimeRecord() {
        init();
    }

    private void init() {
        yesterdayGoal = 0;
        increment = 0;
        incrementUnit = -1;
        todayGoal = 0;
        todayRecord = 0;
        percentage = 0;
        editAmount = 0;

        startValue = 0;
        lastValue = 0;
        difference = 0;
        recordingState = false;
    }

    /* 어제 달성치가 0인 경우 = 새로 시작 -> 측정해야 함 */
    public boolean isNewSession() {
        if (yesterdayGoal == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOnRecording() {
        return recordingState;
    }

    public long calcDifference() {
        difference = lastValue - startValue;
        return difference;
    }

    public void addDifferenceToTodayRecord() {
        todayRecord += difference;
    }

    /* 가감치는 일단 -1분씩 줄이기 */
    /* -> 성공실패 여부와 초과미만치에 따라서 가감치 결정 */
    /* <- 최소단위(unit) <- 유형에 따라 지정해주기 */



    /* 마지막 시간 = 항상 현재 시간 가져오기 */
    /* -> 달성도를 바로 바로 갱신? */
    /* 마지막 시간 - 시작 시간 = 기간 */



    /* 중지 버튼 누름 -> 예/아니오 알림 대화상자 처리
     * 아니오 -> 계속 이어짐
     * 예 -> 처음과 마지막 시간 사이 간격 계산 */

    /* 시간 간격을 기존의 간격에 추가하기 */

    /* 처음과 마지막 시간 초기화, 기록 상태 0으로 바꾸기 */


    /* 오늘 달성량 직접 수정 */

    /* 목표 도달 시 알림(notification) */



    /* 현재 날짜를 받아와서 오늘 날짜와 같은지 비교 -> 달라질 때만 기록 바뀜 */
    /* : 일단 수동으로 날짜 넘기는 버튼을 추가해놓을까 */
    /* : "이대로 오늘을 마치시겠습니까?" */
    /* -> 그러면 두구두구둔...! 축하합니다! 레벨업! */
    /* 만약 개수를 카운팅하는 경우엔, 목표 달성하자마자 바로 축하 메시지 */


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


    public long getYesterdayGoal() { return yesterdayGoal; }

    public long getIncrement() { return increment; }

    public long getIncrementUnit() { return incrementUnit; }

    public long getTodayGoal() { return todayGoal; }

    public long getTodayRecord() { return todayRecord; }

    public long getPercentage() { return percentage; }

    public long getStartValue() { return startValue; }

    public long getLastValue() { return lastValue; }

    public long getDifference() { return difference; }

    public void setTodayRecord(long todayRecord) { this.todayRecord = todayRecord; }

    public void setStartValue(long startValue) { this.startValue = startValue; }

    public void setLastValue(long lastValue) { this.lastValue = lastValue; }

    public void setEditAmount(long editAmount) { this.editAmount = editAmount; }

    public void setRecordingState(boolean state) { recordingState = state; }


}
