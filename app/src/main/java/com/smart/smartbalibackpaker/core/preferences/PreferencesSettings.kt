package com.smart.smartbalibackpaker.core.preferences

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.smart.smartbalibackpaker.EditProfileActivity
import com.smart.smartbalibackpaker.R

class PreferencesSettings : PreferenceFragmentCompat() {

    private lateinit var editProfilePreferences: Preference
    private lateinit var notificationsPreferences: SwitchPreference
    private lateinit var languagesPreferences: Preference
    private lateinit var editProfile: String
    private lateinit var notifications: String
    private lateinit var languages: String

    override fun setDivider(divider: Drawable?) {
        super.setDivider(divider)
        resources.getDrawable(R.drawable.divider_settings)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, "settings_preferences")
        //TODO --> HALOO TEST
        setupEditProfile()
        setupNotifications()
        setupLanguages()
        setupDarkMode()
    }

    private fun setupDarkMode() {
        val listPreference =
            findPreference<Preference>(getString(R.string.key_dark_mode)) as ListPreference?
        listPreference?.setOnPreferenceChangeListener { _, newValue ->
            when(newValue){
                "auto"  -> updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                "on"    -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                "off"   -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

    private fun setupEditProfile() {
        editProfile = resources.getString(R.string.key_edit_profile)
        editProfilePreferences = findPreference<Preference>(editProfile) as Preference
        editProfilePreferences.setOnPreferenceClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
            true
        }
    }

    private fun setupNotifications() {
        notifications = resources.getString(R.string.key_notifications)
        notificationsPreferences = findPreference<SwitchPreference>(notifications) as SwitchPreference

        notificationsPreferences.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{ _,_ ->
                notificationsPreferences.isChecked = !notificationsPreferences.isChecked

                true
            }
    }

    private fun setupLanguages(){
        languages = resources.getString(R.string.key_edit_languages)
        languagesPreferences = findPreference<Preference>(languages) as Preference
        languagesPreferences.setOnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }
    }
}