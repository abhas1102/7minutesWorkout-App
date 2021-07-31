package com.example.dailyworkoutfor7minutes

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_exercise.*

class ExerciseActivity : AppCompatActivity() {

    private var restTimer:CountDownTimer? = null
    private var exerciseTimer:CountDownTimer? = null
    private var restProgress = 0
    private var workProgress = 0

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePosition = -1
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


        exerciseList = Constants.defaultExerciseList()
        setupRestView()


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
        super.onDestroy()
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

    private fun setupRestView(){
        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }



    private fun setupExerciseView(){
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        if(exerciseTimer!=null){
           exerciseTimer!!.cancel()
            workProgress = 0
        }
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        setWorkProgressBar()
    }
}