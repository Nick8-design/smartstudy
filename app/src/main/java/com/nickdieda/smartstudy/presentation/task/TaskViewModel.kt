package com.nickdieda.smartstudy.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
   private val taskRepository: TaskRepository,
   private val subjectRepository: SubjectRepository
): ViewModel() {

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



    fun onEvent(event: TaskEvent){
        when(event){
            TaskEvent.DeleteTask -> TODO()
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

    private fun saveTask() {
        viewModelScope.launch {
            val state=_state.value
            if (state.subjectId==null||state.relatedToSubject==null){
                return@launch
            }
            taskRepository.upsertTask(
                task = Task(
                    title = state.title,
                    description = state.description,
                    dueDate = state.dueDate?: Instant.now().toEpochMilli(),
                    priority = state.priority.value,
                    relatedToSubject = state.relatedToSubject,
                    isComplete = state.isTaskComplete,
                    taskSubjectId = state.subjectId,
                    taskId = state.currentTaskId
                )
            )
        }
    }
}