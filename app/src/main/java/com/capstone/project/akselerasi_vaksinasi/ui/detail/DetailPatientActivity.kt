package com.capstone.project.akselerasi_vaksinasi.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.project.akselerasi_vaksinasi.R
import com.capstone.project.akselerasi_vaksinasi.R.string
import com.capstone.project.akselerasi_vaksinasi.model.Patient
import kotlinx.android.synthetic.main.activity_detail_patient.tvAgeValue
import kotlinx.android.synthetic.main.activity_detail_patient.tvRegistrationIdValue
import kotlinx.android.synthetic.main.activity_detail_patient.tvSexValue
import kotlinx.android.synthetic.main.activity_detail_patient.tvSymptomValue
import kotlinx.android.synthetic.main.activity_detail_patient.tvVaxTypeValue

class DetailPatientActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context, patient: Patient) {
            context.startActivity(
                Intent(context, DetailPatientActivity::class.java).putExtra(
                    "DETAIL_PATIENT",
                    patient
                )
            )
        }
    }

    private lateinit var patient: Patient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_patient)

        patient = intent?.getParcelableExtra("DETAIL_PATIENT") ?: Patient()

        setupToolbar()

        setupData(patient)
    }

    private fun setupToolbar() {
        if (supportActionBar != null) {
            supportActionBar!!.title = getString(string.label_detail_patient_data)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupData(patient: Patient) {
        tvRegistrationIdValue.text = patient.regId
        tvAgeValue.text = patient.age_yrs.toString()
        tvSexValue.text = if (patient.sex == "M") getString(string.label_male) else getString(string.label_female)
        tvSymptomValue.text = patient.symptom0
        tvVaxTypeValue.text = patient.vax_manu
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}