package com.example.eventtrackingapp.ViewModel

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val users = mutableMapOf<String, String>()
    private var loggedInUser: String? = null

    init {
        // Adding default user
        users["admin"] = "password123"
    }

    fun login(username: String, password: String): Boolean {
        return if (users[username] == password) {
            loggedInUser = username
            true
        } else {
            false
        }
    }

    fun createAccount(username: String, password: String): Boolean {
        return if (users.containsKey(username)) {
            false
        } else {
            users[username] = password
            true
        }
    }

    fun resetPassword(username: String, newPassword: String): Boolean {
        return if (users.containsKey(username)) {
            users[username] = newPassword
            true
        } else {
            false
        }
    }

    fun logout() {
        loggedInUser = null
    }
}
