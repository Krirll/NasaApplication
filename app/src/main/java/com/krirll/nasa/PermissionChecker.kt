package com.krirll.nasa

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.krirll.nasa.ui.MainActivity
import java.io.Serializable

class PermissionChecker : Permission , Serializable {

    override fun checkWriteStoragePermission(context : Context) =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    override fun getPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as MainActivity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            MainActivity.REQ_CODE
        )
    }
}