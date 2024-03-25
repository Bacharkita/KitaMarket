package com.todoapp.kitamarket

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.todoapp.kitamarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val personalNumberInput = viewBinding.mainSearchBar
        val customerInfoButton = viewBinding.customerInfoButton
        val customerArticlesButton = viewBinding.customerArticlesButton

        fun setOnClick(button: Button){
            val personalNumber = personalNumberInput.text.toString().trim()
            val validationResult =
                Validator.getPersonalNumberValidationResult(
                    this,
                    personalNumber)

            if(validationResult == getString(R.string.valid)){

                Db.getCustumer(personalNumber){customer, completionResult ->

                    if (completionResult == R.string.successed){
                        val fullName = customer.name
                        val balance = customer.balance.toString()
                        val phoneNumber = customer.phone

                        if(button == customerArticlesButton){
                            val intent = Intent(this, CustomerInformation::class.java)
                            intent.putExtra(Constant.PERSONAL_NUMBER_EXTRA,personalNumber)
                            intent.putExtra(Constant.FULL_NAME_EXTRA,fullName)
                            intent.putExtra(Constant.BALANCE_EXTRA,balance)
                            startActivity(intent)

                        }else{
                            val intent = Intent(this, CustomerInfoActivity::class.java)
                            intent.putExtra(Constant.PERSONAL_NUMBER_EXTRA,personalNumber)
                            intent.putExtra(Constant.FULL_NAME_EXTRA,fullName)
                            intent.putExtra(Constant.BALANCE_EXTRA,balance)
                            intent.putExtra(Constant.PHONE_NUMBER_EXTRA,phoneNumber)
                            startActivity(intent)
                        }

                    }else{
                        personalNumberInput.error = getString(completionResult)
                        personalNumberInput.requestFocus()
                    }
                }
            }
            else{
                personalNumberInput.error = validationResult
                personalNumberInput.requestFocus()
            }

            return@setOnClick
        }

        customerInfoButton.setOnClickListener {
            setOnClick(customerInfoButton)

        }

        customerArticlesButton.setOnClickListener{
            setOnClick(customerArticlesButton)
        }

        viewBinding.addCustomerButton.setOnClickListener{
            val intent = Intent(this, CustomerCreationActivity::class.java)
            startActivity(intent)
        }

        viewBinding.newSellButton.setOnClickListener{
            val intent = Intent(this, SellActivity::class.java)
            startActivity(intent)
        }


    }

}




