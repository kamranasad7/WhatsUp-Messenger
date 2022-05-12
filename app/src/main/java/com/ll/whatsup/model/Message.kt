package com.ll.whatsup.model

import java.sql.Time

class Message {

    var id: Long = -1
    var text: String? = null
    var time: Time? = null
    var sender: Account? = null
    var receiver: Account? = null
}
