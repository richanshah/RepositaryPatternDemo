package com.example.legocatalog

import android.app.Application
import com.example.legocatalog.di.AppInjector
import com.example.legocatalog.util.CrashReportingTree
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        //Stetho is for viewing db on chorme for this project
        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        else Timber.plant(CrashReportingTree())

        AppInjector.init(this)
    }
//    activityInjector fragmentInjector has been removed in new version
    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}