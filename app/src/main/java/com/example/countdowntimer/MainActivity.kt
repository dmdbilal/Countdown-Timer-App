package com.example.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.countdowntimer.databinding.ActivityMainBinding
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var konfettiView: KonfettiView

    private lateinit var timer: CountDownTimer
    private var milli_secs = 0L
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener {
            if(isRunning) {
                pauseTimer()
            } else {
                val h: Long = if (binding.countdownHour.text.toString().isBlank()) "0".toLong() else binding.countdownHour.text.toString().toLong()
                val m: Long = if (binding.countdownMinute.text.toString().isBlank()) "0".toLong() else binding.countdownMinute.text.toString().toLong()
                val s: Long = if (binding.countdownSecond.text.toString().isBlank()) "0".toLong() else binding.countdownSecond.text.toString().toLong()
                if(h==0L && m==0L && s ==0L)
                    Toast.makeText(applicationContext, "Invalid Input", Toast.LENGTH_SHORT).show()
                if(h!=0L || m!=0L || s!=0L) {
                    milli_secs = (h * 3600000) + (m * 60000) + (s * 1000) // millsecs to secs
                    startTimer(milli_secs)
                }
            }
        }

        binding.resetButton.setOnClickListener {
            resetTimer()
        }
    }

    private fun resetTimer() {
        isRunning = false
        timer.cancel()
        milli_secs = 0L
        updateUI()
        binding.resetButton.visibility = View.GONE
    }

    private fun startTimer(secs: Long) {
        timer = object : CountDownTimer(secs, 1000) {
            override fun onTick(p0: Long) {
                milli_secs = p0
                updateUI()
            }

            override fun onFinish() {
                loadConfeti()
            }
        }
        timer.start()
        binding.startStopButton.text = "Stop"
        isRunning = true
        binding.resetButton.visibility = View.VISIBLE
    }

    private fun updateUI() {

        val h = milli_secs/1000 /3600
        val m = (milli_secs/1000) /60 % 60
        val s = milli_secs/1000 % 60

        binding.timeView.text = "$h:$m:$s"
    }

    private fun loadConfeti() {
        konfettiView = binding.viewKonfetti
        konfettiView.start(explode())
    }

    private fun explode(): List<Party> {
        return listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )
        )
    }

    private fun pauseTimer() {
        binding.startStopButton.text = "Start"
        isRunning = false
        timer.cancel()
        binding.resetButton.visibility = View.VISIBLE
    }
}
