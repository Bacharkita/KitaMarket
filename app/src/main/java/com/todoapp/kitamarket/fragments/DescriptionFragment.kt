package com.todoapp.kitamarket.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.todoapp.kitamarket.Constant
import com.todoapp.kitamarket.Article
import com.todoapp.kitamarket.R


class DescriptionFragment : Fragment() {
    var titleArg = ""
    var noteArg = ""
    var timestampArg = ""
    var ownerArg = ""
    var priceArg = ""
    var soldByArg = ""
    var idByArg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        titleArg = requireArguments().getString(Constant.NAME_EXTRA)!!
        idByArg = requireArguments().getString(Constant.ARTICLE_ID_EXTRA)!!
        noteArg = requireArguments().getString(Constant.NOTE_EXTRA)!!
        timestampArg = requireArguments().getString(Constant.TIME_STAMP_EXTRA)!!
        ownerArg = requireArguments().getString(Constant.OWNER_EXTRA)!!
        priceArg = requireArguments().getString(Constant.PRICE_EXTRA)!!
        soldByArg = requireArguments().getString(Constant.SOLD_BY_EXTRA).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<TextView>(R.id.article_fragment_name_value)
        val note = view.findViewById<TextView>(R.id.article_fragment_note_value)
        val timestamp = view.findViewById<TextView>(R.id.article_fragment_timestamp_value)
        val  owner = view.findViewById<TextView>(R.id.article_fragment_owner_value)
        val price = view.findViewById<TextView>(R.id.article_fragment_price_value)
        val soldBy = view.findViewById<TextView>(R.id.article_fragment_sold_by_value)
        val id = view.findViewById<TextView>(R.id.article_fragment_id_value)

        id.text = idByArg
        name.text = titleArg
        note.text = noteArg
        timestamp.text = timestampArg
        owner.text = ownerArg
        price.text = priceArg
        soldBy.text = soldByArg

    }

    companion object {

        fun newInstance(article: Article) =
            DescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(Constant.NAME_EXTRA, article.name)
                    putString(Constant.NOTE_EXTRA, article.note)
                    putString(Constant.TIME_STAMP_EXTRA, article.timestamp)
                    putString(Constant.OWNER_EXTRA, article.owner)
                    putString(Constant.PRICE_EXTRA, article.price.toString())
                    putString(Constant.SOLD_BY_EXTRA, article.soldBy)
                    putString(Constant.ARTICLE_ID_EXTRA, article.id)

                }
            }
    }
}

