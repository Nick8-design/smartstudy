package com.nickdieda.smartstudy.presentation.task

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import com.nickdieda.smartstudy.presentation.navArgs

import com.nickdieda.smartstudy.util.Priority
import com.nickdieda.smartstudy.util.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
   private val taskRepository: TaskRepository,
   private val subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val navArgs: TaskScreenNavArgs=savedStateHandle.navArgs()
    private val _state= MutableStateFlow(TaskState())

    val state= combine(
        _state,
        subjectRepository.getAllSubjects()

    ){ state,subjects->
        state.copy(subjects=subjects)

    }.stateIn(
        scope=viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TaskState()

    )

    private  val _snackbarEventFlow= MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow=_snackbarEventFlow.asSharedFlow()

init {
    fetchTasks()
    fetchSubject()
}
    fun onEvent(event: TaskEvent){
        when(event){
            TaskEvent.DeleteTask -> deleteTask()
            is TaskEvent.OnDateChange -> {
                _state.update {
                    it.copy(dueDate = event.date)
                }
            }
            is TaskEvent.OnDescriptionChange ->  {
                _state.update {
                    it.copy(description = event.description)
                }
            }
            TaskEvent.OnIsCompleteChange ->  {
                _state.update {
                    it.copy(isTaskComplete = !_state.value.isTaskComplete)
                }
            }
            is TaskEvent.OnPriorityChange ->  {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }
            is TaskEvent.OnRelatedSubjectSelect ->  {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }
            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }
            TaskEvent.SaveTask -> saveTask()
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            try {
                val currentTaskId = state.value.currentTaskId;
                if (currentTaskId != null) {
                    withContext(Dispatchers.IO) {
                        taskRepository.deleteTask(currentTaskId)
                    }
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(
                            message = "Task deleted successfully",
                            SnackbarDuration.Long
                        )
                    )
                    _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)

                } else {
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(
                            message = "No task to delete",
                            SnackbarDuration.Long
                        )
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Coundn't delete the task : ${e.message}",
                        SnackbarDuration.Long
                    )
                )

            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state=_state.value
            if (state.subjectId==null||state.relatedToSubject==null){

                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Please select subject related to task"
                    )
                )
                return@launch
            }
            try {


                taskRepository.upsertTask(
                    task = Task(
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate ?: Instant.now().toEpochMilli(),
                        priority = state.priority.value,
                        relatedToSubject = state.relatedToSubject,
                        isComplete = state.isTaskComplete,
                        taskSubjectId = state.subjectId,
                        taskId = state.currentTaskId
                    )
                )
             _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Task saved successfully"
                    )
                    )
                _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)

        }catch (e: Exception){
            _snackbarEventFlow.emit(
                SnackbarEvent.ShowSnackbar(
                    message = "Coundnt save the task : ${e.message}",
                    SnackbarDuration.Long
                )
            )
        }




        }
    }

    private  fun  fetchTasks(){
        viewModelScope.launch {
            navArgs.taskId?.let { id->
                taskRepository.getTaskById(id)?.let {task->
_state.update {
    it.copy(
        title = task.title,
        description = task.description,
        dueDate = task.dueDate,
        isTaskComplete = task.isComplete,
        relatedToSubject = task.relatedToSubject,
        priority = Priority.frromInt(task.priority),
        subjectId = task.taskSubjectId,
        currentTaskId = task.taskId
    )
}
                }
            }
        }
    }


    private  fun  fetchSubject() {
        viewModelScope.launch {
            navArgs.subjectId?.let { id ->
                subjectRepository.getSubjectById(id)?.let { sub->
                    _state.update {
                        it.copy(
                            subjectId = sub.subjectId,
                            relatedToSubject = sub.name
                        )
                    }
                }

            }
        }
    }

}