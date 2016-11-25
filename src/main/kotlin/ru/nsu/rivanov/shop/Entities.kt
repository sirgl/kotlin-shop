package ru.nsu.rivanov.shop

import java.math.BigDecimal
import javax.persistence.*

@Entity data class Category(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Int? = null,
        @OneToOne var parentCategory: Category? = null,
        @Column var name: String = "",
        @Column var description: String = ""
)

@Entity data class Product(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Int? = null,
        @Column var name: String,
        @Column var price: BigDecimal,
        @Column var description: String,
        @Column var count: Int,
        @OneToOne var category: Category,
        @Column var imageUrl: String?,
        @ElementCollection var attributes: MutableList<String>
)

@Entity data class Order(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: String,
        @OneToMany(cascade = arrayOf(CascadeType.ALL)) var items: MutableList<OrderItem>,
        @Column var shippingAdress: String,
        @Enumerated(EnumType.STRING) var paymentMethod: PaymentMethod,
        @Enumerated(EnumType.STRING) var shippingMethod: ShippingMethod
)

enum class ShippingMethod {
    PICKUP,
    RUSSIAN_POST,
    EMS,
    DHL
}

enum class PaymentMethod {
    PAYPAL,
    CASH,
    DEBIT_CARD,
    YANDEX_MONEY
}

@Entity data class OrderItem(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Int? = null,
        @OneToOne var product: Product,
        @Column var count: Int
)

@Entity data class UserProfile(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
        @OneToMany var orders: MutableList<Order>,
        @Column var name: String,
        @Column var phone: String,
        @Column var mail: String
)