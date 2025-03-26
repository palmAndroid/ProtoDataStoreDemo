package com.protodatastoredemo.storage

import kotlinx.coroutines.flow.Flow

interface SessionHandler {
    suspend fun setCurrentUser(userName: String?)
    fun getCurrentUser(): Flow<User>
    suspend fun clear()
}
