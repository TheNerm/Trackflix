package com.example.trackflix.fragments.list

import com.example.trackflix.database.TrackableType

enum class SortingCriteria(val value:String) {
    CREATIONDATE("creationDate"), PRIORITY("priority"), COMPLETION("completion"), HOURSSPENT("hoursSpent"), EXPECTEDHOURS("expectedHours");

    companion object {
        infix fun from(value: String): TrackableType? = TrackableType.values().firstOrNull { it.value == value }
    }
}