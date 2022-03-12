package com.example.mchomework.profile

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.*
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.navigation.NavController
import com.example.mchomework.Graph
import com.example.mchomework.MainActivity
import com.example.mchomework.R
import java.io.File
import java.io.IOException
import java.util.*

@Composable
fun Profile(
    navController: NavController,
    activity: Activity
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.profile),
                    modifier = Modifier.padding(10.dp)
                )
            }
            val img = BitmapFactory.decodeFile(findFile())
            if (img != null) {
                Image(
                    bitmap = img.asImageBitmap(),
                    contentDescription = "Profile picture",
                    modifier = Modifier.size(300.dp)
                        .rotate(90f)
                        .padding(10.dp)
                )
            }
            else {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Profile picture",
                    modifier = Modifier.size(300.dp)
                )
            }
            Button(
                onClick = { takePicture(navController, activity) }
            ) {
            Text(
                text = "Take a picture"
            )
            }
        }
    }
}


fun takePicture(navController: NavController, activity: Activity) {
    deleteFile()
    dispatchTakePictureIntent(activity)
//    navController.navigate("camera")
}




private fun dispatchTakePictureIntent(
    activity: Activity
) {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        // Create the File where the photo should go
        val photoFile: File? = try {
            File.createTempFile("ProfilePicture", ".jpg", Graph.appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                Graph.appContext,
                "com.example.android.fileprovider",
                it
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(activity, takePictureIntent, 1, Bundle())
//            startActivityForResult(takePictureIntent, 1)
        }
    }
//    }
//    takePictureIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//    try {
//        startActivityForResult(activity, takePictureIntent, 1, Bundle())
//
//    } catch (e: ActivityNotFoundException) {
//        // display error state to the user
//    }
}

private fun findFile(): String? {
    val dir = Graph.appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val list = dir?.listFiles()
    if (list != null) {
        if (list.isEmpty()) {
            return null
        }
    }
    return list?.get(0)?.absolutePath
}
private fun deleteFile() {
    val dir = File(findFile())
    dir?.delete()
}