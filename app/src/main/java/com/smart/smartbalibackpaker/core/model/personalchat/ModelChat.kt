package com.smart.smartbalibackpaker.core.model.personalchat

data class ModelChat(
    var message: String,
    var receiver: String,
    var sender: String,
    var timestamp: String,
    var statusBaca: String
) {
    constructor() : this("", "", "", "", "") {

    }
}