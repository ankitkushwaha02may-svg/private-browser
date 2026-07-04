package com.mybrowser.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mybrowser.app.R
import com.mybrowser.app.model.Shortcut

class ShortcutAdapter(
    private val context: Context,
    private val items: List<Shortcut>
) : BaseAdapter() {

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_shortcut, parent, false)

        val icon = view.findViewById<ImageView>(R.id.iconImage)
        val label = view.findViewById<TextView>(R.id.labelText)

        val item = items[position]
        icon.setImageResource(item.iconRes)
        label.text = item.name

        return view
    }
}