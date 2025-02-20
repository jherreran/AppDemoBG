package com.joffreherrera.demoapp.utils

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }
