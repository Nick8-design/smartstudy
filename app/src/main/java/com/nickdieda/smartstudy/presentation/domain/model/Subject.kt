package com.nickdieda.smartstudy.presentation.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nickdieda.smartstudy.presentation.theme.gradient1
import com.nickdieda.smartstudy.presentation.theme.gradient2
import com.nickdieda.smartstudy.presentation.theme.gradient3
import com.nickdieda.smartstudy.presentation.theme.gradient4
import com.nickdieda.smartstudy.presentation.theme.gradient5

@Entity
data class Subject(

    val name:String,
    val goalHours:Float,
    val colors:List<Color>,

    @PrimaryKey(true)
    val subjectId:Int?=null
){
    companion object{
        val subjectColors= listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
