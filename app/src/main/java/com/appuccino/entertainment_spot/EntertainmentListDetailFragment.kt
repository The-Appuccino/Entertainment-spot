package com.appuccino.entertainment_spot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager


class EntertainmentItemAdapter(private val items: List<EntertainmentItem>) :
    RecyclerView.Adapter<EntertainmentItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.textView1)
        val coverImageView: ImageView = view.findViewById(R.id.imageView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.coverImageView.setImageResource(item.imageResId)
    }

    override fun getItemCount() = items.size
}

class EntertainmentListDetailFragment : Fragment() { //fragment that appears whenever a user clicks on a list

    private lateinit var adapter: EntertainmentItemAdapter
    private lateinit var recyclerView: RecyclerView
    private var listTitle: String? = null
    private var entertainmentItems: List<EntertainmentItem> = emptyList()

    companion object {
        private const val ARG_LIST_NAME = "listName"
        private const val ARG_ITEMS = "items"

        fun newInstance(listName: String, items: ArrayList<EntertainmentItem>): EntertainmentListDetailFragment {
            val fragment = EntertainmentListDetailFragment()
            val args = Bundle()
            args.putString(ARG_LIST_NAME, listName)
            args.putParcelableArrayList(ARG_ITEMS, items)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listTitle = it.getString(ARG_LIST_NAME)
            entertainmentItems = it.getParcelableArrayList(ARG_ITEMS) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entertainment_list_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleTextView: TextView = view.findViewById(R.id.listTitleTextView)
        recyclerView = view.findViewById(R.id.entertainmentRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = EntertainmentItemAdapter(entertainmentItems)
        recyclerView.adapter = adapter

        listTitle?.let {
            titleTextView.text = it
        }
    }
}

