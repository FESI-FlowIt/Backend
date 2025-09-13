package com.fesi.flowit.goal.search

enum class GoalSortCriteria(
    private val description: String
) {
    LATEST("생성일 기준"),
    DUE_DATE("마감일 기준"),

    ;
}