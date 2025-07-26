package com.nickdieda.smartstudy.presentation.domain.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nickdieda.smartstudy.presentation.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun  insertSession(session: Session)


    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM Session")
    fun getAllSssions(): Flow<List<Session>>

    @Query("SELECT * FROM Session WHERE sessionSubjectId = :subjectId" )
    fun getSessionsForSubject(subjectId:Int): Flow<List<Session>>

    @Query("SELECT SUM(duration) FROM SESSION")
    fun getTotalSessionDuration(): Flow<Long>

    @Query("SELECT SUM(duration) FROM SESSION WHERE sessionSubjectId = :subjectId")
    fun getTotalSessionDurationBySubject(subjectId:Int): Flow<Long>

    @Query("DELETE FROM Session where sessionSubjectId = :sujectId")
    suspend fun deleteSessionBySubjectId(sujectId:Int)



}