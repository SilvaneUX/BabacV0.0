package com.smart.smartbalibackpaker.core.model.personalchat

data class DataUser(
    var id: String,
    var username: String,
    var email: String,
    var image: String
) {
    constructor() : this("", "", "", "") {

    }
}
