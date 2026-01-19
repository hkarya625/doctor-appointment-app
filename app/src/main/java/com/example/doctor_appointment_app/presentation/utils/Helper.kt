package com.arya.bookmydoc.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun openBrowser(context: Context, uri: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, uri.toUri()))
}

fun sendSms(context: Context, mobile: String, body: String) {
    val intent = Intent(Intent.ACTION_SENDTO, "smsto:$mobile".toUri())
    intent.putExtra("sms_body", body)
    context.startActivity(intent)
}

fun dialNumber(context: Context, mobile: String) {
    val intent = Intent(Intent.ACTION_DIAL, "tel:${mobile.trim()}".toUri())
    context.startActivity(intent)
}

fun shareText(context: Context, subject: String, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share"))
}
