package com.nickdieda.smartstudy.presentation.task

import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.util.Priority

data class TaskState(
    val title:String="",
    val description:String="",
    val dueDate:Long?=null,
    val isTaskComplete:Boolean=false,
    val priority: Priority= Priority.LOW,
    val relatedToSubject:String?=null,
    val subjects:List<Subject> = emptyList(),
    val subjectId:Int? =null,
    val currentTaskId:Int?=null
)
