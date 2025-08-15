package com.example.androidcrm.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrm.R
import com.example.androidcrm.utils.Constants

class CustomerAddEditActivity : AppCompatActivity() {
    private lateinit var ETname: EditText
    private lateinit var ETcompany: EditText
    private lateinit var ETemail: EditText
    private lateinit var ETphone: EditText
    private var currentCustomerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_add_edit)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.Toolbar)
        setSupportActionBar(toolbar)

        ETname = findViewById(R.id.ETname)
        ETcompany = findViewById(R.id.ETcompany)
        ETemail = findViewById(R.id.ETemail)
        ETphone = findViewById(R.id.ETphone)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(Constants.EXTRA_CUSTOMER_ID)) {
            title = "Edit Customer"
            currentCustomerId = intent.getIntExtra(Constants.EXTRA_CUSTOMER_ID, -1)
            ETname.setText(intent.getStringExtra(Constants.EXTRA_NAME))
            ETcompany.setText(intent.getStringExtra(Constants.EXTRA_COMPANY))
            ETemail.setText(intent.getStringExtra(Constants.EXTRA_EMAIL))
            ETphone.setText(intent.getStringExtra(Constants.EXTRA_PHONE))
        } else {
            title = "Add Customer"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                saveCustomer()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveCustomer() {
        val name = ETname.text.toString().trim()
        val company = ETcompany.text.toString().trim()
        val email = ETemail.text.toString().trim()
        val phone = ETphone.text.toString().trim()

        if (name.isEmpty() || company.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent().apply {
            if (currentCustomerId != -1) {
                putExtra(Constants.EXTRA_CUSTOMER_ID, currentCustomerId)
            }
            putExtra(Constants.EXTRA_NAME, name)
            putExtra(Constants.EXTRA_COMPANY, company)
            putExtra(Constants.EXTRA_EMAIL, email)
            putExtra(Constants.EXTRA_PHONE, phone)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}