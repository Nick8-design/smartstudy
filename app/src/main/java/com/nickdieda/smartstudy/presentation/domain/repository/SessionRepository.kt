package com.nickdieda.smartstudy.presentation.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nickdieda.smartstudy.presentation.domain.model.Session
import com.nickdieda.smartstudy.presentation.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun  insertSession(session: Session)


    suspend fun deleteSession(sessionId: Int)


    fun getAllSssions(): Flow<List<Session>>

    fun getRecentTenSessionsForSubject(subjectId:Int): Flow<List<Session>>


    fun getTotalSessionDuration(): Flow<Long>


    fun getTotalSessionDurationBySubject(subjectId:Int): Flow<Long>

    suspend fun deleteSessionBySubjectId(sujectId:Int)

}