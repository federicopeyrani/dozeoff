package com.federicopeyrani.repose.sleep.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, "app-database.db")
            .build()

    @Provides
    @Singleton
    fun provideSleepEventDao(applicationDatabase: ApplicationDatabase) =
        applicationDatabase.getSleepEventDao()
}