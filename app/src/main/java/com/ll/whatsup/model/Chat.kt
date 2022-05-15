package com.ll.whatsup.model

data class Chat(var accountNum: String = "", var chatName: String = "", var messages: ArrayList<Message> = ArrayList()) {

}