package dev.prince.gamopedia

import android.app.Application
import dev.prince.gamopedia.di.initKoin
import dev.prince.gamopedia.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class GamopediaApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GamopediaApp)
        }
    }

}