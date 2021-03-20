package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Election
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("textDate")
fun bindAsteroidStatusImage(textView: TextView, date: Date?) {
    if (date == null)
        textView.text = ""
    else {
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        textView.text = format.format(date)
    }
}

@BindingAdapter("electionDateText")
fun TextView.setElectionDate(item: Election) {
    item.let {
        text = item.electionDay.toSimpleString()
    }
}

@BindingAdapter("customText")
fun TextView.customText(hasInformation: Boolean) {
    if (!hasInformation) {
        text = resources.getString(R.string.unknown_address)
    }
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("goneIfNull")
fun goneIfNull(view: View, it: Any?) {
    view.visibility = if (it == null) View.GONE else View.VISIBLE
}

@BindingAdapter("followText")
fun bindAsteroidStatusImage(button: AppCompatButton, isFollow: Boolean) {
    if (isFollow)
        button.setText(R.string.txt_unfollow_election)
    else
        button.setText(R.string.txt_follow_election)
}