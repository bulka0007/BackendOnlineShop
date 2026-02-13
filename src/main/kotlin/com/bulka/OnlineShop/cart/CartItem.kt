package com.bulka.OnlineShop.cart

import com.bulka.OnlineShop.product.Product
import jakarta.persistence.*
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators

@Entity
@Table(name = "cart_items")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class CartItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    var cart: Cart,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,

    @Column(nullable = false)
    var quantity: Int = 1,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)