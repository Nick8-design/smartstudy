package com.nickdieda.smartstudy.presentation.subject

import androidx.compose.ui.graphics.Color
import com.nickdieda.smartstudy.presentation.dashboard.DashboardEvent
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Task

sealed class SubjectEvent{

    data object UpdateSubject : SubjectEvent()

    data object DeleteSubject: SubjectEvent()
    data object DeleteSession: SubjectEvent()

    data class onDeleteSessionButtonClick(val session: Session): SubjectEvent()

    data class onTaskIsCompleteChange(val task: Task): SubjectEvent()

    data class onSubjectCardColorChange(val colors: List<Color>): SubjectEvent()

    data class onSubjectNameChange(val name: String): SubjectEvent()

    data class onGoalStudyHoursChange(val hours: String): SubjectEvent()



}


