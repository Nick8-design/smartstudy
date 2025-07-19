package com.nickdieda.smartstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.nickdieda.smartstudy.presentation.NavGraphs

import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task

import com.nickdieda.smartstudy.presentation.theme.SmartStudyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStudyTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)

            }
            }
        }
    }










val subjects=listOf(
    Subject(name = "English", goalHours = 10f, colors = Subject.subjectColors[0].map { it.toArgb() }, subjectId = 0),
    Subject(name = "Kiswahili", goalHours = 10f, colors = Subject.subjectColors[1].map { it.toArgb() }, subjectId = 0),
    Subject(name = "Physics", goalHours = 10f, colors = Subject.subjectColors[2].map { it.toArgb() }, subjectId = 0),
    Subject(name = "Maths", goalHours = 10f, colors = Subject.subjectColors[3].map { it.toArgb() }, subjectId = 0),
    Subject(name = "Geography", goalHours = 10f, colors = Subject.subjectColors[4].map { it.toArgb() }, subjectId = 0)
)

val tasks= listOf(
    Task(
        title = "Learn Android",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Do homework",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Prepare Notes ",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Watch Movie",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isComplete = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Learn .NET",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Go to work",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = true,
        taskSubjectId = 0,
        taskId = 1
    ),



    )

val  sessions= listOf(
    Session(
        sessionSubjectId = 0,
        relatedToSubject = "French",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 0,
        relatedToSubject = "Computer Study",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 0,
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 0,
        relatedToSubject = "Physics",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 0,
        relatedToSubject = "Kiswahili",
        date = 0L,
        duration = 2,
        sessionId = 0
    )



)