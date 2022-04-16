package onepass

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import java.util.*

class Services() {

    // service list
    private val _serviceArray: Array<String> = arrayOf("amazon", "apple", "ebay", "facebook",
            "google", "instagram", "linkedin", "outlook", "paypal", "playstation", "twitter")

    // display options
    private val ROW_MARGIN = 40
    private val SERVICE_ICON_WIDTH = 120
    private val SERVICE_ICON_HEIGHT = 120
    private val PASSWORD_TEXT_SIZE: Float = 15F
    private val SERVICE_NAME_VIEW_MARGIN_START = 20
    private val SERVICE_NAME_VIEW_TEXT_SIZE: Float = 20F
    private val ONCLICK_POPUP_MESSAGE = "Password copied"

    // main activity stuff
    private lateinit var _context: Context
    private lateinit var _clipboard: ClipboardManager

    constructor(ctx: Context) : this() {
        this._context = ctx
        this._clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    /**
     * Get an array of rows, each containing a service icon, service title,
     * and a password text view.
     */
    fun getServiceRows(): Vector<TableRow> {

        val rows = Vector<TableRow>()
        _serviceArray.forEach { serviceName -> rows.add(BuildRow(serviceName)) }

        return rows
    }


    /**
     * Create a service table row with given name.
     */
    fun BuildRow(serviceName: String): TableRow {

        val row = createRow()
        val icon: ImageView = buildServiceIcon(serviceName)
        val service: TextView = buildServiceNameView(serviceName)
        val password: TextView = buildPasswordView()

        setOnclickEvent(serviceName, password, row)
        row.addView(icon)
        row.addView(service)
        row.addView(password)

        return row
    }

    /**
     * Copy password and display a popup whenever
     * the given password view is clicked.
     */
    private fun setOnclickEvent(serviceName: String, passwordView: TextView, row: TableRow) {
        passwordView.setOnClickListener {
            if (passwordView.text.toString().isNotEmpty()) {
                val clip = ClipData.newPlainText("$serviceName password", passwordView.text.toString())
                _clipboard.setPrimaryClip(clip)

                val copyPasswordPopup = Toast.makeText(_context, ONCLICK_POPUP_MESSAGE, Toast.LENGTH_SHORT)
                copyPasswordPopup.setGravity(Gravity.CENTER, 0, 0)
                copyPasswordPopup.show()

                row.startAnimation(AnimationUtils.loadAnimation(_context, android.R.anim.fade_in))
                row.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Build a row view with given margins.
     */
    private fun createRow(): TableRow {

        val row = TableRow(_context)
        val rowLayout = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT)
        rowLayout.setMargins(0, 0, 0, ROW_MARGIN)
        row.layoutParams = rowLayout

        return row
    }


    /**
     * Build an imageView with given service name.
     * (the image is stored under "res/drawable/serviceName_icon")
     */
    private fun buildServiceIcon(serviceName: String): ImageView {

        val icon = ImageView(_context)
        icon.layoutParams = TableRow.LayoutParams(SERVICE_ICON_WIDTH, SERVICE_ICON_HEIGHT)
        icon.adjustViewBounds = true
        icon.scaleType = ImageView.ScaleType.FIT_CENTER
        icon.setImageResource(_context.resources.getIdentifier("@drawable/" + serviceName + "_icon",
                null, _context.packageName))

        return icon
    }

    /**
     * Build the text view that displays the service name.
     */
    private fun buildServiceNameView(serviceName: String): TextView {

        val view = TextView(_context)
        view.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER or Gravity.START
            marginStart = SERVICE_NAME_VIEW_MARGIN_START
        }
        view.textSize = SERVICE_NAME_VIEW_TEXT_SIZE
        view.text = serviceName

        return view
    }

    /**
     * Build the text view that contains the generated password
     */
    private fun buildPasswordView(): TextView {

        val view = TextView(_context)
        view.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        view.textSize = PASSWORD_TEXT_SIZE

        return view
    }
}
