package woowacourse.movie.util

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setBoolean(
        key: String,
        value: Boolean,
    ) {
        sharedPreferences.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(
        key: String,
        defaultValue: Boolean,
    ): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    companion object {
        private const val PREFERENCES_NAME = "shared_preferences"
    }
}
