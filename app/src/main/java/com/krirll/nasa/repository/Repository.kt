package com.krirll.nasa.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository
@Inject constructor(
    private val remote: Remote
){
    fun getRemoteService() = remote.getService()
}