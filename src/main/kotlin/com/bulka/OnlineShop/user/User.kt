package com.bulka.OnlineShop.user

import jakarta.persistence.*

enum class UserRole {
    USER,
    ADMIN
}

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var login: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER
)