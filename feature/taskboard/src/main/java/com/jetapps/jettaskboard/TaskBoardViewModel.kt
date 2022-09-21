package com.jetapps.jettaskboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskBoardViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {

}