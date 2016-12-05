package ru.nsu.rivanov.shop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * Rest controller to get report
 */
@RestController
class ReportController {
    @Autowired lateinit var reportBuilder: ReportBuilder

    /**
     * GET method to get report
     */
    @GetMapping("report")
    public fun createReport(@RequestParam startingDate: Date, @RequestParam endingDate: Date): IntervalResponse {
        return reportBuilder.createReport(startingDate, endingDate)
    }
}

/**
 * Builder that analyzes data from repository and assemblies report
 */
@Component
open class ReportBuilder {
    @Autowired lateinit var orderRepository: OrderRepository

    constructor(orderRepository: OrderRepository) {
        this.orderRepository = orderRepository
    }


    fun createReport(startingDate: Date, endingDate: Date): IntervalResponse {
        val orders = orderRepository.findByOrderDateBetween(startingDate, endingDate)
        val totalCash = orders.flatMap { it.items }
                .map { it.product?.price?.times(it.count) }
                .reduce { c1, c2 -> c1?.plus(c2 ?: 0) }
        val categoryToCount = orders.flatMap { it.items }
                .groupBy { it.product?.category }
                .map { Pair(it.key?.name?:"", it.value.map { it.count }.reduce { a, b -> a + b }) }
                .toMap()
        val totalCount = categoryToCount.values.reduce { a, b -> a + b }
        return IntervalResponse(orders, totalCash?:0, totalCount, categoryToCount)
    }
}

data class IntervalResponse(
        var orders: List<Order>,
        var totalRevenues: Int,
        var soldTotally: Int,
        var soldByCategories: Map<String, Int>
)