package com.bulka.OnlineShop.product

import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,

    var description: String? = null,

    var price: Double,

    var stock: Int = 0
)