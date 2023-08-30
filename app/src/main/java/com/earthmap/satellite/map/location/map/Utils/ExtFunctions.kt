package com.earthmap.satellite.map.location.map.Utils

import android.content.res.Resources
import android.view.ViewGroup
import com.google.android.gms.ads.AdSize

 fun ViewGroup.getAdaptiveAdSize(): AdSize {
    val outMetrics = Resources.getSystem().displayMetrics

    val density = outMetrics.density

    var adWidthPixels = width.toFloat()
    if (adWidthPixels == 0f) {
        adWidthPixels = outMetrics.widthPixels.toFloat()
    }

    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}
