package com.nickdieda.smartstudy.presentation.domain.data.repository

import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import com.nickdieda.smartstudy.presentation.domain.data.local.SessionDao
import com.nickdieda.smartstudy.presentation.domain.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImp @Inject constructor(
    private val sessionDao: SessionDao
): SessionRepository {
    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(sessionId: Session) {
        return sessionDao.deleteSession(sessionId)
    }

    override fun getAllSssions(): Flow<List<Session>> {
       return sessionDao.getAllSssions()
           .map { sessions -> sessions.sortedByDescending { it.date } }
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
       return sessionDao.getSessionsForSubject(subjectId).take(10)
           .map { sessions -> sessions.sortedByDescending { it.date } }
    }

    override fun getTotalSessionDuration(): Flow<Long> {
      return  sessionDao.getTotalSessionDuration()
    }

    override fun getTotalSessionDurationBySubject(subjectId: Int): Flow<Long> {
       return sessionDao.getTotalSessionDurationBySubject(subjectId)
    }

    override suspend fun deleteSessionBySubjectId(sujectId: Int) {
     return sessionDao.deleteSessionBySubjectId(sujectId)
    }
}