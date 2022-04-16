package onepass

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import onepass.*


/**
 * Copy the custom service password every time it is clicked,
 * and a popup message.
 */
fun setCustomServiceListener(customServiceBar: LinearLayout, clipboard: ClipboardManager, ctx: Context) {

    val passwordCoppiedPopup = Toast.makeText(ctx, "Password copied", Toast.LENGTH_SHORT)
    passwordCoppiedPopup.setGravity(Gravity.CENTER, 0, 0)

    val customServiceEditView = customServiceBar.getChildAt(0) as EditText
    val customServicePasswordView = customServiceBar.getChildAt(1) as TextView
    val addService = customServiceBar.getChildAt(2) as ImageButton

    customServicePasswordView.setOnClickListener {

        if (customServicePasswordView.text.isNotEmpty()) {

            val customServiceServiceName = customServiceEditView.text.toString()
            val clip: ClipData = ClipData.newPlainText("$customServiceServiceName password",
                    customServicePasswordView.text.toString())
            clipboard.setPrimaryClip(clip)

            passwordCoppiedPopup.show()

            customServiceBar.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.fade_in))
            customServiceBar.visibility = View.VISIBLE
        }
    }

    addService.setOnClickListener{
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(ctx as Activity, intent,100,null)
    }
}

/**
 * Toggle dark mode.
 * This listener recreates the main activity
 * to change the theme.
 */
fun setDarkmodeListener(buttonView: ImageButton, ctx: MainActivity){
    buttonView.setOnClickListener {
        if (isNightModeEnabled(ctx)) {
            setThemeMode(ctx, false)
        } else {
            setThemeMode(ctx, true)
        }
        ctx.recreate()
    }
}

/**
 * Password updating watcher.
 * Calculates all hashes on given table and custom service bar
 * every time a character is entered
 */
fun buildPasswordWatcher(serviceTable: TableLayout, customServiceBar: LinearLayout,
                         mainActivity: MainActivity
): TextWatcher {

    return object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(text: Editable) {

            val mainPassword = (mainActivity.findViewById<View>(R.id.main_password_input) as TextView).text.toString()

            updateServiceTablePasswords(mainPassword, serviceTable)
            updateCustomServicePassword(mainPassword, customServiceBar)
        }
    }
}


/**
 * Custom service updating watcher.
 * Calculates the hash specifically for given layout and displays it
 * after each key type.
 */
fun buildCustomServiceWatcher(customServiceBar: LinearLayout,
                              mainActivity: MainActivity
): TextWatcher {

    return object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(text: Editable) {

            val customService = mainActivity.findViewById<TextView>(R.id.custom_service)
            val serviceTable = mainActivity.findViewById<TableLayout>(R.id.service_table)
            val scrollView = mainActivity.findViewById<ScrollView>(R.id.service_scroll_view)
            val existingService = searchView(customService.text.toString(), serviceTable)

            if(existingService != null){
                scrollToView(scrollView, existingService)
            }

            val mainPassword = mainActivity.findViewById<TextView>(R.id.main_password_input).text.toString()
            updateCustomServicePassword(mainPassword, customServiceBar)
        }
    }
}

