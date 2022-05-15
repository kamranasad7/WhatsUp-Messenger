package com.ll.whatsup.model

data class Account(var profileName: String = "", var number: String = "", var bio: String = "", var profilePicture: String = "") {
    var status: ArrayList<Status> = ArrayList()
    var chats: ArrayList<Chat> = ArrayList()
}