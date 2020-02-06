package com.github.RezaKardoost.shirazuinternet

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class ShirazUInternet : Application() {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/vazir.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build())
    }
}