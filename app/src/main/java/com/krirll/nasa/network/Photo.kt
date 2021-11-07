package com.krirll.nasa.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Photo : Serializable {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("hdurl")
    @Expose
    var imageUrl: String? = null

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
    explanation = "Are galaxies giant magnets? Yes, but the magnetic fields in galaxies are..."
    author = "Unknown" or "Ruslan Photography"
    date = "2021.11.06"
}
*/