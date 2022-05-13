package com.ll.whatsup.model

class Account {

    var profileName = String()
    var phoneNumber = String()
    var bio = String()
    var profilePicture = String()
    var allChats = ArrayList<Chat>()
    var status = ArrayList<Status>()

}