package com.bulka.OnlineShop.order

import com.bulka.OnlineShop.product.Product
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "order_items")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,

    @Column(nullable = false)
    var quantity: Int = 1,

    @Column(name = "price_at_order_time", nullable = false)
    var priceAtOrderTime: Double = 0.0,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)