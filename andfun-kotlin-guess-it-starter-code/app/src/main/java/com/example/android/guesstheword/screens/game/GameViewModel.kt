package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel(){

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 15_000L
        const val PANIC_SECONDS = 10L

    }

    // Timer
    private val timer: CountDownTimer

    private val _word = MutableLiveData<String>()
    private val _score = MutableLiveData<Int>()
    private val _eventGameFinish = MutableLiveData<Boolean>()
    private val _currentTime = MutableLiveData<Long>()
    private val _eventBuzz = MutableLiveData<BuzzType>()

    val score: LiveData<Int> get() = _score
    val word: LiveData<String> get() = _word
    val eventGameFinish: LiveData<Boolean> get() = _eventGameFinish
    val currentTime: LiveData<Long> get() = _currentTime
    val eventBuzz: LiveData<BuzzType> get() = _eventBuzz




    val currentTimeString: LiveData<String> = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("View Model", "GameViewModel created!")
        _score.value = 0

        resetList()
        nextWord()



        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished/ ONE_SECOND <= PANIC_SECONDS){
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true

            }
        }

        timer.start()

    }



    override fun onCleared() {
        super.onCleared()
        Log.i("View Model", "GameViewModel destroyed!")
        timer.cancel()
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }

        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
        _eventBuzz.value = BuzzType.CORRECT
    }

    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

    fun onBuzzComplete(){
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

}
