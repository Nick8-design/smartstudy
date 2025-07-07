package com.nickdieda.smartstudy.presentation.domain.model

import kotlin.time.Duration

data class Session(
    val sessionSubjectId:Int,
    val relatedToSubject:String,
    val date:Long,
    val duration: Long,
    val sessionId:Int
)
