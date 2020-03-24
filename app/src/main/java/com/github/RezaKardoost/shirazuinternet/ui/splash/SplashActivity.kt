package com.github.RezaKardoost.shirazuinternet.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.RezaKardoost.shirazuinternet.R
import com.github.RezaKardoost.shirazuinternet.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animation_view.post {
            animation_view.setColorFilter(Color.parseColor("#000000"),PorterDuff.Mode.SRC_ATOP)
        }

        lifecycleScope.launch {
            delay(3000)
            launchMainActivity()
        }
    }

    private fun launchMainActivity() {
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


}
