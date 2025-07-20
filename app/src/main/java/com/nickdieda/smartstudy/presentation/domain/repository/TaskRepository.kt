package com.nickdieda.smartstudy.presentation.domain.repository

import androidx.room.Query
import com.nickdieda.smartstudy.presentation.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun upsertTask(task: Task)


    suspend fun deleteTask(taskId:Int)


    suspend fun deleteTaskBySubjectId(sujectId:Int)


    suspend fun getTaskById(taskId:Int)

    fun getCompletedTasksForSubject(subjectId:Int): Flow<List<Task>>

    fun getUpcomingTasksForSubject(subjectId:Int): Flow<List<Task>>

    fun getAllTasks():Flow<List<Task>>
}