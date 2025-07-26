package com.nickdieda.smartstudy.presentation.subject

import androidx.compose.ui.graphics.Color
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task

data class SubjectState(
    val currentSubjectId:Int?=null,
    val subjectName: String="",
    val goalStudyHours: String="",
    val subjectCardColors:List<Color> = Subject.subjectColors.random(),
    val progress:Float=0f,
    val studiedHours:Float=0f,
    var recentSessions:List<Session> =emptyList(),
    val upcomingTasks:List<Task> =emptyList(),
    val completedTasks:List<Task> =emptyList(),
    val session:Session?=null,

)