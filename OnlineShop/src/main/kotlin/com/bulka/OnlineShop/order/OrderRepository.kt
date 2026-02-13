package com.bulka.OnlineShop.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByUserId(userId: Long): List<Order>

    fun findByUserIdAndStatus(userId: Long, status: OrderStatus): List<Order>

    fun findByStatus(status: OrderStatus): List<Order>

    fun existsByUserId(userId: Long): Boolean
}