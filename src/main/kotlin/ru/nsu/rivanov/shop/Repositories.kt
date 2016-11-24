package ru.nsu.rivanov.shop

import org.springframework.data.jpa.repository.JpaRepository


interface ProductRepository : JpaRepository<Product, Int>
interface CategoryRepository : JpaRepository<Category, Int>
interface OrderRepository : JpaRepository<Order, Int>
interface OrderItemRepository : JpaRepository<OrderItem, Int>
interface UserProfileRepository : JpaRepository<UserProfile, Int>