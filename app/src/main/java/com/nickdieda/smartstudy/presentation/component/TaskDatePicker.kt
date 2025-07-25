package com.nickdieda.smartstudy.presentation.component

import androidx.compose.material3.DatePicker

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    state:DatePickerState,
    isOpen:Boolean,
    confirmButtonText:String="OK",
    dismissButtonText:String="Cancel",
    onDismissRequest:()->Unit,
    onConfirmButtonClicked:()->Unit


) {
if (isOpen){

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClicked
            ) {
                Text(confirmButtonText)
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(dismissButtonText)
            }
        },

        content = {

            DatePicker(
                state = state,



            )


        }
    )

}



}