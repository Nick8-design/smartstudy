package com.nickdieda.smartstudy.presentation.dashboard

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nickdieda.smartstudy.R
import com.nickdieda.smartstudy.presentation.component.CountCard
import com.nickdieda.smartstudy.presentation.component.SubjectCard
import com.nickdieda.smartstudy.presentation.component.studySessionsList
import com.nickdieda.smartstudy.presentation.component.tasksList
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task

@Composable
fun DashboardScreen() {
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



    Scaffold(
        topBar ={ DashboardScreenTopBar() }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

item {
    CountCardSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        subjectCount = 5,
        studiedHours = "10",
        goalHours = "15"
    )



}
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjects
                )
            }


            item {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(48.dp,20.dp)
                        .fillMaxWidth()
                ) {
                    Text("Start Study Session")
                }
            }

            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcomming tasks.\nClick the + to add new task.",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClicked = {}

            )
            item{
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            studySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                sessions = sessions,
                onDeleteIconClick = {},
                emptyListText ="You don't have any recent study sessions.\nStart a s study session to begin recording your progress."
            )


        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("SmartStudy",
                style = MaterialTheme.typography.headlineMedium)
        }
    )
}


@Composable
private fun CountCardSection(
    modifier: Modifier,
    subjectCount:Int,
    studiedHours:String,
    goalHours:String
) {
    Row (modifier=modifier){
        CountCard(
            headindText = "Subject Count",
            count = "$subjectCount",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            headindText = "Studied Hours",
            count = studiedHours,
            modifier = Modifier.weight(1f)

        )

        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            headindText = "Goal Study Hours",
            count = goalHours,
            modifier = Modifier.weight(1f)

        )

    }
}


@Composable
fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyListText:String="You don't have any subjects. \nClick the + button to add new subject."
) {

    Column(modifier=modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 12.dp)
            )
            IconButton(
                onClick = {},

            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add SUbject"
                )
            }

        }

        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.img_books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = emptyListText,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall

            )

        }


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList){subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColor = subject.colors,
                    onClick = {
//                        Toast.makeText(conte,"Coming soon ",Toast.LENGTH_SHORT)
                    }
                )

            }
        }
    }


}