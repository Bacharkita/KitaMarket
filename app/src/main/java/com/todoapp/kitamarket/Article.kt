package com.todoapp.kitamarket

class Article {
    var id: String? = null
    var name: String? = null
    var note: String? = null
    var timestamp: String? = null
    var owner:String? = null
    var price:Double? = null
    var soldBy:String? = null


    constructor() {


    }

    constructor(id: String, title: String, note: String, timestamp: String,owner:String,price:Double) {
        this.id = id
        this.name = title
        this.note = note
        this.timestamp = timestamp.toString()
        this.owner = owner
        this.price = price
    }


}