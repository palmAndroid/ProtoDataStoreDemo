package com.protodatastoredemo.storagemanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.protodatastoredemo.storage.User
import com.protodatastoredemo.storage.SessionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore: DataStore<User> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer,
)

class DataStoreSessionHandler (private val context: Context) : SessionHandler {

    override suspend fun setCurrentUser(userName: String?) {
        context.userDataStore.updateData {
                 it.toBuilder()
                .setUserName(userName)
                .build()
        }
    }

    override fun getCurrentUser(): Flow<User> {
        return context.userDataStore.data.map {
            it
        }
    }

    override suspend fun clear() {
        context.userDataStore.updateData {
            it.toBuilder()
                .setUserName("")
                .build()
        }
    }
}
