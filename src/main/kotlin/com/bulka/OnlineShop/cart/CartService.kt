package com.bulka.OnlineShop.cart

import com.bulka.OnlineShop.product.ProductRepository
import com.bulka.OnlineShop.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {

    fun getCartByUserId(userId: Long): Cart? {
        return cartRepository.findByUserId(userId)
    }

    @Transactional
    fun getOrCreateCart(userId: Long): Cart {
        val existingCart = cartRepository.findByUserId(userId)

        if (existingCart != null) {
            return existingCart
        }

       val user = userRepository.findById(userId).orElse(null)
            ?: throw RuntimeException("User with id $userId not found")

        val cart = Cart(user = user)
        return cartRepository.save(cart)
    }

    @Transactional
    fun addToCart(userId: Long, productId: Long, quantity: Int = 1): Cart {

        val cart = getOrCreateCart(userId)

        val product = productRepository.findById(productId).orElse(null)
            ?: throw RuntimeException("Product with id $productId not found")

        val existingItem = cartItemRepository.findByCartAndProduct(cart, product)

        if (existingItem != null) {
            existingItem.quantity += quantity
            cartItemRepository.save(existingItem)
        } else {
            val newItem = CartItem(
                cart = cart,
                product = product,
                quantity = quantity
            )
            cartItemRepository.save(newItem)
            cart.items.add(newItem)
        }

        return cart
    }

    @Transactional
    fun updateCartItemQuantity(cartItemId: Long, quantity: Int): CartItem? {

        val cartItem = cartItemRepository.findById(cartItemId).orElse(null)
            ?: return null

        cartItem.quantity = quantity

        return cartItemRepository.save(cartItem)
    }

    @Transactional
    fun removeFromCart(cartItemId: Long): Boolean {

        if (!cartItemRepository.existsById(cartItemId)) {
            return false
        }

        cartItemRepository.deleteById(cartItemId)

        return true
    }

    @Transactional
    fun clearCart(userId: Long): Boolean {

        val cart = cartRepository.findByUserId(userId)
            ?: return false

        cartItemRepository.deleteAll(cart.items)

        cart.items.clear()

        return true
    }
}