package onepass

import onepass.Math.calculateHMAC
import android.graphics.Point
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*

/**
 * Find a view in given table by searching
 * the service name with given search string.
 *
 * @returns a the view if found, null otherwise
 */
fun searchView(serviceNameSubstr: String, serviceTable: TableLayout) : View? {

    if(serviceNameSubstr.isNotEmpty()){
        for (i in 0 until serviceTable.childCount) {

            val row = serviceTable.getChildAt(i) as TableRow
            val serviceName = row.getChildAt(1) as TextView

            if(serviceName.text.toString().startsWith(serviceNameSubstr)){
                return serviceName
            }
        }
    }

    return null
}

/**
 * Scroll a ScrollView to a child view (the view can be an indirect child view).
 */
fun scrollToView(scrollViewParent: ScrollView, view: View) {
    val childOffset = Point()
    getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)

    scrollViewParent.smoothScrollTo(0, childOffset.y)
}

/**
 * Find child view of root group recursively by iterating upwards
 * from the child view and collecting all the offsets until reaching
 * the root parent.
 */
fun getDeepChildOffset(mainParent: ViewGroup, parent: ViewParent, child: View, accumulatedOffset: Point) {
    val parentGroup = parent as ViewGroup
    accumulatedOffset.x += child.left
    accumulatedOffset.y += child.top - 15 // the icon takes up more space
    if (parentGroup == mainParent) {
        return
    }
    getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
}


/**
 * Calculate a hash for all services in given table
 * and display it in each row view.
 * If the main password is empty, the rows are cleared.
 */
fun updateServiceTablePasswords(mainPassword: String, serviceTable: TableLayout) {

    for (i in 0 until serviceTable.childCount) {

        val row = serviceTable.getChildAt(i) as TableRow

        if (row.childCount < 3) continue
        val serviceName = row.getChildAt(1) as TextView
        val servicePassword = row.getChildAt(2) as TextView

        if (mainPassword.isNotEmpty()) {
            servicePassword.text = calculateHMAC(mainPassword, serviceName.text.toString())
        } else {
            servicePassword.text = ""
        }
    }
}


/**
 * Calculate a hash for given custom service, and display it.
 * If the main password is empty, the view is cleared.
 */
fun updateCustomServicePassword(mainPassword: String, customServiceBar: LinearLayout) {

    val customServiceName = customServiceBar.getChildAt(0) as EditText
    val customServicePassword = customServiceBar.getChildAt(1) as TextView

    if (customServiceName.text.toString().isNotEmpty() and mainPassword.isNotEmpty()) {
        customServicePassword.text = calculateHMAC(mainPassword, customServiceName.text.toString())
    } else {
        customServicePassword.text = ""
    }
}