package com.bulka.OnlineShop.cart

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(private val cartService: CartService) {

    @GetMapping("/user/{userId}")
    fun getCartByUserId(@PathVariable userId: Long): ResponseEntity<Cart> {
        val cart = cartService.getCartByUserId(userId)
        return if (cart != null) {
            ResponseEntity.ok(cart)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/user/{userId}")
    fun createCartForUser(@PathVariable userId: Long): ResponseEntity<Cart> {
        return try {
            val cart = cartService.createCartForUser(userId)
            ResponseEntity.status(201).body(cart)
        } catch (e: RuntimeException) {
            if (e.message?.contains("not found") == true) {
                ResponseEntity.notFound().build()
            } else if (e.message?.contains("already exists") == true) {
                ResponseEntity.status(409).build()
            } else {
                throw e
            }
        }
    }

    @PostMapping("/user/{userId}/items")
    fun addToCart(
        @PathVariable userId: Long,
        @RequestParam productId: Long,
        @RequestParam(required = false, defaultValue = "1") quantity: Int
    ): ResponseEntity<Cart> {
        return try {
            val cart = cartService.addToCart(userId, productId, quantity)
            ResponseEntity.ok(cart)
        } catch (e: RuntimeException) {
            if (e.message?.contains("not found") == true) {
                ResponseEntity.notFound().build()
            } else {
                throw e
            }
        }
    }

    @PutMapping("/items/{cartItemId}")
    fun updateCartItemQuantity(
        @PathVariable cartItemId: Long,
        @RequestParam quantity: Int
    ): ResponseEntity<CartItem> {
        val updatedItem = cartService.updateCartItemQuantity(cartItemId, quantity)
        return if (updatedItem != null) {
            ResponseEntity.ok(updatedItem)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/items/{cartItemId}")
    fun removeFromCart(@PathVariable cartItemId: Long): ResponseEntity<Unit> {
        val removed = cartService.removeFromCart(cartItemId)
        return if (removed) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/user/{userId}")
    fun clearCart(@PathVariable userId: Long): ResponseEntity<Unit> {
        val cleared = cartService.clearCart(userId)
        return if (cleared) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

}