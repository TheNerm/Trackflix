package com.example.trackflix.database

enum class TrackableType(val value: String){
    BOOK("book"), MOVIE("movie"), SERIES("series"), GAME("game");

    companion object {
        infix fun from(value: String): TrackableType? = TrackableType.values().firstOrNull { it.value == value }
    }

}