package com.todoapp.kitamarket

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.todoapp.kitamarket.databinding.ActivityCustomerCreationBinding

class CustomerCreationActivity : AppCompatActivity(), TextWatcher {

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityCustomerCreationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val fullNameEditText = findViewById<EditText>(R.id.create_account_full_name_input)
        val personalNumberEditText = findViewById<EditText>(R.id.create_account_personal_number_input)
        val phoneNumberEditText = findViewById<EditText>(R.id.create_account_phone_number_input)

        val registerButton = findViewById<Button>(R.id.register_button)

        fullNameEditText.addTextChangedListener(this)
        personalNumberEditText.addTextChangedListener(this)
        phoneNumberEditText.addTextChangedListener(this)

        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val personalNumber = personalNumberEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            val phoneNumberValidationResult =
                Validator.getPhoneNumberValidationResult(this,phoneNumber)
            val fullNameValidationResult =
                Validator.getFullNameValidationResult(this,fullName)
            val personalNumberValidationResult =
                Validator.getPersonalNumberValidationResult(
                    this,
                    personalNumber)


            if (personalNumberValidationResult == getString(R.string.valid)&&
                phoneNumberValidationResult == getString(R.string.valid) &&
                fullNameValidationResult == getString(R.string.valid)) {

                createNewCustomer(fullName, personalNumber, phoneNumber)

            }else{
                if (personalNumberValidationResult != getString(R.string.valid)) {

                    personalNumberEditText.error = personalNumberValidationResult
                    personalNumberEditText.requestFocus()
                }

                if (phoneNumberValidationResult != getString(R.string.valid)) {
                    phoneNumberEditText.error = phoneNumberValidationResult
                    phoneNumberEditText.requestFocus()
                }

                if (fullNameValidationResult != getString(R.string.valid)){
                    fullNameEditText.error = fullNameValidationResult
                    fullNameEditText.requestFocus()
                }

                return@setOnClickListener
            }
        }
    }

    private fun createNewCustomer(fullName: String,
                                  personalNumber: String,
                                  phoneNumber: String) {
        Db.createCustomer(fullName,phoneNumber,personalNumber){ completionResult->

            if(completionResult == R.string.successed){
                Toast.makeText(this, R.string.created_successfully, Toast.LENGTH_SHORT).show()

                if(completionResult == R.string.successed){
                    val intent = Intent(this, CustomerInformation::class.java)
                    intent.putExtra(Constant.PERSONAL_NUMBER_EXTRA,personalNumber)
                    intent.putExtra(Constant.FULL_NAME_EXTRA,fullName)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, completionResult, Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, completionResult, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        findViewById<Button>(R.id.register_button).isEnabled =
            findViewById<EditText>(R.id.create_account_full_name_input).text.trim().isNotEmpty() &&
            findViewById<EditText>(R.id.create_account_personal_number_input).text.trim().isNotEmpty() &&
            findViewById<EditText>(R.id.create_account_phone_number_input).text.trim().isNotEmpty()
    }
}


