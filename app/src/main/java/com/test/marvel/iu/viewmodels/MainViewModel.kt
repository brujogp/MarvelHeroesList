package com.test.marvel.iu.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.marvel.data.domain.Interact
import com.test.marvel.data.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val interactor: Interact
) : ViewModel() {
    private val charactersMutableLiveData = MutableLiveData<Status>()

    fun getCharacters(offset: String): LiveData<Status> {
        this@MainViewModel.charactersMutableLiveData.value =
            Status.Loading

        viewModelScope.launch {
            this@MainViewModel.charactersMutableLiveData.postValue(this@MainViewModel.interactor.getCharactersList(offset))
        }

        return charactersMutableLiveData
    }
}