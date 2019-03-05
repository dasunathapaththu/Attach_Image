package com.example.attachimage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val IMAGE_PICKER_ID = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add_image.setOnClickListener {
            val intent = ImagePickUtil.getPickImageIntent(this, "SELECT IMAGE")
            startActivityForResult(intent, IMAGE_PICKER_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_PICKER_ID) {
            val imageFile = ImagePickUtil.getTempFile(this)
            lateinit var uri: Uri

            val isCamera: Boolean =
                (data == null || data.data == null || data.data!!.toString().contains(imageFile.toString()))

            if (isCamera) { /*Image from Camera*/
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    uri = Uri.fromFile(imageFile)
                    iv_image.setImageURI(uri)
                } else {
                    setSnackBar().show()
                }

            } else { /*Image from Album*/
                uri = data!!.data
                iv_image.setImageURI(uri)
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setSnackBar(): Snackbar {
        val snackBar: Snackbar =
            Snackbar.make(fab_add_image, "Grant Camera Permission.", Snackbar.LENGTH_SHORT)
        snackBar.setActionTextColor(Color.YELLOW)
        snackBar.setAction("Settings") {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                )
            )
        }
        val textView: TextView = snackBar.view.findViewById(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        return snackBar
    }
}
