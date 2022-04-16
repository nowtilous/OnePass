package onepass

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        if (isNightModeEnabled(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupServiceTable()
        setupListeners(findViewById(R.id.service_table))
    }


    /**
     * Build service table.
     * Iterate all rows built by Services() class and
     * add them to the main table layout view.
     */
    private fun setupServiceTable() {

        val serviceTable = findViewById<TableLayout>(R.id.service_table)
        val services = Services(this)

        services.getServiceRows().forEach { row -> serviceTable.addView(row) }
    }

    /**
     * Add textChanged/onclick listeners to the following views:
     * - Custom service edit bar (if main password is present, updates the custom
     * service password after every character input)
     * - Main password (updates all services after every character input)
     * - Dark mode button
     */
    private fun setupListeners(serviceTable: TableLayout) {

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val customServiceBar = findViewById<LinearLayout>(R.id.custom_service_bar)
        val customServiceEditView = findViewById<EditText>(R.id.custom_service)
        val customServiceWatcher = buildCustomServiceWatcher(customServiceBar, this)

        val masterPasswordView = findViewById<EditText>(R.id.main_password_input)
        val masterPasswordWatcher = buildPasswordWatcher(serviceTable, customServiceBar, this)

        setCustomServiceListener(customServiceBar, clipboard, this)
        masterPasswordView.addTextChangedListener(masterPasswordWatcher)
        customServiceEditView.addTextChangedListener(customServiceWatcher)

        setDarkmodeListener(findViewById(R.id.darkmode_button), this)
    }

}
