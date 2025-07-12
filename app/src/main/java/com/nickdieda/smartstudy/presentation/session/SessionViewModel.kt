package com.nickdieda.smartstudy.presentation.session

import androidx.lifecycle.ViewModel
import com.nickdieda.smartstudy.presentation.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
): ViewModel() {
}