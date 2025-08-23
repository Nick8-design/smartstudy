package com.nickdieda.smartstudy.presentation.session

import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Subject

data class SessionState(
    val subjectList: List<Subject> =emptyList(),
    val sessionList: List<Session> =emptyList(),
    val relatedToSubject:String?=null,
    val subjectId:Int?=null,
    val session: Session?=null

)
