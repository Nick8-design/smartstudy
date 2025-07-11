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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.nickdieda.smartstudy.presentation.component.DeleteDialog
import com.nickdieda.smartstudy.presentation.component.SubjectListBottomSheet
import com.nickdieda.smartstudy.presentation.component.TaskCheckBox
import com.nickdieda.smartstudy.presentation.component.TaskDatePicker
import com.nickdieda.smartstudy.presentation.theme.Red
import com.nickdieda.smartstudy.subjects
import com.nickdieda.smartstudy.util.Priority
import com.nickdieda.smartstudy.util.changeMillisDateString
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen() {



    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
        title.isBlank()->"Please enter task title."
        title.length<4 ->"Task title is too short"
        title.length>30 ->"Task title is too long."
        else->null

    }

DeleteDialog(
    isOpen = isDeleteDialogOpen,
    title = "Delete Task?",
    bodyText = "Are you sure,you want to delete this task? \nThis action can not be undone.",
    onDismissRequest = {isDeleteDialogOpen=false},
    onConfirmButtonClick = {isDeleteDialogOpen=false}
)

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = {isDatePickerDialogOpen=false},
        onConfirmButtonClicked = { isDatePickerDialogOpen=false }
    )


    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetDialogOpen,
        subjects = subjects,

        onSubjectClicked = {
 scope.launch {
     sheetState.hide()
 }.invokeOnCompletion {
     if(!sheetState.isVisible)isBottomSheetDialogOpen=false
 }
        },
        onDismissRequest = {isBottomSheetDialogOpen=false}
    )

    Scaffold (
        topBar = {
            TaskScreenTopBar(
                isTaskExist = true,
                isComplete = false,
                checkBorderColor = Red,
                onBackButtonClick = {},
                onDeleteButtonClick = {isDeleteDialogOpen=true},
                onCheckBoxClick = {}
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
                isError = taskTitleError!=null && title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty()) },
                modifier = Modifier
                    .fillMaxWidth(),
                value = title,
                singleLine = true,
                onValueChange = {title=it},
                label = {Text("Title")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = description,
                onValueChange = {description=it},
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
                    text = datePickerState.selectedDateMillis.changeMillisDateString(),
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
                        borderColor = if (priority==Priority.MEDIUM){
                            Color.White
                        }else Color.Transparent,
                        labelColor = if (priority==Priority.MEDIUM){
                            Color.White
                        }else Color.White.copy(alpha  =0.7f),
                        onClick = {}
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
                Text(
                    text = "English",
                    style = MaterialTheme.typography.bodyLarge,

                    )

                IconButton(
                    onClick = {isBottomSheetDialogOpen=true},
                ) {
                    Icon(Icons.Default.ArrowDropDown,
                        contentDescription = "Select subject"

                    )
                }


            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                enabled = taskTitleError==null,
                onClick = {},
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