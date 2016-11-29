package ru.nsu.rivanov.shop

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface ProductRepository : JpaRepository<Product, Int>
interface CategoryRepository : JpaRepository<Category, Int>
interface OrderItemRepository : JpaRepository<OrderItem, Int>
interface UserProfileRepository : JpaRepository<UserProfile, Int>
interface OrderRepository : JpaRepository<Order, Int> {
    fun findByOrderDateBetween(startingDate: Date, endingDate: Date) : List<Order>
}
