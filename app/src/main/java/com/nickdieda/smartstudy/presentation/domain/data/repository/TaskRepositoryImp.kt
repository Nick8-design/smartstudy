package com.nickdieda.smartstudy.presentation.domain.data.repository

import com.nickdieda.smartstudy.presentation.domain.data.local.TaskDao
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getCompletedTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId)
            .map{tasks->tasks.filter { it.isComplete }}
            .map { tasks->sortTasks(tasks) }
    }

    override fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId)
            .map{tasks->tasks.filter { it.isComplete.not() }}
            .map { tasks->sortTasks(tasks) }
    }

    override fun getAllTasks(): Flow<List<Task>> {
       return  taskDao.getAllTasks()
           .map{
               tasks->tasks.filter{it.isComplete.not()}

           }
           .map { tasks->sortTasks(tasks) }
    }
    private fun sortTasks(tasks:List<Task>):    List<Task>  {
        return  tasks.sortedWith (
            compareBy<Task>{
                it.dueDate
            }.thenByDescending { it.priority }
        )
    }
}