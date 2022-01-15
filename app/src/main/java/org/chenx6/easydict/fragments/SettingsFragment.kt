package org.chenx6.easydict.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

import org.chenx6.easydict.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // Init theme by preferences
        val themePreference: ListPreference = findPreference("theme")!!
        themePreference.setOnPreferenceChangeListener { preference, newValue ->
            setNightModeByPreference(newValue)
            requireActivity().recreate()
            true
        }
    }

    companion object {
        fun setNightModeByPreference(value: Any) {
            val mode = when (value) {
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                "auto" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}