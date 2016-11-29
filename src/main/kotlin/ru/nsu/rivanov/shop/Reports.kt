package ru.nsu.rivanov.shop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ReportController {
    @Autowired lateinit var reportBuilder: ReportBuilder

    @GetMapping("report")
    public fun createReport(@RequestParam startingDate: Date, @RequestParam endingDate: Date): IntervalResponse {
        return reportBuilder.createReport(startingDate, endingDate)
    }
}

@Component
open class ReportBuilder {
    @Autowired lateinit var orderRepository: OrderRepository

    fun createReport(startingDate: Date, endingDate: Date): IntervalResponse {
        val orders = orderRepository.findByOrderDateBetween(startingDate, endingDate)
        val totalCash = orders.flatMap { it.items }
                .map { it.product?.price?.times(it.count) }
                .reduce { c1, c2 -> c1?.plus(c2 ?: 0f) }
        val categoryToCount = orders.flatMap { it.items }
                .groupBy { it.product?.category }
                .map { Pair(it.key?.name?:"", it.value.map { it.count }.reduce { a, b -> a + b }) }
                .toMap()
        val totalCount = categoryToCount.values.reduce { a, b -> a + b }
        return IntervalResponse(orders, totalCash?:0f, totalCount, categoryToCount)
    }
}

data class IntervalResponse(
        var orders: List<Order>,
        var totalRevenues: Float,
        var soldTotally: Int,
        var soldByCategories: Map<String, Int>
)