package com.workout.dailyworkoutfor7minutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this@MainActivity)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        llStart.setOnClickListener {
           // Toast.makeText(this,"Let's ready to be bored!!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }

        llHistory.setOnClickListener{
            val intent = Intent(this,HistoryActivity::class.java )
            startActivity(intent)
        }
    }
}