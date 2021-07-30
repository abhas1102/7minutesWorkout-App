package com.example.dailyworkoutfor7minutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llStart.setOnClickListener {
           // Toast.makeText(this,"Let's ready to be bored!!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }
    }
}