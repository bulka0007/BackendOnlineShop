package com.bulka.OnlineShop.order

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun getAllOrders(): List<Order> {
        return orderService.getAllOrders()
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<Order> {
        val order = orderService.getOrderById(id)
        return if (order != null) {
            ResponseEntity.ok(order)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    fun getUserOrders(@PathVariable userId: Long): List<Order> {
        return orderService.getUserOrders(userId)
    }

    @GetMapping("/user/{userId}/status/{status}")
    fun getUserOrdersByStatus(
        @PathVariable userId: Long,
        @PathVariable status: OrderStatus
    ): List<Order> {
        return orderService.getUserOrdersByStatus(userId, status)
    }

    @PostMapping("/user/{userId}/checkout")
    fun createOrderFromCart(@PathVariable userId: Long): ResponseEntity<*> {
        return try {
            val order = orderService.createOrderFromCart(userId)
            ResponseEntity.status(201).body(order)
        } catch (e: RuntimeException) {
            when {
                e.message?.contains("not found") == true ->
                    ResponseEntity.status(404).body("Cart not found")
                e.message?.contains("empty") == true ->
                    ResponseEntity.status(400).body("Cart is empty")
                else ->
                    ResponseEntity.status(500).body(e.message ?: "Unknown error")
            }
        }
    }

    @PutMapping("/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @RequestParam status: OrderStatus
    ): ResponseEntity<Order> {
        val updatedOrder = orderService.updateOrderStatus(id, status)
        return if (updatedOrder != null) {
            ResponseEntity.ok(updatedOrder)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Unit> {
        val deleted = orderService.deleteOrder(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}