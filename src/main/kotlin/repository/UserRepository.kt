package com.example.repository

import com.example.model.User
import java.util.concurrent.atomic.AtomicInteger

object UserRepository {
    private val users = mutableMapOf<Int, User>()
    private val idCounter = AtomicInteger(1)

    init {
        addUser("Alex", "alex@example.com")
        addUser("Max", "max@example.com")
    }

    fun getAllUsers(): List<User> = users.values.toList()

    fun getUserById(id: Int): User? = users[id]

    fun addUser(name: String, email: String): User {
        val id = idCounter.getAndIncrement()
        val user = User(id, name, email)
        users[id] = user
        return user
    }

    fun deleteUser(id: Int): Boolean = users.remove(id) != null

    fun existsById(id: Int): Boolean = users.containsKey(id)
}