package com.nickdieda.smartstudy.presentation.domain.data.repository

import com.nickdieda.smartstudy.presentation.domain.data.local.SubjectDao
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImp @Inject constructor(
    private val subjectDao: SubjectDao
): SubjectRepository {
    override suspend fun upsertSubject(subject: Subject) {
       subjectDao.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getTotalGoalHours(): Flow<Float> {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectById(subjectId: Int): Subject? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSubject(subjectId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        TODO("Not yet implemented")
    }

}