package com.example.demo.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.example.demo.databinding.CustomLoaderLayoutBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class Loader @Inject constructor(@ActivityContext var context: Context) {
    private val dialog = Dialog(context)
    private var binding: CustomLoaderLayoutBinding? = null

    init {
        binding = CustomLoaderLayoutBinding.inflate(LayoutInflater.from(context), null, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding!!.root)
        //dialog.window!!.attributes.windowAnimations = R.style.dialogAnimation
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
    }

    fun show() {
        if (binding!!.message.visibility == View.VISIBLE)
            binding!!.message.visibility = View.GONE
        dialog.show()
    }

    fun showWithMessage(message: String) {
        binding!!.message.visibility = View.VISIBLE
        binding!!.message.text = message
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing)
            try {
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

}