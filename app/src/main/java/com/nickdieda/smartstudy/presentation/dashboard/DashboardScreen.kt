package com.nickdieda.smartstudy.presentation.dashboard

import android.annotation.SuppressLint
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.nickdieda.smartstudy.util.SnackbarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
) {

    val viewModel: DashboardViewModel= hiltViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val sessions by viewModel.recentSessions.collectAsStateWithLifecycle()

DashboardScreen(
    state,
    tasks,
    resentSession = sessions,
    snackbarEvent = viewModel.snackbarEventFlow,

    event = viewModel::onEvent,
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




@SuppressLint("RememberReturnType")
@Composable
private fun DashboardScreen(
    state: DashboardState,
    tasks:List<Task>,
resentSession:List<Session>,
    event:(DashboardEvent)-> Unit,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    onSubjectCardClick:(Int?)->Unit,
    onTaskCardClick:(Int?)->Unit,
    onStarterSessionButtonClick:()->Unit
) {




    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when(event){
                is SnackbarEvent.ShowSnackbar->{
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {}
            }

        }
    }



AddSubjectDialog(
    isOpen = isAddSubjectDialogOpen,
    onDismissRequest = { isAddSubjectDialogOpen = false },
    onConfirmButtonClick = {
        event(DashboardEvent.SaveSubject)
        isAddSubjectDialogOpen = false
    },

    selectedColors = state.subjectCardColors,
    onColorChange ={event(DashboardEvent.onSubjectCardColorChange(it))},
    subjectName =state.subjectName,
    goalHours = state.goalStudyHours,
    onSubjectNameChange = {event(DashboardEvent.onSubjectNameChange(it))},
    onGoalHourChange ={event(DashboardEvent.onGoalStudyHoursChange(it))},
)


    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? Your studied hour will be reduced by this session time. This action can not be undone",
        onDismissRequest = {isDeleteDialogOpen=false},
        onConfirmButtonClick = {
            event(DashboardEvent.DeleteSession)
            isDeleteDialogOpen=false},
    )


    Scaffold(
        topBar ={ DashboardScreenTopBar() },
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
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
        subjectCount = state.totalSubjectCount,
        studiedHours = state.totalStudiedHours.toString(),
        goalHours = state.totalGoalStudyHours.toString()
    )



}
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = state.subjects,
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
                onCheckBoxClick = {event(DashboardEvent.onTaskIsCompleteChange(it))},
                onTaskCardClicked = onTaskCardClick

            )
            item{
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            studySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                sessions = resentSession,
                onDeleteIconClick = {
                    event(DashboardEvent.onDeleteSessionButtonClick(it))
                    isDeleteDialogOpen=true},
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
                    gradientColor = subject.colors.map { Color(it) },
                    onClick = { onSubjectCardClick(subject.subjectId) }
                )

            }
        }
    }


}