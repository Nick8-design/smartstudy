package com.nickdieda.smartstudy.presentation.subject

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.presentation.domain.repository.TaskRepository
import com.nickdieda.smartstudy.presentation.navArgs
import com.nickdieda.smartstudy.util.SnackbarEvent
import com.nickdieda.smartstudy.util.toHours
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
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectsId),
        sessionRepository.getTotalSessionDurationBySubject(navArgs.subjectsId)
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

    private  val _snackbarEventFlow= MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow=_snackbarEventFlow.asSharedFlow()



    init {
        fetchSubject()
    }





    fun onEvent(event: SubjectEvent) {
        when (event) {
            SubjectEvent.DeleteSession -> {}
            SubjectEvent.DeleteSubject -> deleteSuject()
            SubjectEvent.UpdateSubject -> updateSubject()
            is SubjectEvent.onDeleteSessionButtonClick -> {}
            is SubjectEvent.onGoalStudyHoursChange ->  {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }
            is SubjectEvent.onSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.colors
                    )
                }
            }
            is SubjectEvent.onSubjectNameChange ->  {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }
            is SubjectEvent.onTaskIsCompleteChange -> updateTask(event.task)
            SubjectEvent.UpdateProgress -> {
                val goalStudyHours=state.value.goalStudyHours.toFloatOrNull()?:1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours/goalStudyHours).coerceIn(0f,1f)
                    )
                }
            }
        }
    }

    private fun updateTask(task: Task){

            viewModelScope.launch {

                try {
                    taskRepository.upsertTask(
                        task=task.copy(isComplete = !task.isComplete)
                    )
                    if (task.isComplete){
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(
                            message = "Saved in completed tasks",

                            )
                    )
                    }else{

                        _snackbarEventFlow.emit(
                            SnackbarEvent.ShowSnackbar(
                                message = "Saved in upcoming tasks",

                                )
                        )
                    }





                }catch (e: Exception){
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(
                            message = "Coundnt update the task : ${e.message}",
                            SnackbarDuration.Long
                        )
                    )
                }



            }
    }

    private fun updateSubject() {
        viewModelScope.launch {

            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() },

                        )
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Updated successfully",

                        )
                )

            }catch (e: Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Coundnt update the subject : ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }



        }
    }


    private fun fetchSubject(){
        viewModelScope.launch {



                subjectRepository
                    .getSubjectById(navArgs.subjectsId)?.let { subject ->
                        _state.update {
                            it.copy(
                                subjectName = subject.name,
                                goalStudyHours = subject.goalHours.toString(),
                                subjectCardColors = subject.colors.map { Color(it) },
                                currentSubjectId = subject.subjectId

                            )
                        }


                    }


        }




    }




//    @SuppressLint("SuspiciousIndentation")
    private  fun deleteSuject(){
        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
            try {
                val currentSubjectId=  state.value.currentSubjectId;
                if (currentSubjectId!=null) {
                        withContext(Dispatchers.IO){
                        subjectRepository.deleteSubject(subjectId = currentSubjectId)
                    }
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(
                            message = "Subject deleted successfully",
                            SnackbarDuration.Long
                        )
                    )
                        _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)

                }else{
                        _snackbarEventFlow.emit(
                            SnackbarEvent.ShowSnackbar(
                                message = "No subject to delete",
                                SnackbarDuration.Long
                            )
                                             )
                    }
            }catch (e: Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Coundn't delete the subject : ${e.message}",
                        SnackbarDuration.Long
                    )
                )

            }
//            _state.update { it.copy(isLoading = false) }
        }
    }




}