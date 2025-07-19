package com.nickdieda.smartstudy.presentation.domain.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nickdieda.smartstudy.presentation.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM Task where taskId = :taskId")
    suspend fun deleteTask(taskId:Int)

    @Query("DELETE FROM Task where taskSubjectId = :sujectId")
    suspend fun deleteTaskBySubjectId(sujectId:Int)

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM Task WHERE taskSubjectId = :subjectId" )
    fun getTasksForSubject(subjectId:Int): Flow<List<Task>>

    @Query("SELECT * FROM Task")
    fun getAllTasks():Flow<List<Task>>

}