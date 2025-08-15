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

class OrderAddEditActivity : AppCompatActivity() {
    private lateinit var ETCustomerID: EditText
    private lateinit var ETTitle: EditText
    private lateinit var ETAmount: EditText

    private var currentOrderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_add_edit)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.Toolbar)
        setSupportActionBar(toolbar)

        ETCustomerID = findViewById(R.id.ETCustomerID)
        ETTitle = findViewById(R.id.ETTitle)
        ETAmount = findViewById(R.id.ETAmount)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(Constants.EXTRA_ORDER_ID)) {
            title = "Edit Order"
            currentOrderId = intent.getIntExtra(Constants.EXTRA_ORDER_ID, -1)
            ETCustomerID.setText(intent.getIntExtra(Constants.EXTRA_ORDER_CUSTOMER_ID, -1).toString())
            ETTitle.setText(intent.getStringExtra(Constants.EXTRA_ORDER_TITLE))
            ETAmount.setText(intent.getDoubleExtra(Constants.EXTRA_ORDER_AMOUNT, 0.0).toString())
        } else {
            title = "Add Order"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                saveOrder()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveOrder() {
        val customerId = ETCustomerID.text.toString().toIntOrNull()
        val title = ETTitle.text.toString().trim()
        val amount = ETAmount.text.toString().toDoubleOrNull()

        if (title.isEmpty() || customerId == null || amount == null) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent().apply {
            putExtra(Constants.EXTRA_ORDER_CUSTOMER_ID, customerId)
            putExtra(Constants.EXTRA_ORDER_TITLE, title)
            putExtra(Constants.EXTRA_ORDER_AMOUNT, amount)
            if (currentOrderId != -1) {
                putExtra(Constants.EXTRA_ORDER_ID, currentOrderId)
            }
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
