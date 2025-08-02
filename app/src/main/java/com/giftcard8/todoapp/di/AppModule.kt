package com.giftcard8.todoapp.di

import android.content.Context
import androidx.room.Room
import com.giftcard8.todoapp.db.UserDatabase
import com.giftcard8.todoapp.db.task.TaskDao
import com.giftcard8.todoapp.db.user.UserDao
import com.giftcard8.todoapp.utils.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
    ): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUserDatabase(
        @ApplicationContext context: Context,
    ): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_db",
        ).build()
    }

    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @Provides
    fun provideTaskDao(db: UserDatabase): TaskDao = db.taskDao()
}
