package com.todoapp.kitamarket


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import com.todoapp.kitamarket.databinding.ActivitySellBinding

class SellActivity : AppCompatActivity() {

    companion object{
        var articlesList: ArrayList<Article> = ArrayList()

        fun isArticleInserted(articleId: String):Boolean{
            for(article in articlesList){
                if(article.id == articleId){
                    return true
                }
            }
            return false
        }
    }
    var totalSumTextView:TextView? = null
    private val sellVM: SellVM by viewModels()
    private val articlesFragmentVM: ArrayListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val viewBinding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sellSearchBar = viewBinding.sellSearchBar

        totalSumTextView = viewBinding.sellTotalValueTextView

        if(savedInstanceState == null){
            articlesFragmentVM.artilcesList = articlesList!!
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.sell_list_fragment_container,
                    ArticlesFragment.newInstance()
                )
                .commit()

            updateTotalSum()

        }else{
            articlesFragmentVM.artilcesList = articlesList!!
            replaceFragment()
            updateTotalSum()

            if(sellVM.errorMessage.isNotEmpty()){
                buildErrorList()
            }

        }

        articlesFragmentVM.run {
            articleDataFromLongClick.observe(this@SellActivity) {

                if (it != null) {
                    buildAlertDialog(it)

                }
            }
        }

        viewBinding.sellAddButton.setOnClickListener {

            val id = sellSearchBar.text.toString().trim()

            if (id.isNotEmpty()){

                if (!isArticleInserted(id)){
                    Db.getArticle(id){article, completionResult->

                        if(completionResult == R.string.successed){
                            articlesList?.add(article!!)
                            replaceFragment()
                            updateTotalSum()
                            sellSearchBar.text.clear()
                        }else{
                            Toast.makeText(
                                this,
                                completionResult,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }else{
                    Toast.makeText(
                        this,
                        getString(R.string.article_exist),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    this,
                    getString(R.string.article_Id_required),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        viewBinding.scanButton.setOnClickListener {
            val intent = Intent(this, QRScanActivity::class.java)
            startActivity(intent)
        }

        viewBinding.submitSell.setOnClickListener {

            submitSell(){  submitedArticleList,completionResult ->

                if (completionResult != getString(R.string.successed)){
                    sellVM.sunmitedArticleList = submitedArticleList
                    sellVM.errorMessage = completionResult
                    buildErrorList()
                }else{
                    articlesFragmentVM.artilcesList.clear()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        articlesList.clear()
    }

    private fun updateTotalSum(){

        var totalSum = 0.0
        for(article in articlesList!!){
            if (article.price != null){
                totalSum += article.price!!
            }
        }
        totalSumTextView?.text = totalSum.toString()
    }

    private fun replaceFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.sell_list_fragment_container,
                ArticlesFragment.newInstance()
            )
            .commit()
    }

    private fun submitSell(callback:(ArrayList<String>, String)->Unit){

        val sunmitedArticleList : ArrayList<String> = ArrayList()

        fun processNext(){

            if(articlesList.isEmpty()){
                callback(sunmitedArticleList,getString(R.string.successed))
                return
            }

            val article = articlesList.removeFirst()

            if (article.soldBy.isNullOrEmpty() ){
                val  marketAmount = article.price!! * Constant.COMMISSION_PERCENT
                val customerAmount = article.price!! - marketAmount

                Db.toggleArticleIsSold(article){ completionResult ->

                    if(completionResult == R.string.successed){
                        Db.updateCustomerBalance(article.owner!!,customerAmount)
                        { completionResult->

                            if(completionResult == R.string.successed){
                                sunmitedArticleList.add(article.id!!)

                                processNext()

                            }else{
                                val errorsMessage = getString(R.string.no_balance_added)

                                callback(sunmitedArticleList,errorsMessage)
                            }
                        }

                    }else{
                        callback(sunmitedArticleList,getString(R.string.database_error))
                    }
                }

            }else{
                  callback(sunmitedArticleList, getString(R.string.sold_article_error))
            }
        }
        processNext()
        replaceFragment()
        updateTotalSum()
    }

    private fun buildErrorList(){

        val alertBuilder = android.app.AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.article_ids_list_view, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.submited_articles_recycler_view)
        recyclerView.adapter = SubmitedArticlesListAdapter(sellVM.sunmitedArticleList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val errorViewTitle = view.findViewById<TextView>(R.id.error_label_text_view)
        val errorViewMessage = view.findViewById<TextView>(R.id.error_message_text_view)

        errorViewTitle.setText(getString(R.string.sell_error_message_label))
        errorViewMessage.setText(sellVM.errorMessage)

        val alertDialog = alertBuilder.create()
        val okButton = view.findViewById<Button>(R.id.error_list_ok_button)
        okButton.setOnClickListener {

            sellVM.sunmitedArticleList.clear()
            sellVM.errorMessage = Constant.EMPTY_STRING
            alertDialog.dismiss()
        }

        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun buildAlertDialog(article: Article){
         AlertDialog.Builder(this)
            .setTitle(R.string.delete_alarm_title)
            .setMessage(R.string.alarm_message)
            .setPositiveButton(R.string.alarm_yes) { dialog, whichButton ->
                articlesFragmentVM.artilcesList.remove(article)
                sellVM.errorMessage = Constant.EMPTY_STRING
                sellVM.sunmitedArticleList.clear()
                replaceFragment()
                updateTotalSum()

            }.setNegativeButton(R.string.alarm_no) { dialog, whichButton ->
                sellVM.errorMessage = Constant.EMPTY_STRING
                sellVM.sunmitedArticleList.clear()

            }.setCancelable(false)
            .show()
        sellVM.alertDialogArticle = article


    }
}

class SellVM() : ViewModel(){
    var sunmitedArticleList:ArrayList<String> = ArrayList()
    var errorMessage = Constant.EMPTY_STRING
    var alertDialogArticle: Article? = null
}

