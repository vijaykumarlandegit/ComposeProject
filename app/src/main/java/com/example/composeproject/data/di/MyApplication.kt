package com.example.composeproject.data.di

import android.app.Application
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application()
//trigger Hilt's code generation and set up the dependency container at the application level.