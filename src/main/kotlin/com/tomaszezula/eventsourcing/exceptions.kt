package com.tomaszezula.eventsourcing

class SdkException : Exception {
    constructor(message: String) : super(message)
    constructor(cause: Exception) : super(cause)
}
