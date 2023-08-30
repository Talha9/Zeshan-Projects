package com.earthmap.satellite.map.location.map.Utils.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.databinding.LoadingDialogBinding

class LoadingDialog(mContext: Context) {

    private val dialog = android.app.Dialog(mContext).apply {
        setContentView(R.layout.loading_dialog)
        window!!.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
        setCancelable(false)
        create()
    }


    fun ld_show() {
        dialog.show()
    }

    fun ld_hide() {
        dialog.dismiss()
    }

}