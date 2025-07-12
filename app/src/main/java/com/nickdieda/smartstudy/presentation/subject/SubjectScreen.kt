package com.nickdieda.smartstudy.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nickdieda.smartstudy.presentation.component.AddSubjectDialog
import com.nickdieda.smartstudy.presentation.component.CountCard
import com.nickdieda.smartstudy.presentation.component.DeleteDialog
import com.nickdieda.smartstudy.presentation.component.studySessionsList
import com.nickdieda.smartstudy.presentation.component.tasksList
import com.nickdieda.smartstudy.presentation.destinations.TaskScreenRouteDestination
import com.nickdieda.smartstudy.presentation.destinations.TaskScreenRouteDestination.invoke
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.task.TaskScreenNavArgs

import com.nickdieda.smartstudy.sessions
import com.nickdieda.smartstudy.tasks
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


data class SubjectScreenNavArgs(
  val  subjectsId:Int
)



@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(navigator: DestinationsNavigator) {
    SubjectScreen(
        onBackButtonClick = {
            navigator.navigateUp()
        },
        onTaskButtonClick = {

            val navArg= TaskScreenNavArgs(taskId=null, subjectId = -1)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))

        },
        onTaskCardClick = { taskId->

            val navArg= TaskScreenNavArgs(taskId=taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))

        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    onBackButtonClick: () -> Unit,
    onTaskButtonClick:()->Unit,
    onTaskCardClick:(Int?)->Unit
) {

   var  isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var subjectName by remember { mutableStateOf("") }
    var getHours by remember { mutableStateOf("") }
    var selectedColors by remember { mutableStateOf(Subject.subjectColors.random()) }

    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        onDismissRequest = { isEditSubjectDialogOpen = false },
        onConfirmButtonClick = {

            isEditSubjectDialogOpen = false
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

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        bodyText = "Are you sure you want to delete this subject? All related tasks and study sessions will be permanently removed. This action can not be undone",
        onDismissRequest = {isDeleteSubjectDialogOpen=false},
        onConfirmButtonClick = {isDeleteSubjectDialogOpen=false},
    )



    val listState = rememberLazyListState()

    val isFabExpanded by remember{
        derivedStateOf { listState.firstVisibleItemIndex==0 }

    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {

            SubjectScreenTopBar(
                title = "Geography",
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = {isDeleteSubjectDialogOpen=true},
                onEditButtonClick = {isEditSubjectDialogOpen=true },
                scrollBehavior = scrollBehavior
            )
    },
    floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick =  onTaskButtonClick ,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            },
            text = { Text(text = "Add Task") },
            expanded = isFabExpanded
        )

    }
) { paddingValues->
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        item {
            SubjectOverViewSection(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                studiedHours = "10",
                goalHours = "15",
                progress = 0.75f
            )
        }
        tasksList(
            sectionTitle = "UPCOMING TASKS",
            emptyListText = "You don't have any upcoming tasks.\nClick the + to add new task.",
            tasks = tasks,
            onCheckBoxClick = {},
            onTaskCardClicked =onTaskCardClick

        )
        item{
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
        tasksList(
            sectionTitle = "COMPLETE TASKS",
            emptyListText = "You don't have any complete tasks.\nClick the check box on the completion of the task.",
            tasks = tasks,
            onCheckBoxClick = {},
            onTaskCardClicked =onTaskCardClick

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


@Composable
private fun SubjectOverViewSection(
modifier: Modifier,
studiedHours:String,
goalHours:String,
progress:Float

) {
    val percentageProgress = remember(progress){
        (progress*100).toInt().coerceIn(0,100)
    }


    Row (
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround){

        CountCard(
            modifier = Modifier.weight(1f),
            headindText = "Goal Study Hours",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headindText = "Study Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
      Box(modifier = Modifier.size(75.dp),
          contentAlignment=Alignment.Center){
          CircularProgressIndicator(
              progress = { 1f },
              modifier = Modifier
                  .fillMaxSize(),
              color = MaterialTheme.colorScheme.surfaceVariant,
              strokeWidth = 4.dp,
              strokeCap = StrokeCap.Round,
          )
          CircularProgressIndicator(
              progress = { progress },
              modifier = Modifier
                  .fillMaxSize(),

              strokeWidth = 4.dp,
              strokeCap = StrokeCap.Round,
          )

          Text("$percentageProgress %" )





      }






    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreenTopBar(
    title:String,
    onBackButtonClick:() ->Unit,
    onDeleteButtonClick:()->Unit,
    onEditButtonClick:()->Unit,
    scrollBehavior: TopAppBarScrollBehavior

) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },

        actions = {
            IconButton(
                onClick = onDeleteButtonClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )
            }

            IconButton(
                onClick = onEditButtonClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Subject"
                )
            }

        }

    )
}