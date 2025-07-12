package com.nickdieda.smartstudy.presentation.domain.data.repository

import com.nickdieda.smartstudy.presentation.domain.data.local.TaskDao
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImp @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun upsertTask(task: Task) {
       taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskBySubjectId(sujectId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: Int) {
        TODO("Not yet implemented")
    }

    override fun getTasksForSubject(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }
}