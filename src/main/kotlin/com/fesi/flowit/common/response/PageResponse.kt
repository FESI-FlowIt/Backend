package com.fesi.flowit.common.response

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val contents: List<T>,
    val page: Int,
    val size: Int,
    val totalPage: Int,
    val totalElement: Long,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasNext: Boolean,
    val hasPrev: Boolean
) {
    companion object {
        fun <T> fromPage(page: Page<T>): PageResponse<T> {
            return PageResponse(
                contents = page.content,
                page = page.number + 1,
                size = page.size,
                totalPage = page.totalPages,
                totalElement = page.totalElements,
                isFirst = page.isFirst,
                isLast = page.isLast,
                hasNext = page.hasNext(),
                hasPrev = page.hasPrevious()
            )
        }

        fun <T, E> fromPageWithContents(contents: List<T>, page: Page<E>): PageResponse<T> {
            return PageResponse(
                contents = contents,
                page = page.number + 1,
                size = page.size,
                totalPage = page.totalPages,
                totalElement = page.totalElements,
                isFirst = page.isFirst,
                isLast = page.isLast,
                hasNext = page.hasNext(),
                hasPrev = page.hasPrevious()
            )
        }
    }
}