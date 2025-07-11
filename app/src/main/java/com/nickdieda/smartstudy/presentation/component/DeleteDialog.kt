package com.nickdieda.smartstudy.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun DeleteDialog(
    isOpen:Boolean,
    title:String,
    bodyText:String,
    onDismissRequest:()->Unit,
    onConfirmButtonClick:()->Unit,


    ) {




    if(isOpen) {


        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = title)
            },
            text = {

            Text(bodyText)
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                TextButton(

                    onClick = onConfirmButtonClick
                ) {
                    Text("Delete",
                        color = Color.Red
                        )
                }

            }

        )
    }
}