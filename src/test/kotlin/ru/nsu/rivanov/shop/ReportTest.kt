package ru.nsu.rivanov.shop

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class ReportTest {
    @Test fun totalCountSummarized() {
        val reportBuilder = ReportBuilder(mock<OrderRepository> {
            on { findByOrderDateBetween(any(), any()) } doReturn listOf(
                    Order(mutableListOf(
                            OrderItem(product = Product("p1", 12), count = 2),
                            OrderItem(product = Product("p2", 3), count = 1)
                    )
                    )
            )
        })
        assertThat(reportBuilder.createReport(Date(), Date()).totalRevenues).isEqualTo(27)
        assertThat(reportBuilder.createReport(Date(), Date()).soldTotally).isEqualTo(3)
    }
}