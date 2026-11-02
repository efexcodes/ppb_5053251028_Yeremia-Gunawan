package com.example.mvvmarsitekturlogin.data.repository

import com.example.mvvmarsitekturlogin.data.local.dao.UserDao
import com.example.mvvmarsitekturlogin.data.local.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) = userDao.insert(user)

    suspend fun login(username: String, password: String): User? =
        userDao.login(username, password)
}
