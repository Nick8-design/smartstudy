package com.nickdieda.smartstudy.presentation.domain.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nickdieda.smartstudy.presentation.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Upsert
 suspend   fun upsertSubject(subject: Subject)

    @Query("SELECT COUNT(*) FROM SUBJECT")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECT")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM Subject Where subjectId = :subjectId")
   suspend fun getSubjectById(subjectId:Int): Subject?

    @Query("DELETE FROM Subject Where subjectId = :subjectId")
   suspend fun deleteSubject(subjectId:Int)

    @Query("SELECT * FROM Subject ")
    fun getAllSubjects():Flow<List<Subject>>

}