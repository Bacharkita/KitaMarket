package com.todoapp.kitamarket

import android.annotation.SuppressLint
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat
import java.util.*

object Db {
    private const val customersPath = "Customers"
    private const val articlesPath = "Articles"
    private const val OWNER_KEY = "owner"
    var seller = "Ayh"
    @SuppressLint("StaticFieldLeak")
    private val database = FirebaseFirestore.getInstance()


    fun createCustomer(name:String,
                       phone:String,
                       personalNumber:String,
                       callback:(Int)->Unit){
        getCustumer(personalNumber){ customer, completionResult ->
            when (completionResult) {
                R.string.successed -> {
                    callback(R.string.custome_exist)
                }
                R.string.database_error -> {
                    callback(completionResult)

                }
                R.string.custome_not_exist -> {
                    val customer = Customer(personalNumber,name,phone,personalNumber)

                    database.collection(customersPath).document(personalNumber)
                        .set(customer)
                        .addOnSuccessListener {
                                    callback(R.string.successed)
                        }

                        .addOnFailureListener { exception ->

                            callback(R.string.database_error)
                        }
                }
            }
        }
    }

    fun updateCustomer(customer: Customer,callback:(Int)->Unit){

        database.collection(customersPath).document(customer.id!!)
            .set(customer)
            .addOnSuccessListener {

                        callback(R.string.successed)
            }

            .addOnFailureListener { exception ->

                callback(R.string.database_error)
            }
    }

    fun updateCustomerBalance(personalNumber:String,
                              balance:Double,
                              callback:(Int)->Unit){

        getCustumer(personalNumber){ customer, completionResult ->

            if(completionResult == R.string.successed && customer.id != null){
                val newBlance = customer.balance!! + balance
                val newCustomer = Customer(customer.id!!,
                                            customer.name!!,
                                            customer.phone!!,
                                            customer.personalNumber!!
                                            ,newBlance)
                updateCustomer(newCustomer){ completionResult ->
                        callback(completionResult)
                }

            }else if( completionResult == R.string.custome_not_exist){
                callback(R.string.custome_not_exist)

            }else {
                callback(completionResult)
            }
        }
    }

    fun getCustumer(personalNumber: String,callback: (Customer,Int)->Unit){

        val docRef = database.collection(customersPath).document(personalNumber)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val customer = document.toObject(Customer::class.java)

                    callback(customer!!,R.string.successed)

                } else {
                    callback(Customer(), R.string.custome_not_exist)
                }
            }
            .addOnFailureListener { exception ->
                callback(Customer(), R.string.database_error)
            }
    }


    fun createArticle(title:String,note:String,owner: String,price:Double): String{
        val timestamp = getCurrentSimpleFormatTime()
        val id = getCurrentTimeMillies()
        val article = Article(id,title,note,timestamp,owner,price)

        database.collection(articlesPath).document(id)
            .set(article)
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->

            }
        return id
    }


    fun updateAtricle(id:String,title:String,note:String,owner: String,price:Double){

        val article = Article(id,title,note,getCurrentSimpleFormatTime(),owner,price)

        database.collection(articlesPath).document(id)
            .set(article)
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->

            }

    }

    fun deleteArticle(id:String){
        database.collection(articlesPath).document(id)
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->

            }
    }

    fun getArticle(id:String,callback: (Article, Int)->Unit) {
        val docRef = database.collection(articlesPath).document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val article = document.toObject(Article::class.java)

                    callback(article!!,R.string.successed)

                } else {
                    callback(Article(),R.string.article_not_exist)
                }
            }
            .addOnFailureListener { exception ->
                callback(Article(),R.string.database_error)
            }
    }

    fun toggleArticleIsSold(article : Article, callback: (Int)->Unit){

        if (article.soldBy == null){
            article.soldBy = seller
        }else{
            article.soldBy = null
        }

        database.collection(articlesPath).document(article.id!!)
            .set(article)
            .addOnSuccessListener {

                callback (R.string.successed)
            }

            .addOnFailureListener { exception ->
                callback (R.string.database_error)
            }
    }

    fun getQueryCustomerArticles(personalNumber: String): Query {
       return database
           .collection(articlesPath)
           .whereEqualTo(OWNER_KEY,personalNumber)
    }

    private fun getCurrentSimpleFormatTime(): String {
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("EEEE dd:hh:mm a ")
        val stringTime = mdformat.format(calendar.time)

        return stringTime
    }

    private fun getCurrentTimeMillies(): String {
        val calendar = Calendar.getInstance()
        val stringTime = calendar.timeInMillis.toString()

        return stringTime
    }
}

