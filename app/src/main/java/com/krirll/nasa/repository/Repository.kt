package com.krirll.nasa.repository

import javax.inject.Inject


class Repository
@Inject constructor(
    private val remote: Remote
){
    fun getRemoteService() = remote.getService()
}