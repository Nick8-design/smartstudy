package com.nickdieda.smartstudy.presentation.domain.di

import com.nickdieda.smartstudy.presentation.domain.data.repository.SessionRepositoryImp
import com.nickdieda.smartstudy.presentation.domain.data.repository.SubjectRepositoryImp
import com.nickdieda.smartstudy.presentation.domain.data.repository.TaskRepositoryImp
import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        imp: SubjectRepositoryImp
    ): SubjectRepository


    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        imp: TaskRepositoryImp
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        imp: SessionRepositoryImp
    ): SessionRepository


}