package com.example.safespace.entity


class User(val userId: String, val name: String, val email: String){
    constructor():this("","","")
}