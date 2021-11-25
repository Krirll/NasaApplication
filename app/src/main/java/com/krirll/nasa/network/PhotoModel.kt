package com.krirll.nasa.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PhotoModel : Serializable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("url")
    @Expose
    var imageUrl: String? = null

    @SerializedName("hdurl")
    @Expose
    var hdImageUrl: String? = null

    @SerializedName("thumbnail_url")
    @Expose
    var imageOfVideoUrl: String? = null

    @SerializedName("explanation")
    @Expose
    var explanation: String? = null

    @SerializedName("copyright")
    @Expose
    var author: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null
}


//example
/*
{
    title = "The Central Magnetic Field of the Cigar Galaxy"
    imageUrl = "https://apod.nasa.gov/apod/image/1903/M82Magnet_SOFIA_2412.jpg"
    hdImageUrl = "https://apod.nasa.gov/apod/image/1903/HDM82Magnet_SOFIA_2412.jpg" or null
    imageOfVideoUrl = "https://apod.nasa.gov/apod/image/1903/M82Magnet_SOFIA_2412.jpg"
    explanation = "Are galaxies giant magnets? Yes, but the magnetic fields in galaxies are..."
    author = "Unknown" or "Ruslan Photography"
    date = "2021-11-06"
}
*/