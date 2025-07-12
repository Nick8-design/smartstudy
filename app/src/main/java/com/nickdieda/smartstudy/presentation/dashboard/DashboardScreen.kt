package com.nickdieda.smartstudy.presentation.dashboard

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nickdieda.smartstudy.R
import com.nickdieda.smartstudy.presentation.component.AddSubjectDialog
import com.nickdieda.smartstudy.presentation.component.CountCard
import com.nickdieda.smartstudy.presentation.component.DeleteDialog
import com.nickdieda.smartstudy.presentation.component.SubjectCard
import com.nickdieda.smartstudy.presentation.component.studySessionsList
import com.nickdieda.smartstudy.presentation.component.tasksList
import com.nickdieda.smartstudy.presentation.destinations.SessionScreenRouteDestination
import com.nickdieda.smartstudy.presentation.destinations.SubjectScreenRouteDestination
import com.nickdieda.smartstudy.presentation.destinations.TaskScreenRouteDestination
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.presentation.subject.SubjectScreenNavArgs
import com.nickdieda.smartstudy.presentation.task.TaskScreenNavArgs
import com.nickdieda.smartstudy.sessions
import com.nickdieda.smartstudy.subjects
import com.nickdieda.smartstudy.tasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
) {


DashboardScreen(
    onSubjectCardClick={subjectId->
        subjectId?.let{
            val navArg= SubjectScreenNavArgs(subjectId)
            navigator.navigate(SubjectScreenRouteDestination(navArgs = navArg))
        }

    },
    onTaskCardClick={
            taskId->

            val navArg= TaskScreenNavArgs(taskId=taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))

    },
    onStarterSessionButtonClick={
        navigator.navigate(SessionScreenRouteDestination)
    }
)

}




@Composable
private fun DashboardScreen(

    onSubjectCardClick:(Int?)->Unit,
    onTaskCardClick:(Int?)->Unit,
    onStarterSessionButtonClick:()->Unit
) {




    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var subjectName by remember { mutableStateOf("") }
    var getHours by remember { mutableStateOf("") }
    var selectedColors by remember { mutableStateOf(Subject.subjectColors.random()) }

AddSubjectDialog(
    isOpen = isAddSubjectDialogOpen,
    onDismissRequest = { isAddSubjectDialogOpen = false },
    onConfirmButtonClick = {

        isAddSubjectDialogOpen = false
    },

    selectedColors = selectedColors,
    onColorChange ={selectedColors=it},
    subjectName =subjectName,
    goalHours = getHours,
    onSubjectNameChange = {subjectName=it},
    onGoalHourChange ={getHours=it},
)


    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? Your studied hour will be reduced by this session time. This action can not be undone",
        onDismissRequest = {isDeleteDialogOpen=false},
        onConfirmButtonClick = {isDeleteDialogOpen=false},
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
                    subjectList = subjects,
                    onAddIconClicked = {
                        isAddSubjectDialogOpen=true
                    },
                    onSubjectCardClick=onSubjectCardClick
                )
            }


            item {
                Button(
                    onClick = onStarterSessionButtonClick,
                    modifier = Modifier
                        .padding(48.dp, 20.dp)
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
                onTaskCardClicked = onTaskCardClick

            )
            item{
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            studySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                sessions = sessions,
                onDeleteIconClick = {isDeleteDialogOpen=true},
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
    onAddIconClicked:()->Unit,
    emptyListText:String="You don't have any subjects. \nClick the + button to add new subject.",
    onSubjectCardClick: (Int?) -> Unit
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
                onClick = onAddIconClicked,

            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
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
                    onClick = { onSubjectCardClick(subject.subjectId) }
                )

            }
        }
    }


}