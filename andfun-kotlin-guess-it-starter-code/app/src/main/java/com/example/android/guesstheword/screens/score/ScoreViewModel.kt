package com.example.android.guesstheword.screens.score

import androidx.lifecycle.*

class ScoreViewModel (finalScore: Int): ViewModel(){

    private var _score = MutableLiveData<Int>()
    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val score: LiveData<Int> get() = _score
    val eventPlayAgain: LiveData<Boolean> get() = _eventPlayAgain
    init {
        _score.value = finalScore
    }

    fun onPlayAgain(){
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete(){
        _eventPlayAgain.value = false
    }
}
class ScoreViewModelFactory(private val finalScore: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            // TODO Construct and return the ScoreViewModel
            return ScoreViewModel(finalScore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}