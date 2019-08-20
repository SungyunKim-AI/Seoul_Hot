package com.inseoul

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*


class TestActivity :
    AppCompatActivity()
{

    val apiKey = "AIzaSyArBMJ-s5uzGsRCNNyon9LeQsXDgCDmcTI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)

        // Create a new Places client instance
        val placesClient = Places.createClient(this)







        // Define a Place ID.
        val placeId = "ChIJYxd6fL6ifDURy7wm894BUcQ"

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        val fields = Arrays.asList(Place.Field.PHOTO_METADATAS)

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)

        placesClient.fetchPlace(placeRequest).addOnSuccessListener { response ->
            val place = response.place

            // Get the photo metadata.
            val photoMetadata = place.photoMetadatas!![0]
            Log.v("Image Size", place!!.photoMetadatas!!.size.toString())
            // Get the attribution text.
            val attributions = photoMetadata.attributions

            // Create a FetchPhotoRequest.
            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500) // Optional.
                .setMaxHeight(300) // Optional.
                .build()
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener { fetchPhotoResponse ->
                val bitmap = fetchPhotoResponse.bitmap
                test_img.setImageBitmap(bitmap)
                test_text.text = attributions

            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    // Handle error with given status code.
//                    Log.e(FragmentActivity.TAG, "Place not found: " + exception.getMessage())
                }
            }
        }
    }
}
