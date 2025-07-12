package com.nickdieda.smartstudy.presentation.domain.di

import android.app.Application
import androidx.room.Room
import com.nickdieda.smartstudy.presentation.domain.data.local.AppDatabase
import com.nickdieda.smartstudy.presentation.domain.data.local.SessionDao
import com.nickdieda.smartstudy.presentation.domain.data.local.SubjectDao
import com.nickdieda.smartstudy.presentation.domain.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

   @Provides
   @Singleton
    fun provideDatabase(
        application: Application
    ): AppDatabase{
        return Room
            .databaseBuilder(
            application,
                AppDatabase::class.java,
                "smartstudy.db"
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDatabase): SubjectDao{
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase): SessionDao{
        return database.sessionDao()
    }






}