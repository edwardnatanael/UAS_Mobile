package com.capstone.project.akselerasi_vaksinasi.ui.vaksin

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.project.akselerasi_vaksinasi.R
import com.capstone.project.akselerasi_vaksinasi.databinding.FragmentVaksinBinding
import com.capstone.project.akselerasi_vaksinasi.model.Patient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class VaksinFragment : Fragment() {

    private lateinit var vaksinViewModel: VaksinViewModel
    private lateinit var _binding: FragmentVaksinBinding
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vaksinViewModel =
            ViewModelProvider(this).get(VaksinViewModel::class.java)
        _binding = FragmentVaksinBinding.inflate(layoutInflater)
        val view = binding.root

        vaksinViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        binding.progressBar.visibility = View.INVISIBLE
        predict()
        genderAdapter()
        hideKeyboard()
        return view
    }

    private fun hideKeyboard() {
        binding.layoutVaksin.setOnClickListener {
            val view = activity?.currentFocus
            if(view != null) {
                val hide = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun genderAdapter(){
        val spinner = binding.edtSex
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun predict(){
        binding.btnPredict.setOnClickListener {
            if(binding.edtRegId.text.toString().isEmpty()){
                binding.edtRegId.setError("Field ini harus diisi!!")
            } else if(binding.edtVaksin.text.toString().isEmpty()){
                binding.edtVaksin.setError("Field ini harus diisi!!")
            } else if(binding.edtUmur.text.toString().isEmpty()){
                binding.edtUmur.setError("Field ini harus diisi!!")
            } else if(binding.edtSymptom.text.toString().isEmpty()){
                binding.edtSymptom.setError("Field ini harus diisi!!")
            } else {
                binding.progressBar.visibility = View.VISIBLE
                val regId = binding.edtRegId.text.toString()
                val vax_manu = binding.edtVaksin.text.toString()
                val age_yrs = binding.edtUmur.text.toString()
                val sex = if(binding.edtSex.selectedItem.toString() == "Pria") "M" else "F"
                val symptom0 = binding.edtSymptom.text.toString()

                val patient = Patient(regId, vax_manu, age_yrs.toInt(), sex, symptom0)

                database = Firebase.database.reference
                database.child("patients").child(regId).setValue(patient)
                    .addOnSuccessListener {
                        Log.d("response", "add data to db successful")
                        binding.edtRegId.text.clear()
                        binding.edtVaksin.text.clear()
                        binding.edtUmur.text.clear()
                        binding.edtSymptom.text.clear()
                        binding.edtSex.setSelection(0)
                        binding.progressBar.visibility = View.INVISIBLE

                    }
                    .addOnFailureListener {
                        Log.d("response", "error ${it.message}")
                        binding.progressBar.visibility = View.INVISIBLE
                    }
            }
        }
    }


}