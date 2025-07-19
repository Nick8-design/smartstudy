package com.nickdieda.smartstudy.presentation.dashboard

import androidx.compose.material3.CardColors
import androidx.compose.ui.graphics.Color
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject

data class DashboardState(
    val totalSubjectCount: Int= 0,
    val totalStudiedHours: Float= 0f,
    val totalGoalStudyHours: Float=0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String= "",
    val goalStudyHours: String= "",
    val subjectCardColors:List<Color> = Subject.subjectColors.random(),
    val session: Session?= null

    )