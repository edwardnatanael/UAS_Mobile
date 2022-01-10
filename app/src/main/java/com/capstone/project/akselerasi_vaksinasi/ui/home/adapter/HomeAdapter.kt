package com.capstone.project.akselerasi_vaksinasi.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.capstone.project.akselerasi_vaksinasi.R
import com.capstone.project.akselerasi_vaksinasi.model.Patient
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

open class HomeAdapter(
    options: FirebaseRecyclerOptions<Patient>,
    val onItemClick: ((data: Patient) -> Unit)?,
) : FirebaseRecyclerAdapter<Patient, HomeAdapter.HomeViewHolder?>(options) {

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int, data: Patient
    ) {
        holder.idPatient.text = data.regId
        holder.vaxType.text = data.vax_manu
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    inner class HomeViewHolder(itemView: View) : ViewHolder(itemView) {

        var idPatient: TextView = itemView.findViewById(R.id.tvIdPatient)
        var vaxType: TextView = itemView.findViewById(R.id.tvVaxType)
    }
}