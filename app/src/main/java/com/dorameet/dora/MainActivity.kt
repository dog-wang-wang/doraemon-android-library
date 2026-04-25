package com.dorameet.dora

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dorameet.foundation.screen.activity.CheckTableComponentActivity

class MainActivity : CheckTableComponentActivity() {

    override fun beforeCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPhonePortrait() {
        super.onPhonePortrait()
        Toast.makeText(this, "竖屏", Toast.LENGTH_SHORT).show()
    }

    override fun onPhoneLandScape() {
        super.onPhoneLandScape()
        Toast.makeText(this, "横屏", Toast.LENGTH_SHORT).show()
    }

    override fun onTabletPortrait() {
        Toast.makeText(this, "平板竖屏", Toast.LENGTH_SHORT).show()
    }

    override fun onTabletLandScape() {
        Toast.makeText(this, "平板横屏", Toast.LENGTH_SHORT).show()
    }
}