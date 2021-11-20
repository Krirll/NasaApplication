package com.krirll.nasa.di.components

import com.krirll.nasa.di.modules.RetrofitModule
import com.krirll.nasa.repository.Repository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface RepositoryComponent {
    fun getRepository() : Repository
}