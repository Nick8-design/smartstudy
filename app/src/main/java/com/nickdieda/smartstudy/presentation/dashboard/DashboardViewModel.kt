package com.nickdieda.smartstudy.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.nickdieda.smartstudy.presentation.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private  val subjectRepository: SubjectRepository
): ViewModel() {


}