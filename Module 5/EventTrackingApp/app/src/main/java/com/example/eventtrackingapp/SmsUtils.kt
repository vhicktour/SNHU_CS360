package com.example.eventtrackingapp

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast

@Suppress("DEPRECATION")
fun sendSms(context: Context, phoneNumber: String, message: String) {
    try {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "SMS Failed to Send", Toast.LENGTH_SHORT).show()
    }
}
