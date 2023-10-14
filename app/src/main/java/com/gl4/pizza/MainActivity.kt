package com.gl4.pizza

import android.Manifest
import android.R.id.message
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 0
    var message = ""
    var sellerPhoneNumber = "+21623409451"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstNameEditText = findViewById<TextInputEditText>(R.id.FirstNameEditText)
        val lastNameEditText = findViewById<TextInputEditText>(R.id.LastNameEditText)
        val addressEditText = findViewById<TextInputEditText>(R.id.AddressEditText)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        val checkBoxMushrooms = findViewById<CheckBox>(R.id.Mushrooms)
        val checkBoxOlives = findViewById<CheckBox>(R.id.Olives)
        val checkBoxPepperoni = findViewById<CheckBox>(R.id.Pepperoni)
        val checkBoxOnions = findViewById<CheckBox>(R.id.Onions)

        val btn = findViewById<Button>(R.id.Button)

        btn.setOnClickListener {

            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val address = addressEditText.text.toString()

            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            var selectedPizzaSize = ""
            if (selectedRadioButtonId == R.id.mini) {
                selectedPizzaSize = "Mini"
            } else if (selectedRadioButtonId == R.id.medium) {
                selectedPizzaSize = "Medium"
            } else if (selectedRadioButtonId == R.id.maxi) {
                selectedPizzaSize = "Maxi"
            }

            val checkedToppings = ArrayList<String>()
            when {
                checkBoxMushrooms.isChecked -> checkedToppings.add(checkBoxMushrooms.text.toString())
                checkBoxOlives.isChecked -> checkedToppings.add(checkBoxOlives.text.toString())
                checkBoxPepperoni.isChecked -> checkedToppings.add(checkBoxPepperoni.text.toString())
                checkBoxOnions.isChecked -> checkedToppings.add(checkBoxOnions.text.toString())
            }

            message = "Order Details:\n" +
                    "Name: $firstName $lastName\n" +
                    "Address: $address\n" +
                    "Pizza Size: $selectedPizzaSize\n" +
                    "Toppings: ${checkedToppings.joinToString(", ")}"


            // check if permission is not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // if the user has denied the permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.SEND_SMS
                    )
                ) {
                } else {
                    //popup to request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        MY_PERMISSIONS_REQUEST_SEND_SMS
                    )
                }
            }  else {
                // If the permission is already granted, call the sendSms function directly.
                sendSms(message, sellerPhoneNumber)
        }



        }
    }

        fun sendSms(message: String, sellerPhoneNumber: String) {

            val smsManager = SmsManager.getDefault()

            try {
                smsManager.sendTextMessage(sellerPhoneNumber, null, message, null, null)
                runOnUiThread {
                    Toast.makeText(this, "Message sent: $message", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error while sending message", Toast.LENGTH_SHORT).show()
                }
                if (e.message != null) {
                    Log.d("MainActivity", e.message!!);
                }

                e.printStackTrace()
            }
            // After sending the SMS, navigate to the Splash screen
            val intent = Intent(this, Splash::class.java)
            startActivity(intent)

            // Delay for 5 seconds before returning to the MainActivity
            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish() // Finish the Splash activity
            }, 5000) // 5000 milliseconds (5 seconds)
        }


        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when (requestCode) {
                MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        sendSms(message, sellerPhoneNumber)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "SMS faild, please try again.", Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                }
            }
        }


    }
