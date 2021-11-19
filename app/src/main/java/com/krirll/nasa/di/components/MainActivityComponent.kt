package com.krirll.nasa.di.components

import com.krirll.nasa.common.ViewListener
import com.krirll.nasa.di.modules.MainActivityModule
import dagger.Component

@Component(modules = [MainActivityModule::class])
interface MainActivityComponent {
    fun getFragmentListener() : ViewListener
}