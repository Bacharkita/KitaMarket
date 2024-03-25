package com.todoapp.kitamarket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ArticleListAdapter(context: Context, notelist: ArrayList<Article>) :
    ArrayAdapter<Article>(context, 0, notelist) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.article_list_element_layout, parent, false)
        val article: Article? = getItem(position)
        view.findViewById<TextView>(R.id.article_name_text_view).text = article?.name
        view.findViewById<TextView>(R.id.article_id_text_view).text = article?.id
        view.findViewById<TextView>(R.id.article_price_text_view).text= article?.price.toString()

        return view

    }
}