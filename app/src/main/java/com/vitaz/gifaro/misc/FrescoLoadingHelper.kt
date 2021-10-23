package com.vitaz.gifaro.misc

import android.net.Uri
import androidx.core.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.view.SimpleDraweeView
import com.vitaz.gifaro.MainApplication
import com.vitaz.gifaro.R

fun setUri(draweeView: SimpleDraweeView, uri: Uri, retryEnabled: Boolean) {
    draweeView.controller = Fresco.newDraweeControllerBuilder()
        .setOldController(draweeView.controller)
        .setTapToRetryEnabled(retryEnabled)
        .setUri(uri)
        .setAutoPlayAnimations(true)
        .build()
}

fun getFrescoProgressBarLoadable(): ProgressBarDrawable {
    return ProgressBarDrawable().apply {
        color = ContextCompat.getColor(MainApplication.instance.getAppContext()!!, R.color.accent)
        backgroundColor = ContextCompat.getColor(
            MainApplication.instance.getAppContext()!!,
            android.R.color.white
        )
        radius = 20
    }
}
