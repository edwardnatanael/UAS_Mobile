package com.capstone.project.akselerasi_vaksinasi.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.project.akselerasi_vaksinasi.R
import com.capstone.project.akselerasi_vaksinasi.databinding.FragmentHomeBinding
import com.capstone.project.akselerasi_vaksinasi.databinding.FragmentProfilBinding
import com.capstone.project.akselerasi_vaksinasi.model.Patient
import com.capstone.project.akselerasi_vaksinasi.ui.detail.DetailPatientActivity
import com.capstone.project.akselerasi_vaksinasi.ui.home.adapter.HomeAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.FirebaseRecyclerOptions.Builder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.tvWelcome)
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        database = FirebaseDatabase.getInstance().getReference("patients")
        val options: FirebaseRecyclerOptions<Patient> = Builder<Patient>()
            .setQuery(database, Patient::class.java)
            .build()
        homeAdapter = HomeAdapter(options, ::onItemClick)
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(root.context)
            adapter = homeAdapter
        }

        getUserData()

        return view
    }

    private fun getUserData() {
        database = Firebase.database.reference
        auth = Firebase.auth

        val uid = auth.currentUser?.uid

        if (uid != null) {
            database.child("users").child(uid).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.child("imgURL").value}")
                var name : String = it.child("name").value.toString()

                binding.tvWelcome.text = name


            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
    }


    private fun onItemClick(patient: Patient) {
        DetailPatientActivity.start(requireContext(), patient)
    }

    override fun onStart() {
        super.onStart()
        homeAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        homeAdapter.stopListening()
    }
}