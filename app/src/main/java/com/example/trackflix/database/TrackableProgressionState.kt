package com.example.trackflix.database

enum class TrackableProgressionState(val value:String) {
    BACKLOG("backlog"), IN_PROGRESS("inProgress"), FINISHED("finished"), CANCELLED("cancelled");

    companion object {
        infix fun from(value: String): TrackableProgressionState? = TrackableProgressionState.values().firstOrNull { it.value == value }
    }
}