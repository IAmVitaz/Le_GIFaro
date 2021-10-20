package com.vitaz.gifaro

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class MainApplication: Application() {

    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Fresco.initialize(applicationContext)
    }

    fun getAppContext(): Context? {
        return this.applicationContext
    }
}
