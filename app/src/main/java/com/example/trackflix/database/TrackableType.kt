package com.example.trackflix.database

enum class TrackableType(val value: String){
    BOOK("Book"), MOVIE("Movie"), SERIES("Series"), GAME("Game");

    companion object {
        infix fun from(value: String): TrackableType? = TrackableType.values().firstOrNull { it.value == value }
    }

}