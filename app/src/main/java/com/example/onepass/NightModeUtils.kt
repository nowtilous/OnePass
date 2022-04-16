package onepass

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

private const val NIGHT_MODE = "NIGHT_MODE"
private const val DARK_MODE_PREFERENCE = "DarkModeEnabled"

/**
 * Check if night mode is turned on using a shared preference
 */
fun isNightModeEnabled(context: Context): Boolean {
    val mPrefs = context.getSharedPreferences(DARK_MODE_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    return mPrefs.getBoolean(NIGHT_MODE, false)
}

/**
 * Set dark mode preference as given boolean
 */
fun setThemeMode(context: Context, isNightModeEnabled: Boolean) {
    val mPrefs = context.getSharedPreferences(DARK_MODE_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    val editor = mPrefs.edit()
    editor.putBoolean(NIGHT_MODE, isNightModeEnabled)
    editor.apply()
}