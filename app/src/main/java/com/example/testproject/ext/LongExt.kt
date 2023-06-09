package com.example.testproject.ext

fun Long.findDifferenceWithCurrentTimeInMinute(): Long =
    (System.currentTimeMillis() - this) / 60000