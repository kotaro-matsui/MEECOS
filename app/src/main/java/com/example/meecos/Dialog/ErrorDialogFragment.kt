package com.example.meecos.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.meecos.Config.*
import com.example.meecos.R

class ErrorDialogFragment : DialogFragment() {

    var mOK: Button? = null
    var mTitle: TextView? = null
    var mMessage: TextView? = null

    var listener: ErrorDialogListener? = null

    interface ErrorDialogListener {
        fun onErrorOkClick () {}
    }

    companion object {
        fun newInstance(title: String,
                        message: String,
                        listener: ErrorDialogListener?): ErrorDialogFragment {
            val fragment = ErrorDialogFragment()

            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(MESSAGE, message)

            if (listener != null) {
                fragment.listener = listener
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_error, null)
        val bundle = arguments

        this.mTitle = view.findViewById(R.id.title)
        this.mMessage = view.findViewById(R.id.message)

        if (bundle != null) {
            this.mTitle!!.text = bundle.getString(TITLE)
            this.mMessage!!.text = bundle.getString(MESSAGE)
        }

        this.mOK = view.findViewById(R.id.ok)

        this.mOK!!.setOnClickListener {
            if (this.listener != null) {
                this.listener!!.onErrorOkClick()
            } else {
                this.dismiss()
            }
        }

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return dialog
    }
}
