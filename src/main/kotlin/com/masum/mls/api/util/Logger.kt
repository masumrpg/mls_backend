package com.masum.mls.api.util

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Extension function supaya mirip anotasi
 * Tinggal tulis: private val log = logger()
 */
inline fun <reified T : Any> T.logger(): KLogger =
    KotlinLogging.logger(T::class.java.name)