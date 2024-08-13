package com.example.eventtrackingapp

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast

fun sendSms(context: Context, phoneNumber: String, message: String) {
    try {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context, "SMS sent to $phoneNumber", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "SMS failed: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
