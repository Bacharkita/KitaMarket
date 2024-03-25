package com.todoapp.kitamarket

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.todoapp.kitamarket.databinding.ActivityCreateArticleBinding

class ArticleCreationActivity : AppCompatActivity() {
    var personalNumber: String? = null

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityCreateArticleBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        personalNumber = intent.getStringExtra(Constant.PERSONAL_NUMBER_EXTRA)
        val nameInput = viewBinding.createArticleNameInput
        val priceInput = viewBinding.createArticlePriceInput
        val noteInput = viewBinding.createArticleNoteInput

        viewBinding.addArticleButton.setOnClickListener{

            val name = nameInput.text.toString().trim()
            val price = priceInput.text.toString().trim()
            val note = noteInput.text.toString().trim()

            val articleNameValidationResult =
                Validator.getArticleNameValidationResult(
                    this,
                    name)
            val articlePriceValidationResult =
                Validator.getArticlePriceValidationResult(
                    this
                    ,price)
            val articleNoteValidationResult =
                Validator.getArticleNoteValidationResult(
                    this,
                    note)

            if (articleNameValidationResult == getString(R.string.valid) &&
                    articlePriceValidationResult == getString(R.string.valid) &&
                    articleNoteValidationResult == getString(R.string.valid)) {

                val id = Db.createArticle(name,note,personalNumber!!,price.toDouble())
                val intent = Intent(this, QRGeneratorActivity::class.java)
                intent.putExtra(Constant.ARTICLE_ID_EXTRA,id)
                intent.putExtra(Constant.PERSONAL_NUMBER_EXTRA,personalNumber)
                startActivity(intent)
                finish()

            }else{

            if (articleNameValidationResult != getString(R.string.valid)) {
                nameInput.error = articleNameValidationResult
                nameInput.requestFocus()
            }

            if (articlePriceValidationResult != getString(R.string.valid)) {
                priceInput.error = articlePriceValidationResult
                priceInput.requestFocus()
            }

            if (articleNoteValidationResult != getString(R.string.valid)){
                noteInput.error = articleNoteValidationResult
                noteInput.requestFocus()
            }

            return@setOnClickListener

            }
        }
    }
}

