package com.nickdieda.smartstudy.presentation.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import com.nickdieda.smartstudy.util.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
   subjectRepository: SubjectRepository,

): ViewModel() {

    private  val _state = MutableStateFlow(SessionState())

    val state =combine(
        _state,
        subjectRepository.getAllSubjects(),
        sessionRepository.getAllSssions()
    ){state,subjects,sessions->

        state.copy(
            subjectList =subjects,
            sessionList = sessions

        )

    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        SessionState()
    )




    private  val _snackbarEventFlow= MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow=_snackbarEventFlow.asSharedFlow()



    fun onEvent(event: SessionEvent){
        when(event){
            SessionEvent.NotifyToUpdateSubject -> notifyToUpdateSubject()
            SessionEvent.DeleteSession -> deleteSession()
            is SessionEvent.OnDeleteSessionButtonClick ->{
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SessionEvent.OnRelatedSubjectChange -> {
               _state.update {
                   it.copy(
                       relatedToSubject = event.subject.name,
                       subjectId = event.subject.subjectId
                   )
               }
            }
            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectAndRelatedSubject -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.relatedToSubject,
                        subjectId = event.subjectId
                    )
                }

            }
        }
    }

    private fun notifyToUpdateSubject() {
        viewModelScope.launch {
            if (state.value.subjectId==null||state.value.relatedToSubject==null)
            _snackbarEventFlow.emit(
                SnackbarEvent.ShowSnackbar(
                    message = "Please select a subject first",
                    SnackbarDuration.Long
                )
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration<36){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Single session can not be less than 36 seconds",
                    )
                        )
                return@launch
            }

            try {



                sessionRepository.insertSession(
                    session = Session(
                        sessionSubjectId = state.value.subjectId ?: -1,
                        relatedToSubject = state.value.relatedToSubject ?: "",
                        date = Clock.System.now().toEpochMilliseconds(),
                        duration = duration
                    )
                )

                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Session saved successfully",

                        )
                )

            }catch (e: Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Coundnt save the session : ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }


        }
    }


    private fun deleteSession() {
        viewModelScope.launch {

            try {
                state.value.session?.let{
                   sessionRepository.deleteSession(it)
                }
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Session deleted successfully",

                        )
                )

            }catch (e: Exception){
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Coundnt delete session. : ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }



        }
    }

}