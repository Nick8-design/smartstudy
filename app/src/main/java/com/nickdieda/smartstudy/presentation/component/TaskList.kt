package com.nickdieda.smartstudy.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nickdieda.smartstudy.R
import com.nickdieda.smartstudy.presentation.domain.model.Task
import com.nickdieda.smartstudy.util.Priority
import com.nickdieda.smartstudy.util.changeMillisDateString


fun LazyListScope.tasksList(
    sectionTitle:String,
    tasks:List<Task>,
    emptyListText:String,
    onTaskCardClicked:(Int?)->Unit,
    onCheckBoxClick: (Task) -> Unit
){
    item{
        Text(
            text = sectionTitle,
            style=MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp)
        )
    }

    if (tasks.isEmpty()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .size(120.dp),
                    painter = painterResource(id = R.drawable.img_tasks),
                    contentDescription = emptyListText
                )
                Spacer( modifier = Modifier.height(12.dp))
                Text(
                    text = emptyListText,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall

                )

            }
        }
    }
    items(tasks){ task->
        TaskCard(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
            task=task,
            onCheckBoxClick = {onCheckBoxClick(task)},
            onClick = {onTaskCardClicked(task.taskId)}
        )

    }
    
}


@Composable
fun TaskCard(
    modifier: Modifier=Modifier,
    task:Task,
    onCheckBoxClick:()->Unit,
    onClick:()->Unit
) {
    ElevatedCard(
        modifier=modifier.clickable {  onClick()}
    ){
        Row (
            modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically


        ){
TaskCheckBox(
    isComplete = task.isComplete,
    borderColor = Priority.frromInt(task.priority).color,
    onCheckBoxClick = onCheckBoxClick
)
            Spacer( modifier=Modifier.width(10.dp))

            Column {
                Text(
                    text = task.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if(task.isComplete){
                        TextDecoration.LineThrough
                    }else TextDecoration.None
                )
                Spacer( modifier=Modifier.height(4.dp))
                Text(
                    text = task.dueDate.changeMillisDateString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }

}