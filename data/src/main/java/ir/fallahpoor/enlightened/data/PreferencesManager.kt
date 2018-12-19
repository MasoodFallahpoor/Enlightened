package ir.fallahpoor.enlightened.data

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

class PreferencesManager @Inject
constructor(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, true)

    fun getString(key: String) = sharedPreferences.getString(key, "") ?: ""

}