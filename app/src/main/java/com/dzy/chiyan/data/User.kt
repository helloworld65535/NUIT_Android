package com.dzy.chiyan.data

data class User(
    var id: Int? = null,
    var username: String? = null,
    var password: String? = null
) {
    fun canLogin(): Boolean {
        return username != null && password != null
    }
}
