package com.fesi.flowit.heatmap.vo

enum class HeatmapInsightMsg(
    val format: String,
    private val description: String
) {
    /* 주간 */
    WEEKLY_GOLDEN_TIME("이번 주 골든 타임 %d회 달성!", "주간 골든 타임 (횟수)"),
    WEEKLY_HIGHEST_DAY("이번 주는 %s이 %s으로 최고!", "주간 최고 기록 (요일, 시간)"),

    /* 월간 */
    MONTHLY_HIGHEST_WEEK("이번 달은 %s주 차를 열심히 보냈네요!", "월간 최고 기록 (주)")
}