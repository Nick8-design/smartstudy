package com.nickdieda.smartstudy.presentation.subject

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import com.nickdieda.smartstudy.presentation.navArgs
import com.nickdieda.smartstudy.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private  val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private  val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val navArgs: SubjectScreenNavArgs = savedStateHandle.navArgs()
    private val _state = MutableStateFlow(SubjectState())

    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectsId),
        taskRepository.getCompletedTasksForSubject(navArgs.subjectsId),
        sessionRepository.getSessionsForSubject(navArgs.subjectsId),
        sessionRepository.getTotalSessionDurationBySubjectId(navArgs.subjectsId)
    ) { state, upcommingTasks, completeTasks, recentSessions, totalSessionDurationsBySubject ->
        state.copy(
            upcomingTasks = upcommingTasks,
            completedTasks = completeTasks,
            recentSessions = recentSessions,
            studiedHours = totalSessionDurationsBySubject.toHours(),
        )

    }
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SubjectState()
        )


    fun onEvent(event: SubjectEvent) {
        when (event) {
            SubjectEvent.DeleteSession -> TODO()
            SubjectEvent.DeleteSubject -> TODO()
            SubjectEvent.UpdateSubject -> TODO()
            is SubjectEvent.onDeleteSessionButtonClick -> TODO()
            is SubjectEvent.onGoalStudyHoursChange -> TODO()
            is SubjectEvent.onSubjectCardColorChange -> TODO()
            is SubjectEvent.onSubjectNameChange -> TODO()
            is SubjectEvent.onTaskIsCompleteChange -> TODO()
        }
    }

}