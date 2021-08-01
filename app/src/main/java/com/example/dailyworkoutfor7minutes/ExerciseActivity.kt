package com.example.dailyworkoutfor7minutes

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer:CountDownTimer? = null
    private var exerciseTimer:CountDownTimer? = null
    private var restProgress = 0
    private var workProgress = 0

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePosition = -1
    private var tts:TextToSpeech?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        tts = TextToSpeech(this,this)


        exerciseList = Constants.defaultExerciseList()
        setupRestView()
    // Order of using exerciseList and setupRestView() method should be similar to what used otherwise nullpointerexception will occur

    }

   public override fun onDestroy() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            workProgress = 0
        }

       tts?.stop()  //If TextToSpeech is speaking while shutdown the activity stop it and shut down
       tts?.shutdown()
        super.onDestroy()
    }

    private fun setupRestView(){
        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0
        }
        nextExerciseText.text = exerciseList!![currentExercisePosition+1].getName() // Getting the name of next exercise in rest view
        setRestProgressBar()

    }





    private fun setRestProgressBar(){
        progressBar.progress = restProgress
        restTimer = object :CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10-restProgress
                tvTimer.text = (10-restProgress).toString()
            }

            override fun onFinish() {
             //   Toast.makeText(this@ExerciseActivity,"Start working", Toast.LENGTH_SHORT).show()
                currentExercisePosition++
                setupExerciseView()
            }

        }.start()
    }

    private fun setupExerciseView(){
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        /*  if(exerciseTimer!=null){
             exerciseTimer!!.cancel()
              workProgress = 0
          } */
        exerciseTimer?.cancel() // Another way of null check
        workProgress = 0
        speakName(exerciseList!![currentExercisePosition].getName())
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()

        setWorkProgressBar()
    }

    private fun setWorkProgressBar(){

        exercise_progressBar.progress = workProgress
        exerciseTimer = object :CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                workProgress++
                exercise_progressBar.progress = 30-workProgress
                exercise_tvTimer.text = (30-workProgress).toString()
            }

            override fun onFinish() {


                if (currentExercisePosition < exerciseList!!.size-1){
                    setupRestView()


                }else {
                    Toast.makeText(this@ExerciseActivity, "You Did It", Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){  //It is to check whether TextToSpeech in device is installed or not
            val result = tts!!.setLanguage(Locale.US) // We want to set the language to US
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("TTS","The language specified is not supported")
            }
        } else{
            Log.e("TTS","Initialisation failed")
        }
    }
    private fun speakName(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }


}