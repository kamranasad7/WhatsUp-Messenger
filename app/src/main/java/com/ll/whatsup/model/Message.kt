package com.ll.whatsup.model


import java.util.*

data class Message(var text: String = "", var time: Date = Date(), var senderNum: String  = "", var receiver: String = "") {
    var id: Long = -1
}
