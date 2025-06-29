package com.example.testapp_20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class CustomAdapterHistory (private var data: ArrayList<DataClass.Bin>): RecyclerView.Adapter<CustomAdapterHistory.ViewHolder>() {
    class ViewHolder(itemView: View, private val listener: View.OnClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {


        var name: TextView = itemView.findViewById(R.id.tv_card)
        var country: TextView = itemView.findViewById(R.id.tv_country);
        var locate: TextView = itemView.findViewById(R.id.tv_locate);
        var bank: TextView = itemView.findViewById(R.id.tv_bank)
        var url: TextView = itemView.findViewById(R.id.tv_url)
        var phone: TextView = itemView.findViewById(R.id.tv_phone)
        var type: TextView = itemView.findViewById(R.id.tv_type)
        var city: TextView = itemView.findViewById(R.id.tv_city)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onClick(v)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = data[position];
        holder.name.text = data[position].bin
        holder.country.text = data[position].country?.name?: "N/A"
        holder.locate.text = "${data[position].country?.latitude ?: "N/A"} ${data[position].country?.longitude ?: "N/A"}"
        holder.bank.text = data[position].bank?.name?: "N/A"
        holder.url.text = data[position].bank?.url?: "N/A"
        holder.phone.text = data[position].bank?.phone?: "N/A"
        holder.city.text = data[position].bank?.city?: "N/A"
        holder.type.text = data[position].scheme?: "N/A"
        updateAddToCartButton(holder, position)
    }
    private fun updateAddToCartButton(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener(){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_layout_history, parent, false)
        return ViewHolder(view, View.OnClickListener {
        })

    }

    override fun getItemCount(): Int {
        return data.size
    }
}