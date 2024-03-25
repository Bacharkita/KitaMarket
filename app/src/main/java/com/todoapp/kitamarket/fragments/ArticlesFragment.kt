package com.todoapp.kitamarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.fragment.app.activityViewModels

class ArticlesFragment : Fragment() {

    private val viewModel: ArrayListViewModel by activityViewModels()
    var listview:ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_articles,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listview = view.findViewById<ListView>(R.id.noteListView)
        viewModel.adapter = ArticleListAdapter(requireContext(), viewModel.artilcesList)
        listview?.adapter = viewModel.adapter

        listview?.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            val article = viewModel.artilcesList[position]
            viewModel.articleDataFromItemClick.value = article
        }

        listview?.onItemLongClickListener =
            AdapterView.OnItemLongClickListener {  _, _, position, _ ->
                val article = viewModel.artilcesList[position]
                viewModel.articleDataFromLongClick.value = article
                true
            }

    }


    companion object {
        fun newInstance() = ArticlesFragment().apply {}
    }

    override fun onStart() {
        super.onStart()
        viewModel.adapter.notifyDataSetChanged()
    }
}

class ArrayListViewModel() : ViewModel(){

    lateinit var artilcesList: ArrayList<Article>
    lateinit var adapter: ArticleListAdapter
    val  articleDataFromItemClick = MutableLiveData<Article?>(null)
    val  articleDataFromLongClick = MutableLiveData<Article?>(null)


}