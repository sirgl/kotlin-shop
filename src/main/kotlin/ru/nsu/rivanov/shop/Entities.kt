package ru.nsu.rivanov.shop

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity data class Category(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Int? = null,
        @OneToOne var parentCategory: Category? = null,
        @Column var name: String = "",
        @Column var description: String = ""
) {
    constructor() : this(null, null, "", "")

    override fun toString() = name
}

@Entity data class Product(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Int?,
        @Column var name: String,
        @Column var price: Int,
        @Column var description: String,
        @Column var count: Int,
        @OneToOne var category: Category?,
        @Column var imageUrl: String?,
        @ElementCollection var attributes: MutableList<String>
) {
    constructor() : this(null, "", 0, "", 0, null, null, mutableListOf())
    constructor(name: String, price: Int) : this(null, name, price, "", 0, null, null, mutableListOf())
}

@Table(name = "orders")
@Entity data class Order(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: String?,
        @OneToMany(cascade = arrayOf(CascadeType.ALL)) var items: MutableList<OrderItem>,
        @Column var shippingAdress: String,
        @Column var orderDate: Date,
        @Enumerated(EnumType.STRING) var paymentMethod: PaymentMethod,
        @Enumerated(EnumType.STRING) var shippingMethod: ShippingMethod
) {
    constructor() : this(null, mutableListOf(), "", Date(), PaymentMethod.DEBIT_CARD, ShippingMethod.PICKUP)
    constructor(items: MutableList<OrderItem>) : this(null, items, "", Date(), PaymentMethod.CASH, ShippingMethod.DHL)
}

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
        @OneToOne var product: Product?,
        @Column var count: Int
) {
    constructor() : this(null, null, 0)
}

@Entity data class UserProfile(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
        @OneToMany var orders: MutableList<Order>,
        @Column var name: String,
        @Column var phone: String,
        @Column var mail: String
) {
    constructor() : this(null, mutableListOf(), "", "", "")
}