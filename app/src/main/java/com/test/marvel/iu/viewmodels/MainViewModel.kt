package com.test.marvel.iu.viewmodels

import android.content.Context

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.marvel.data.domain.Interact
import com.test.marvel.data.models.Result
import com.test.marvel.data.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interact: Interact
) : ViewModel() {
    private val charactersMutableLiveData = MutableLiveData<Status>()
    private val comicsMutableLiveData = MutableLiveData<Status>()
    var heroSelected: Result? = null

    fun getCharacters(offset: String): LiveData<Status> {
        this@MainViewModel.charactersMutableLiveData.value =
            Status.Loading

        viewModelScope.launch {
            this@MainViewModel.charactersMutableLiveData.postValue(
                this@MainViewModel.interact.getCharactersList(
                    offset
                )
            )
        }

        return charactersMutableLiveData
    }

    fun getComicsByCharacterId(characterId: String): LiveData<Status> {
        this@MainViewModel.comicsMutableLiveData.value =
            Status.Loading

        viewModelScope.launch {
            this@MainViewModel.comicsMutableLiveData.postValue(
                this@MainViewModel.interact.getComicsByCharacterId(
                    characterId
                )
            )
        }

        return comicsMutableLiveData
    }

    fun heroSelected(result: Result) {
        this.heroSelected = result
    }
}