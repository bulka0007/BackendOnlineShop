package com.bulka.OnlineShop.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByLogin(login: String): User?

    fun existsByLogin(login: String): Boolean

}