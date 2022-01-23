package org.chenx6.easydict.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.chenx6.easydict.R
import org.chenx6.easydict.WordDatabase


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
        val cleanHistoryPreference: Preference = findPreference("clean_history")!!
        cleanHistoryPreference.setOnPreferenceClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setTitle("你确定？")
                setMessage("你确定要删除所有历史记录吗？")
                setPositiveButton("确定") { _, _ ->
                    cleanAllHistory()
                }
                setNegativeButton("算了") { _, _ -> }
            }
            builder.create().show()
            true
        }
    }

    // Clean all history
    private fun cleanAllHistory() {
        lifecycleScope.launch(Dispatchers.IO) {
            WordDatabase.getInstance(requireContext())
                .getWordDao()
                .cleanAllQueryHistory()
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