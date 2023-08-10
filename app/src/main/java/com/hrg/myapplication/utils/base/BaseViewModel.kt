package com.hrg.myapplication.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrg.myapplication.domain.models.ResultNet
import com.hrg.myapplication.utils.networkHelper.ConnectionNet
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var mConnectionNet: ConnectionNet
}