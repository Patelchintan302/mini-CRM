package com.example.androidcrm.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrm.R
import com.example.androidcrm.network.RetrofitClient
import com.example.androidcrm.network.UserResponse // UPDATED import
import com.example.androidcrm.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerAddEditActivity : AppCompatActivity() {
    private lateinit var ETname: EditText
    private lateinit var ETcompany: EditText
    private lateinit var ETemail: EditText
    private lateinit var ETphone: EditText
    private lateinit var fetchButton: Button
    private var currentCustomerId: Int = -1

    // UPDATED: The Call type now uses the new UserResponse
    private var userCall: Call<UserResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_add_edit)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.Toolbar)
        setSupportActionBar(toolbar)

        ETname = findViewById(R.id.ETname)
        ETcompany = findViewById(R.id.ETcompany)
        ETemail = findViewById(R.id.ETemail)
        ETphone = findViewById(R.id.ETphone)
        fetchButton = findViewById(R.id.button_fetch_random)

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

        fetchButton.setOnClickListener {
            fetchRandomCompanyData()
        }
    }

    private fun fetchRandomCompanyData() {
        val TAG = "API_FETCH"

        userCall?.cancel()

        // UPDATED: Call the new endpoint
        userCall = RetrofitClient.instance.getRandomUser()

        userCall?.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    // The API returns a list, so we safely get the first user
                    val user = userResponse?.results?.firstOrNull()

                    if (user != null) {
                        Log.d(TAG, "SUCCESS! Data: $user")

                        // Combine first and last name for the full name
                        val fullName = "${user.name.first} ${user.name.last}"

                        ETname.setText(fullName)
                        ETemail.setText(user.email)
                        ETphone.setText(user.phone)

                        // Create a placeholder company name
                        ETcompany.setText("$fullName's Company")

                        Toast.makeText(applicationContext, "Data fetched!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "Response was successful, but the user list was empty.")
                    }
                } else {
                    val errorCode = response.code()
                    Log.e(TAG, "Response error. Code: $errorCode")
                    Toast.makeText(applicationContext, "Error: $errorCode", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if (call.isCanceled) {
                    Log.i(TAG, "Call was cancelled.")
                } else {
                    Log.e(TAG, "API call failed completely.", t)
                    Toast.makeText(applicationContext, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        userCall?.cancel()
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

        if (name.isEmpty() || company.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please ensure name, company, and phone are filled", Toast.LENGTH_SHORT).show()
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
