package com.earthmap.satellite.map.location.map.Utils.dialogs

import com.earthmap.satellite.map.location.map.R



import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.earthmap.satellite.map.location.map.databinding.RatingDialogBinding
import com.willy.ratingbar.ScaleRatingBar

class RateUsDailog(var mContext: Context):Dialog(mContext) {
    private var ratingBar: ScaleRatingBar? = null
    lateinit var binding:RatingDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        binding= RatingDialogBinding.inflate(layoutInflater)
        setContentView(
            binding.root
        )

        initialization()
        clickListeners()
    }

    private fun clickListeners() {
        binding.noteNowBtn!!.setOnClickListener {
            try {
                dismiss()
            } catch (e: Exception) {
            }
        }
        ratingBar!!.setOnRatingChangeListener { ratingBar, rating, fromUser ->

        if (rating >= 4f) {
            rateUs()
            dismiss()
            ratingBar.rating = 0F

            } else {
              Toast.makeText(mContext,"Thanks for suggestion",Toast.LENGTH_SHORT).show()
            }


        }
        binding.rateUsBtn.setOnClickListener {
            rateUs()
            dismiss()
        }
    }

    private fun initialization() {
        ratingBar = binding.simpleRatingBar
    }


    fun rateUs() {
        val appPackageName = context.packageName // getPackageName() from Context or Activity object
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "market://details?id=$appPackageName"
                    )
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
        }
    }

    fun sendEmail(subject: String?, body: String?) {

        val i = Intent(Intent.ACTION_SENDTO)
        i.type = "text/plain"
        i.data = Uri.parse("mailto:")
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("appswingstudio@gmail.com"))
        i.putExtra(Intent.EXTRA_SUBJECT, subject)
        i.putExtra(Intent.EXTRA_TEXT, body)
        i.setPackage("com.google.android.gm")
        try {
            mContext.startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                mContext,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


}