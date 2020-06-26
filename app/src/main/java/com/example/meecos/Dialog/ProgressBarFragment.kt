package com.example.meecos.Dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R

class ProgressBarFragment : DialogFragment() {

    lateinit var activity: Activity

    fun newInstance(activity: Activity): ProgressBarFragment {
        val fragmnet = ProgressBarFragment()
        fragmnet.activity = activity
        return fragmnet
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_progress_bar, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setDimAmount(0F)
        return dialog
    }

    /**
     * Show
     */
    fun showDialog() {
        var activity = this.activity as MainActivity
        this.show(activity.supportFragmentManager, "dialog")
    }

    /**
     * Dismiss
     */
    fun dismissDialog() {
        this.dismiss()
    }
}