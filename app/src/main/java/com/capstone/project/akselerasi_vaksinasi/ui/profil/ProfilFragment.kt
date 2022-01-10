package com.capstone.project.akselerasi_vaksinasi.ui.profil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.project.akselerasi_vaksinasi.LoginActivity
import com.capstone.project.akselerasi_vaksinasi.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfilFragment : Fragment() {

    private lateinit var _binding: FragmentProfilBinding
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilBinding.inflate(layoutInflater)
        val view = binding.root

        getProfil()
        logout()

        return view
    }

    private fun getProfil() {
        database = Firebase.database.reference
        auth = Firebase.auth

        val uid = auth.currentUser?.uid

        Log.d("uid", uid.toString())

        if (uid != null) {
            database.child("users").child(uid).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.child("imgURL").value}")
                 var name : String = it.child("name").value.toString()
                 var email : String = it.child("email").value.toString()
                 var institusi: String = it.child("inst").value.toString()
                 var photo: String = it.child("imgURL").value.toString()
                 var noPegawai: String =it.child("empnum").value.toString()


                binding.edtNama.text = name
                binding.edtEmail.text = email
                binding.edtNoPegawai.text = noPegawai
                binding.edtinstitusi.text = institusi

                Glide.with(requireContext())
                    .load(photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgAvatar)

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

    }

    private fun logout() {
        binding.txtLogout.setOnClickListener {
            auth.signOut()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }




}
