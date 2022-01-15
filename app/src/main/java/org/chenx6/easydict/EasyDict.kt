package org.chenx6.easydict

import android.app.Application
import androidx.preference.PreferenceManager
import org.chenx6.easydict.fragments.SettingsFragment

class EasyDict : Application() {
    override fun onCreate() {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        // Set theme(night mode) by preference
        val theme = prefManager.getString("theme", "") ?: "auto"
        SettingsFragment.setNightModeByPreference(theme)
        super.onCreate()
    }
}