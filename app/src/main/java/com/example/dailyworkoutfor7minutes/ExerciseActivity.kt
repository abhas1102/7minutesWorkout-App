package com.example.dailyworkoutfor7minutes

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.item_custom_back_confirmation.*
import java.lang.Exception
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
    private var player:MediaPlayer?=null

    private var exerciseAdapter:ExercriseStatusAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        tts = TextToSpeech(this,this)


        exerciseList = Constants.defaultExerciseList()
        setupRestView()

        exerciseStatusRecyclerView()
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
       player?.stop()
        super.onDestroy()
    }

    private fun setupRestView(){
        try {
            player = MediaPlayer.create(applicationContext,R.raw.press_start)
            player?.isLooping = false
            player?.start()
        } catch (e:Exception){
            e.printStackTrace()
        }

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
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
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
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()


                if (currentExercisePosition < exerciseList!!.size-1){

                    setupRestView()
                }else {
                  //  Toast.makeText(this@ExerciseActivity, "You Did It", Toast.LENGTH_SHORT).show()
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
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

    private fun exerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExercriseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.item_custom_back_confirmation)

        customDialog.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }


}