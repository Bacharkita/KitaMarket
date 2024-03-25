package com.todoapp.kitamarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.todoapp.kitamarket.databinding.ActivityCustomerInfoBinding
class CustomerInfoActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityCustomerInfoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val balance = intent.getStringExtra(Constant.BALANCE_EXTRA)
        val fullName = intent.getStringExtra(Constant.FULL_NAME_EXTRA)
        val phoneNumber = intent.getStringExtra(Constant.PHONE_NUMBER_EXTRA)
        val personalNumber = intent.getStringExtra(Constant.PERSONAL_NUMBER_EXTRA)

        viewBinding.customerInfoPersonalNumberValue.text = personalNumber
        viewBinding.customerInfoFullNameValue.text = fullName
        viewBinding.createAccountPhoneNumberValue.text = phoneNumber
        viewBinding.customerInfoBalanceValue.text = balance
    }
}