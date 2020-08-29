package io.eberlein.btinformer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import io.eberlein.btinformer.R
import io.eberlein.btinformer.Settings
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {
    private val settings = Settings.getOrCreate()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        v.sw_gps.setOnClickListener {
            settings.gpsLogging = sw_gps.isActivated
        }

        v.et_scan_time.addTextChangedListener {
            val sT = it.toString().toInt()
            if(sT < 500 || sT > 1000 * 60) Toast.makeText(context, "error: your scan time value is either below 500 or above 60000", Toast.LENGTH_LONG).show()
            else {
                settings.scanTime = sT
            }
        }

        return v
    }
}