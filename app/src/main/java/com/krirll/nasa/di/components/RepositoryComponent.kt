package com.krirll.nasa.di.components

import com.krirll.nasa.di.modules.RepositoryModule
import com.krirll.nasa.repository.Repository
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun getRepository() : Repository
}