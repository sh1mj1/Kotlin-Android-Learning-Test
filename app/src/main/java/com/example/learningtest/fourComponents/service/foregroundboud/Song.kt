package com.example.learningtest.fourComponents.service.foregroundboud

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.learningtest.R

data class Track(
    val name: String,
    val desc: String,
    @RawRes val id: Int,
    @DrawableRes val image: Int,
) {
    constructor() : this("", "", R.raw.one, R.drawable.ic_artist)
}

val songs =
    listOf(
        Track(
            name = "First song",
            desc = "First song description",
            id = R.raw.one,
            image = R.drawable.ic_artist,
        ),
        Track(
            name = "Second song",
            desc = "Second song description",
            id = R.raw.two,
            image = R.drawable.ic_artist,
        ),
        Track(
            name = "Third song",
            desc = "Third song description",
            id = R.raw.three,
            image = R.drawable.ic_artist,
        ),
        Track(
            name = "Fourth song",
            desc = "Fourth song description",
            id = R.raw.four,
            image = R.drawable.ic_artist,
        ),
    )
