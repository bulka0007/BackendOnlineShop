package com.bulka.OnlineShop.order

import com.bulka.OnlineShop.cart.Cart
import com.bulka.OnlineShop.cart.CartItem
import com.bulka.OnlineShop.cart.CartService
import com.bulka.OnlineShop.product.ProductRepository
import com.bulka.OnlineShop.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val cartService: CartService
) {

    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    fun getOrderById(id: Long): Order? {
        return orderRepository.findById(id).orElse(null)
    }

    fun getUserOrders(userId: Long): List<Order> {
        return orderRepository.findByUserId(userId)
    }

    fun getUserOrdersByStatus(userId: Long, status: OrderStatus): List<Order> {
        return orderRepository.findByUserIdAndStatus(userId, status)
    }

    @Transactional
    fun createOrderFromCart(userId: Long): Order {
        val cart = cartService.getCartByUserId(userId)
            ?: throw RuntimeException("Cart for user $userId not found")

        if (cart.items.isEmpty()) {
            throw RuntimeException("Cart is empty")
        }

        val order = Order(
            user = cart.user,
            status = OrderStatus.PENDING,
            shippingAddress = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        var totalAmount = 0.0
        for (cartItem in cart.items) {
            val orderItem = OrderItem(
                order = order,
                product = cartItem.product,
                quantity = cartItem.quantity,
                priceAtOrderTime = cartItem.product.price,
                createdAt = LocalDateTime.now()
            )
            order.items.add(orderItem)
            totalAmount += cartItem.product.price * cartItem.quantity
        }

        order.totalAmount = totalAmount

        val savedOrder = orderRepository.save(order)

        cartService.clearCart(userId)

        return savedOrder
    }

    @Transactional
    fun updateOrderStatus(orderId: Long, status: OrderStatus): Order? {
        val order = orderRepository.findById(orderId).orElse(null)
            ?: return null

        order.status = status
        order.updatedAt = LocalDateTime.now()

        return orderRepository.save(order)
    }

    @Transactional
    fun deleteOrder(orderId: Long): Boolean {
        if (!orderRepository.existsById(orderId)) {
            return false
        }

        orderRepository.deleteById(orderId)
        return true
    }
}