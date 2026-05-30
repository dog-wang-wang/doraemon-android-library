package com.doraemon.dora

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.doraemon.foundation_ui_view.screen.activity.CheckTableComponentActivity
import com.doraemon.foundation.tips.toastShort
import com.doraemon.foundation.utils.debugWithEvent

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
        toastShort("竖屏")
//        Toast.makeText(this, "竖屏", Toast.LENGTH_SHORT).show()
        debugWithEvent("test事件","OK")
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