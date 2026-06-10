package com.muslima.yya

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform