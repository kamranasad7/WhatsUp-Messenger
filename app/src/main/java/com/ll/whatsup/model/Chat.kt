package com.ll.whatsup.model

data class Chat(var accountNum: String = "", var chatName: String = "", var messages: HashMap<String, Message> = HashMap()) {

    fun getMessagesList(): ArrayList<Message> {
        val arr = ArrayList<Message>()
        arr.addAll(messages.values)
        return arr
    }

}