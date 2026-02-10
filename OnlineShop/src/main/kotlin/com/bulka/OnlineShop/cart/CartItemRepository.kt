package com.bulka.OnlineShop.cart

import com.bulka.OnlineShop.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartItemRepository : JpaRepository<CartItem, Long> {

    fun findByCart(cart: Cart): List<CartItem>

    fun findByCartAndProduct(cart: Cart, product: Product): CartItem?

    fun deleteByCartAndProduct(cart: Cart, product: Product)

}