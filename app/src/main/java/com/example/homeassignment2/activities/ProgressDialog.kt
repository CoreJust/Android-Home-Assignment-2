package com.example.homeassignment2.activities

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.homeassignment2.R

// Short auxiliary object to create loading dialogs.
object ProgressDialog {
    private var dialog: AlertDialog? = null

    fun create(context: Context) {
        if (dialog != null) {
            Log.d("ERROR", "Tried to open a loading dialog whereas the previous one was not closed")
            dialog?.dismiss()
        }

        dialog = AlertDialog.Builder(context, R.style.ProgressDialogTheme)
            .setView(R.layout.progress_dialog)
            .setCancelable(false)
            .create()

        dialog?.show()
    }

    fun close() {
        if (dialog == null) {
            Log.d("ERROR", "Tried to close a dialog while it is not open")
            return
        }

        dialog?.dismiss()
        dialog = null
    }
}