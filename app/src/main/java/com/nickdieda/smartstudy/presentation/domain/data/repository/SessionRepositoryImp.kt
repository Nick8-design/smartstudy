package com.nickdieda.smartstudy.presentation.domain.data.repository

import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.data.local.SessionDao
import com.nickdieda.smartstudy.presentation.domain.model.Session
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImp @Inject constructor(
    private val sessionDao: SessionDao
): SessionRepository {
    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(sessionId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllSssions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionDuration(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionDurationBySubjectId(subjectId: Int): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSessionBySubjectId(sujectId: Int) {
        TODO("Not yet implemented")
    }
}