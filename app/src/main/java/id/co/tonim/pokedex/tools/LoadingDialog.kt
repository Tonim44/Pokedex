package id.co.tonim.pokedex.tools

import android.app.Activity
import android.app.AlertDialog
import id.co.tonim.pokedex.R

class LoadingDialog(val mActivity: Activity) {

    private lateinit var isdialog: AlertDialog

    fun startLoading() {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_item, null)

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        isdialog.show()
    }

    fun isDismiss() {
        isdialog.dismiss()
    }
}