package com.example.testproject.ext

fun String.numericFormatting(previousInput: String): String {
    var input = this
    return if (input.isEmpty() || (previousInput == "0" && input == "0")) {
        "0"
    } else if (input.startsWith("0")) {
        input.removePrefix("0")
    } else {
        if (previousInput == "0") {
            input = input.removeSuffix("0")
        }
        input
    }
}