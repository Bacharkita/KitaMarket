package com.todoapp.kitamarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.todoapp.kitamarket.databinding.ActivityCustomerArticlesBinding
import com.todoapp.kitamarket.fragments.DescriptionFragment
import kotlin.collections.ArrayList


class CustomerInformation : AppCompatActivity() {

    companion object{
        var selectedArticle:Article = Article()
    }

    private val articlesFragmentVM: ArrayListViewModel by viewModels()
    private val customerArticlesVM: CustumerArticlesVM by viewModels()
    var listener: ListenerRegistration? = null
    var articlesToSell: ArrayList<Article>? = null
    var soldArticles: ArrayList<Article>? = null
    var personalNumber: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityCustomerArticlesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val balanc = intent.getStringExtra(Constant.BALANCE_EXTRA)
        val fullName = intent.getStringExtra(Constant.FULL_NAME_EXTRA)
        personalNumber = intent.getStringExtra(Constant.PERSONAL_NUMBER_EXTRA)
        articlesToSell = ArrayList()
        soldArticles = ArrayList()
        viewBinding.customerArticlesBalance.text = balanc
        viewBinding.customerArticlesFullName.text = fullName

        if(savedInstanceState == null){
            articlesFragmentVM.artilcesList = articlesToSell!!
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    ArticlesFragment.newInstance()
                )
                .commit()
        }else{
            if (customerArticlesVM.isDescription ){
                val article = articlesFragmentVM.articleDataFromItemClick.value
                if ( article!= null) {
                    switchFragmentContainer(article)
                }
            }
            if(customerArticlesVM.alertDialogArticleId.isNotEmpty()){
                buildAlertDialog(customerArticlesVM.alertDialogArticleId)
            }
        }

        viewBinding.addNewArticle.setOnClickListener {

            val intent = Intent(this, ArticleCreationActivity::class.java)
            intent.putExtra(Constant.PERSONAL_NUMBER_EXTRA, personalNumber)
            startActivity(intent)
        }

        viewBinding.soldProductsButton.setOnClickListener {

            customerArticlesVM.isSoldArticles = true
            articlesFragmentVM.artilcesList = soldArticles!!
            replaceFragment()
            customerArticlesVM.isDescription = false
        }

        viewBinding.unsoldProductsButton.setOnClickListener {

            customerArticlesVM.isSoldArticles = false
            articlesFragmentVM.artilcesList = articlesToSell!!
            replaceFragment()
            customerArticlesVM.isDescription = false

        }

        articlesFragmentVM.run {
            articleDataFromItemClick.observe(this@CustomerInformation) {
                if (it != null) {
                    switchFragmentContainer(it)
                    customerArticlesVM.isDescription = true
                }
            }
        }

        articlesFragmentVM.run {
            articleDataFromLongClick.observe(this@CustomerInformation) {

                if (it != null) {
                    buildOptionsDialog(it)

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val querySnapshot = Db.getQueryCustomerArticles(personalNumber!!)

        listener = querySnapshot.addSnapshotListener {
                querySnapshot: QuerySnapshot?,
                firebaseFirestoreException: FirebaseFirestoreException? ->

                sortArticles(querySnapshot)
                articlesFragmentVM.artilcesList =
                    if (customerArticlesVM.isSoldArticles) soldArticles!! else articlesToSell!!

            if (customerArticlesVM.isDescription ) {
                val article = articlesFragmentVM.articleDataFromItemClick.value
                if (article != null) {
                    switchFragmentContainer(article)
                }
            }else{
                replaceFragment()

            }

        }
    }

    override fun onPause() {
        super.onPause()
        listener?.remove()

    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()

    }

    private  fun replaceFragment(){

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                ArticlesFragment.newInstance()
            )
            .commit()
    }

    private fun sortArticles(data: QuerySnapshot?){
        articlesToSell?.clear()
        soldArticles?.clear()

        for (n in data!!) {
            var note = n.toObject(Article::class.java)

            if (note != null) {
                val firstIndex = 0

                if(note.soldBy == null){
                    articlesToSell?.add(firstIndex, note)

                }else{
                    soldArticles?.add(firstIndex, note)
                }
            }
        }
    }

    private fun switchFragmentContainer(article: Article){
        val widthDp = resources.displayMetrics.run { widthPixels / density }
        if( widthDp.toDouble() >= 500.0){
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container2,
                    DescriptionFragment.newInstance(article)
                )
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    DescriptionFragment.newInstance(article)
                )
                .commit()
        }
    }
    private fun buildAlertDialog(articleId: String){
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.delete_alarm_title)
            .setMessage(R.string.alarm_message)
            .setPositiveButton(R.string.alarm_yes) { dialog, whichButton ->
                Db.deleteArticle(articleId)
                customerArticlesVM.alertDialogArticleId = Constant.EMPTY_STRING

            }.setNegativeButton(R.string.alarm_no) { dialog, whichButton ->
                customerArticlesVM.alertDialogArticleId = Constant.EMPTY_STRING

            }.setCancelable(false)
            .show()
        customerArticlesVM.alertDialogArticleId = articleId


    }

    private fun buildOptionsDialog(article: Article){
        val alertBuilder = android.app.AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.view_delete_edit_options, null)
        val optionsDialog = alertBuilder.create()

        optionsDialog.setView(view)
        optionsDialog.show()
        optionsDialog.setOnDismissListener() {
            articlesFragmentVM.articleDataFromLongClick.value = null
        }

        view.findViewById<Button>(R.id.option_update_button).setOnClickListener {
            selectedArticle = article
            val intent = Intent(this, ArticleUpdatingActivity::class.java)
            startActivity(intent)
            optionsDialog.dismiss()

        }

        view.findViewById<Button>(R.id.option_delete_button).setOnClickListener {
            optionsDialog.dismiss()
            buildAlertDialog(article.id!!)
        }
    }
}

class CustumerArticlesVM() : ViewModel(){
    var isDescription = false
    var isSoldArticles = false
    var alertDialogArticleId = Constant.EMPTY_STRING

}





