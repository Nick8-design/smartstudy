package com.nickdieda.smartstudy.presentation.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import com.nickdieda.smartstudy.util.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private  val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private  val taskRepository: TaskRepository
): ViewModel() {


    //data given to the screen
    private val _state = MutableStateFlow(DashboardState())

    val state=combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionDuration()


    ){state,subjectCount,goalHours,subjects,totalSessionDuration ->

        state.copy(

//            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalSubjectCount = subjectCount,
            totalStudiedHours = totalSessionDuration.toFloat()

        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        DashboardState()
    )



    val tasks: StateFlow<List<Task>> =taskRepository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            emptyList()
        )



    val recentSessions: StateFlow<List<Session>> =sessionRepository.getAllSssions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            emptyList()
        )



    private  val _snackbarEventFlow= MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow=_snackbarEventFlow.asSharedFlow()



    //actions performed by the user to the screen
    fun onEvent(event: DashboardEvent){
        when(event){
            DashboardEvent.DeleteSession -> {}
            DashboardEvent.SaveSubject -> saveSubject()

            is DashboardEvent.onDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }
            is DashboardEvent.onGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }
            is DashboardEvent.onSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.colors
                    )
                }
            }
            is DashboardEvent.onSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }
            is DashboardEvent.onTaskIsCompleteChange -> {
//                _state.update {
//                    it.copy(
//                        task = event.task
//                    )
//                }
            }
        }
    }





    private fun saveSubject() {

        viewModelScope.launch{
            try {


                subjectRepository.upsertSubject(
                    subject = Subject(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() },

                        )
                )
                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectColors.random()
                    )
                }
                _snackbarEventFlow.emit(
                SnackbarEvent.ShowSnackbar(
                    message = "Subject saved successfully"
                )
                )

            }catch (e: Exception){
                _snackbarEventFlow.emit(
                SnackbarEvent.ShowSnackbar(
                    message = "Coundnt save the subject : ${e.message}",
                    SnackbarDuration.Long
                )
                )
            }



        }



    }

}