package com.example.trackflix.fragments.list

enum class SortingCriteria(val value:String) {
    CREATIONDATE("creationDate"), PRIORITY("priority"), ALPHABETICAL("alphabetical"), COMPLETION("completion"), HOURSSPENT("hoursSpent"), EXPECTEDHOURS("expectedHours");

    companion object {
        infix fun from(value: String): SortingCriteria? = SortingCriteria.values().firstOrNull { it.value == value }
    }
}