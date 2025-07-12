package com.nickdieda.smartstudy.presentation.session

import android.widget.Button
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nickdieda.smartstudy.presentation.component.DeleteDialog
import com.nickdieda.smartstudy.presentation.component.SubjectListBottomSheet
import com.nickdieda.smartstudy.presentation.component.studySessionsList

import com.nickdieda.smartstudy.sessions
import com.nickdieda.smartstudy.subjects
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch



@Destination
@Composable
fun SessionScreenRoute(nav: DestinationsNavigator) {

    val viewModel: SessionViewModel= hiltViewModel()
    SessionScreen(
        onBackButtonClick = {
nav.navigateUp()
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(onBackButtonClick: () -> Unit) {

    val scope = rememberCoroutineScope()
    var isBottomSheetDialogOpen by remember { mutableStateOf(false) }
    val sheetState= rememberModalBottomSheetState()
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }


    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure,you want to delete this session? \nThis action can not be undone.",
        onDismissRequest = {isDeleteDialogOpen=false},
        onConfirmButtonClick = {isDeleteDialogOpen=false}
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
        topBar = { SessionScreenTopBar (onBackButtonClick = onBackButtonClick)}
    ){ paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                    ,
                    relatedToSubject ="English" ,
                    selectSubjectButtonClick = {isBottomSheetDialogOpen=true}
                )
            }


            item {
                ButtonSections(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = {},
                    cancelButtonClick = {},
                    finishButtonClick = {}
                )
            }

            studySessionsList(
                sectionTitle = "STUDY SESSIONS HISTORY",
                sessions = sessions,
                onDeleteIconClick = {isDeleteDialogOpen=true},
                emptyListText ="You don't have any recent study sessions.\nStart a s study session to begin recording your progress."
            )



        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreenTopBar(
    onBackButtonClick:()->Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        },
        title = { Text("Study Session", style = MaterialTheme.typography.headlineSmall) },


        )


}




    @Composable
    fun TimerSection(
        modifier: Modifier

    ){
Box (
    modifier=modifier,
    contentAlignment = Alignment.Center
){
    Box(modifier = Modifier
        .size(250.dp)

        .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape),
        contentAlignment = Alignment.Center

    ){
        Text(
            text = "00:05:32",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
        )
    }

}


    }


@Composable
fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject:String,
    selectSubjectButtonClick:()->Unit
) {
    Column(
        modifier = modifier
    ) {


        Text(
            text = "Related to subject",
            style = MaterialTheme.typography.bodySmall,

            )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge,

                )

            IconButton(
                onClick = selectSubjectButtonClick,
            ) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Select subject"

                )
            }


        }

    }

}

@Composable
fun ButtonSections(modifier: Modifier,
                   startButtonClick:()->Unit,
                   cancelButtonClick:()->Unit,
             finishButtonClick:()->Unit

) {

    Row (
        modifier=modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Button(
            onClick = cancelButtonClick
        ) {
            Text("Cancel",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
        }

        Button(
            onClick = startButtonClick
        ) {
            Text("Start",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }

        Button(
            onClick = finishButtonClick
        ) {
            Text("Finish",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}