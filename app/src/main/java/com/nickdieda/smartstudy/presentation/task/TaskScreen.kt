package com.nickdieda.smartstudy.presentation.task


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nickdieda.smartstudy.presentation.component.DeleteDialog
import com.nickdieda.smartstudy.presentation.component.SubjectListBottomSheet
import com.nickdieda.smartstudy.presentation.component.TaskCheckBox
import com.nickdieda.smartstudy.presentation.component.TaskDatePicker
import com.nickdieda.smartstudy.util.Priority
import com.nickdieda.smartstudy.util.SnackbarEvent
import com.nickdieda.smartstudy.util.changeMillisDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime

data class TaskScreenNavArgs(
    val taskId:Int?,
    val subjectId:Int?
)

@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreenRoute(nav: DestinationsNavigator) {
    val viewModel: TaskViewModel= hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    TaskScreen(
        onEvent = viewModel::onEvent,
        state=state ,
        snackbarEvent = viewModel.snackbarEventFlow,
        onBackButtonClick = {
            nav.navigateUp()
        }
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreen(
    state: TaskState,
    onEvent:(TaskEvent)->Unit,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    onBackButtonClick: () -> Unit
) {



//    var title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }

    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }

    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val today = ZonedDateTime.now().toInstant().toEpochMilli()

                return utcTimeMillis >=today
            }
        }
    )
    val scope = rememberCoroutineScope()
    var isBottomSheetDialogOpen by remember { mutableStateOf(false) }
    val sheetState= rememberModalBottomSheetState()



    taskTitleError=when{
       state.title.isBlank()->"Please enter task title."
        state.title.length<4 ->"Task title is too short"
        state.title.length>30 ->"Task title is too long."
        else->null

    }

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

                SnackbarEvent.NavigateUp -> {onBackButtonClick()}
            }

        }
    }


    DeleteDialog(
    isOpen = isDeleteDialogOpen,
    title = "Delete Task?",
    bodyText = "Are you sure,you want to delete this task? \nThis action can not be undone.",
    onDismissRequest = {isDeleteDialogOpen=false},
    onConfirmButtonClick = {
        onEvent(TaskEvent.DeleteTask)
        isDeleteDialogOpen=false}
)

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = {isDatePickerDialogOpen=false},
        onConfirmButtonClicked = {
            onEvent(TaskEvent.OnDateChange(datePickerState.selectedDateMillis))
            isDatePickerDialogOpen=false }
    )


    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetDialogOpen,
        subjects = state.subjects,

        onSubjectClicked = {subject->
       scope.launch {
     sheetState.hide()
 }.invokeOnCompletion {
     if(!sheetState.isVisible)isBottomSheetDialogOpen=false

 }
            onEvent(TaskEvent.OnRelatedSubjectSelect(subject))

        },
        onDismissRequest = {isBottomSheetDialogOpen=false}
    )

    Scaffold (

        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            TaskScreenTopBar(
                isTaskExist = state.currentTaskId!=null,
                isComplete = state.isTaskComplete,
                checkBorderColor = state.priority.color,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = {isDeleteDialogOpen=true},
                onCheckBoxClick = {
                    onEvent(TaskEvent.OnIsCompleteChange)
                }
            )
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(state = rememberScrollState())
        ){

            OutlinedTextField(
                isError = taskTitleError!=null && state.title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty()) },
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.title,
                singleLine = true,
                onValueChange = {onEvent(TaskEvent.OnTitleChange(it))},
                label = {Text("Title")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.description,
                onValueChange = {onEvent(TaskEvent.OnDescriptionChange(it))},
                label = {Text("Description")}
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Due Date",
                style = MaterialTheme.typography.bodySmall,

            )
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

                ){
                Text(
                    text = state.dueDate.changeMillisDateString(),
                    style = MaterialTheme.typography.bodyLarge,

                    )

                IconButton(
                    onClick = {isDatePickerDialogOpen=true},
                ) {
                    Icon(Icons.Default.DateRange,
                        contentDescription = "Select Due Date"

                    )
                }


            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Priority",
                style = MaterialTheme.typography.bodySmall,

                )
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth()){
                Priority.entries.forEach { priority->
                    PrioriyButton(
                        modifier = Modifier.weight(1f),
                        label =priority.title,
                        backgroundColor = priority.color,
                        borderColor = if (priority==state.priority){
                            Color.White
                        }else Color.Transparent,
                        labelColor = if (priority==state.priority){
                            Color.White
                        }else Color.White.copy(alpha  =0.7f),
                        onClick = {
                            onEvent(TaskEvent.OnPriorityChange(priority))
                        }
                    )

                }
            }


            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Related to subject",
                style = MaterialTheme.typography.bodySmall,

                )
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                val firstSubject=state.subjects.firstOrNull()?.name?:""
                Text(
                    text = state.relatedToSubject?:firstSubject,
                    style = MaterialTheme.typography.bodyLarge,

                    )

                IconButton(
                    onClick = {

                        isBottomSheetDialogOpen=true
                              },
                ) {
                    Icon(Icons.Default.ArrowDropDown,
                        contentDescription = "Select subject"

                    )
                }


            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                enabled = taskTitleError==null,
                onClick = {
                    onEvent(TaskEvent.SaveTask)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)


            ) {
                Text("Save")
            }



        }

    }
}


@Composable
fun PrioriyButton(
    modifier: Modifier = Modifier,
    label:String,
    backgroundColor: Color,
    borderColor:Color,
    labelColor: Color,
    onClick:()->Unit

) {
    Box(
        modifier=modifier
            .background(backgroundColor)
            .clickable {
                onClick()
            }
            .padding(5.dp)
            .border(1.dp, borderColor, MaterialTheme.shapes.medium)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label,color=labelColor)
    }

}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenTopBar(
    isTaskExist:Boolean,
    isComplete:Boolean,
    checkBorderColor: Color,
    onBackButtonClick:() -> Unit,
    onDeleteButtonClick:() -> Unit,
    onCheckBoxClick:() -> Unit,


    ) {
    TopAppBar(
        navigationIcon = {
          IconButton(
              onClick = onBackButtonClick,
          ) {
              Icon(Icons.Default.ArrowBack,
                  contentDescription = "Navigate back"

              )
          }
        },
        title = {
            Text("Task", style = MaterialTheme.typography.headlineSmall)
        },
        actions = {
            if(isTaskExist){
                TaskCheckBox(
                    isComplete =isComplete,
                    borderColor = checkBorderColor,
                    onCheckBoxClick =onCheckBoxClick
                )

                IconButton(
                    onClick = onDeleteButtonClick,
                ) {
                    Icon(Icons.Default.Delete,
                        contentDescription = "Delete task"

                    )
                }

            }



        }

    )

}