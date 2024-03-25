package com.todoapp.kitamarket



class Customer {

    var id: String? = null
    var name: String? = null
    var phone: String? = null
    var personalNumber: String? = null
    var balance: Double ?= 0.0



    constructor() {
        }

    constructor(id: String,
                name: String,
                phone: String,
                personalNumber: String,
                balance:Double) {

        this.id = id
        this.name = name
        this.phone = phone
        this.personalNumber = personalNumber
        this.balance = balance
    }

    constructor(id: String,
                name: String,
                phone: String,
                personalNumber: String, ) {

        this.id = id
        this.name = name
        this.phone = phone
        this.personalNumber = personalNumber
        this.balance = 0.0
    }
}