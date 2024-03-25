package com.todoapp.kitamarket

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.todoapp.kitamarket.databinding.ActivityCreateArticleBinding

class ArticleUpdatingActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityCreateArticleBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val selectedArticle:Article = CustomerInformation.selectedArticle

        val nameInput = viewBinding.createArticleNameInput
        val priceInput = viewBinding.createArticlePriceInput
        val noteInput = viewBinding.createArticleNoteInput

        nameInput.setText(selectedArticle.name!!)
        priceInput.setText(selectedArticle.price!!.toString())
        noteInput.setText(selectedArticle.note!!)

        val updateButton = viewBinding.addArticleButton
        updateButton.setText(R.string.update_label)
        updateButton.setOnClickListener{

            val id = selectedArticle.id!!.trim()
            val name = nameInput.text.toString().trim()
            val price = priceInput.text.toString().trim()
            val note = noteInput.text.toString().trim()
            val owner = selectedArticle.owner!!.trim()

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

               Db.updateAtricle(id,name,note,owner,price.toDouble())
               nameInput.text.clear()
               priceInput.text.clear()
               noteInput.text.clear()
               finish()

            } else {

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
