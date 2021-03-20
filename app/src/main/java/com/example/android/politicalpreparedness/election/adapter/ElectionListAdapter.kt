package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.databinding.ElectionsItemBinding
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class ElectionListAdapter(
        private val headerText: String,
        private val onClickListener: (Election) -> Unit
) : ListAdapter<DataItem,
        RecyclerView.ViewHolder>(ElectionDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Election>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.ElectionItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val election = getItem(position) as DataItem.ElectionItem
                holder.bind(onClickListener, election.election)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent, headerText)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup, headerText: String): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                        R.layout.header,
                        parent,
                        false
                )
                view.text.text = headerText
                return TextViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ElectionItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(val binding: ElectionsItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(onClickListener: (Election) -> Unit, item: Election) {
            binding.election = item
            binding.root.setOnClickListener { onClickListener(item) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ElectionsItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class ElectionListener(val clickListener: (electionId: Election) -> Unit) {
    fun onClick(election: Election) =
            clickListener(election)
}

sealed class DataItem {
    data class ElectionItem(val election: Election) : DataItem() {
        override val id = election.id
    }

    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}
