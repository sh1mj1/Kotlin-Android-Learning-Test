package com.example.learningtest.util

import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun captureOutput(block: () -> Unit): String {
    val outputStream = ByteArrayOutputStream()
    val printStream = PrintStream(outputStream)
    val originalOut = System.out
    System.setOut(printStream)

    try {
        block()
    } finally {
        System.setOut(originalOut)
    }

    return outputStream.toString().trim()
}
