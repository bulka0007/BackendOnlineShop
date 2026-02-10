package com.bulka.OnlineShop.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun getUserByLogin(login: String): User? {
        return userRepository.findByLogin(login)
    }

    @Transactional
    fun createUser(user: User): User {
        return if (userRepository.existsByLogin(user.login)) {
            throw RuntimeException("User with login ${user.login} already exists")
        } else {
            userRepository.save(user)
        }
    }

    @Transactional
    fun updateUser(id: Long, user: User): User? {
        return userRepository.findById(id).map {existingUser ->
            if (user.login != existingUser.login && userRepository.existsByLogin(user.login)) {
                throw RuntimeException("User with login ${user.login} already exists")
            }
            existingUser.login = user.login
            existingUser.firstName = user.firstName
            existingUser.lastName = user.lastName
            existingUser.password = user.password
            existingUser.role = user.role
            userRepository.save(existingUser)
        }.orElse(null)
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}