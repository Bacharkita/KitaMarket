package com.todoapp.kitamarket

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly

@RequiresApi(Build.VERSION_CODES.O_MR1)
object  Validator{
    const val PERSONAL_NUMBER_REQUIRED_LENGTH = 12
    const val MIN_FULL_NAME_LENGTH = 5
    const val MAX_FULL_NAME_LENGTH = 25
    const val MIN_PHONE_NUMBER_LENGTH = 7
    const val MAX_PHONE_NUMBER_LENGTH = 11
    const val MIN_ARTICLE_NAME_LENGHT = 2
    const val MAX_ARTICLE_NAME_LENGTH = 15
    const val MAX_NOTE_LENGTH = 255
    const val MAX_PRICE_LENGTH = 10


    fun getPersonalNumberValidationResult(requerContext:Activity,personalNumber: String):String {
        fun getString(int : Int ):String{
            return requerContext.getString(int)
        }

        if (personalNumber.length != PERSONAL_NUMBER_REQUIRED_LENGTH &&
            !personalNumber.isDigitsOnly()) {

            val completionResult = getString(R.string.wrong_personal_number_length)
                .plus(PERSONAL_NUMBER_REQUIRED_LENGTH.toString())
                .plus(getString(R.string.digits))
                .plus(getString(R.string.join_word_and))
                .plus(getString(R.string.digits_only_required))

            return completionResult

        } else if (personalNumber.length != PERSONAL_NUMBER_REQUIRED_LENGTH) {
            return getString(R.string.wrong_personal_number_length)
                .plus(PERSONAL_NUMBER_REQUIRED_LENGTH.toString())
                .plus(getString(R.string.digits))

        } else if (!personalNumber.isDigitsOnly()) {
            return getString(R.string.personal_number_only_digits_requerd)
        }

        return getString(R.string.valid)
    }

    fun getFullNameValidationResult(requerContext:Activity,fullName:String):String{
        fun getString(int : Int ):String{
            return requerContext.getString(int)
        }

        if (fullName.length < MIN_FULL_NAME_LENGTH){
            val shortFullName = getString(R.string.short_full_name)
                .plus(MIN_FULL_NAME_LENGTH.toString())
                .plus(getString(R.string.chars_lable))

            return shortFullName

        }else if(fullName.length > MAX_FULL_NAME_LENGTH){
            val longFullName = getString(R.string.long_full_name)
                .plus(MAX_FULL_NAME_LENGTH.toString())
                .plus(getString(R.string.chars_lable))

            return longFullName

        }

        return getString(R.string.valid)
    }

    fun getPhoneNumberValidationResult(requerContext:Activity, phoneNumber:String):String{
        fun getString(int : Int ):String{
            return requerContext.getString(int)
        }

        if(!phoneNumber.isDigitsOnly()){

            return  getString(R.string.digits_only_required)

        }else if(phoneNumber.length > MAX_PHONE_NUMBER_LENGTH){
            val longPhoneNumber = getString(R.string.long_phone_number)
                .plus(MAX_PHONE_NUMBER_LENGTH.toString())
                .plus(getString(R.string.digits))
            return longPhoneNumber

        }else if(phoneNumber.length < MIN_PHONE_NUMBER_LENGTH){
            val shortPhoneNumber = getString(R.string.short_phone_number)
                .plus(MIN_PHONE_NUMBER_LENGTH.toString())
                .plus(getString(R.string.digits))
            return shortPhoneNumber
        }else{
            return getString(R.string.valid)

        }
    }

    fun getArticleNameValidationResult(requerContext:Activity,artickeName:String):String{
        fun getString(int : Int ):String{
            return requerContext.getString(int)
        }

        when {
            artickeName.length < MIN_ARTICLE_NAME_LENGHT -> {
                val shortArticleName = getString(R.string.short_Article_name)
                    .plus(MIN_ARTICLE_NAME_LENGHT.toString())
                    .plus(getString(R.string.chars_lable))
                return shortArticleName

            }
            artickeName.length > MAX_ARTICLE_NAME_LENGTH -> {
                val longtArticleName = getString(R.string.long_Article_name)
                    .plus(MAX_ARTICLE_NAME_LENGTH.toString())
                    .plus(getString(R.string.chars_lable))
                return longtArticleName

            }
            else -> {
                return getString(R.string.valid)
            }
        }
    }

    fun getArticleNoteValidationResult(requerContext:Activity, note: String): String{
        fun getString(int : Int ):String{
            return requerContext.getString(int)
        }

        if (note.length > MAX_NOTE_LENGTH) {
            val longNote = getString(R.string.long_note)
                .plus(MAX_NOTE_LENGTH.toString())
                .plus(getString(R.string.chars_lable))
            return longNote
        }
        return getString(R.string.valid)
    }

    fun getArticlePriceValidationResult(requerContext:Activity, price:String):String{
        fun getString(int : Int ):String{
            return requerContext.getString(int)
        }

        if(price.isEmpty()){
            return getString(R.string.price_required)
        }

        if (price.length > MAX_PRICE_LENGTH) {
            val longPrice = getString(R.string.long_prics)
                .plus(MAX_PRICE_LENGTH.toString())
                .plus(getString(R.string.chars_lable))
            return longPrice
        }
        return getString(R.string.valid)
    }
}
