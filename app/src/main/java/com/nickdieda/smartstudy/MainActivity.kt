package com.nickdieda.smartstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nickdieda.smartstudy.presentation.dashboard.DashboardScreen
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.session.SessionScreen
import com.nickdieda.smartstudy.presentation.subject.SubjectScreen
import com.nickdieda.smartstudy.presentation.task.TaskScreen
import com.nickdieda.smartstudy.presentation.theme.SmartStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStudyTheme {
//                DashboardScreen()
//                SubjectScreen()
//                TaskScreen()
                SessionScreen()
            }
            }
        }
    }

val subjects=listOf(
    Subject(name = "English", goalHours = 10f, colors = Subject.subjectColors[0], subjectId = 0),
    Subject(name = "Kiswahili", goalHours = 10f, colors = Subject.subjectColors[1], subjectId = 0),
    Subject(name = "Physics", goalHours = 10f, colors = Subject.subjectColors[2], subjectId = 0),
    Subject(name = "Maths", goalHours = 10f, colors = Subject.subjectColors[3], subjectId = 0),
    Subject(name = "Geography", goalHours = 10f, colors = Subject.subjectColors[4], subjectId = 0)
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