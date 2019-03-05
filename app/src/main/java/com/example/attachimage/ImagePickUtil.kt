package com.example.attachimage

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import java.io.File

//Created by Dasun Athapaththu on 01,March,2019
object ImagePickUtil {

    fun getPickImageIntent(context: Context, title: String): Intent {
        var chooserIntent = Intent()
        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val tackPhotoIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra("return_data", true)
            .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)))
        intentList = addIntentToList(context, intentList, pickIntent)
        intentList = addIntentToList(context, intentList, tackPhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(intentList.removeAt(intentList.size - 1), title)
                .putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray<Parcelable>())
        }
        return chooserIntent
    }

    //private fun toArray(list: MutableList<I>){
//
//}
    private fun addIntentToList(context: Context, _list: MutableList<Intent>, intent: Intent): MutableList<Intent> {
        val list = _list.toMutableList()
        val resInfo: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, 0)
        for (item in resInfo) {
            val packageName: String = item.activityInfo.packageName
            val targetIntent: Intent = Intent(intent)
                .setPackage(packageName)
            list += targetIntent
        }
        return list
    }

    fun getTempFile(context: Context): File {
        val imageFile = File(context.externalCacheDir, "chacheImage")
        imageFile.parentFile.mkdirs()
        return imageFile
    }
}