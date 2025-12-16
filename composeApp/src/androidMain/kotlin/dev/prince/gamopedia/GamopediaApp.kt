package dev.prince.gamopedia

import android.app.Application
import dev.prince.gamopedia.di.initKoin
import org.koin.android.ext.koin.androidContext

class GamopediaApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GamopediaApp)
        }
    }

}