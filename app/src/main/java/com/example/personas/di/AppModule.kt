package com.example.personas.di

import android.content.Context
import androidx.room.Room
import com.example.personas.data.PersonasDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesTicketDatabase(@ApplicationContext appContext: Context): PersonasDb =
        Room.databaseBuilder(
            appContext,
            PersonasDb::class.java,
            "PersonasDb.db")
            .fallbackToDestructiveMigration()
            .build()
}