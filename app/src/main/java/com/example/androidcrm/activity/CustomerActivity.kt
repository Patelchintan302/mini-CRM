package com.example.androidcrm.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrm.Customer
import com.example.androidcrm.CustomerViewModel
import com.example.androidcrm.R
import com.example.androidcrm.adapter.CustomerAdapter
import com.example.androidcrm.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * This Activity displays a list of all customers from the local Room database.
 * It allows the user to add, edit, delete, and manually sync customers with Firebase.
 */
class CustomerActivity : AppCompatActivity(), CustomerAdapter.onClickListener {

    private val customerViewModel: CustomerViewModel by viewModels()
    private lateinit var customerAdapter: CustomerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addCustomerButton: FloatingActionButton

    /**
     * This launcher handles the result returned from CustomerAddEditActivity.
     * It processes the data for both new and edited customers.
     */
    private val getResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val id = data.getIntExtra(Constants.EXTRA_CUSTOMER_ID, -1)
                    val name = data.getStringExtra(Constants.EXTRA_NAME)
                    val company = data.getStringExtra(Constants.EXTRA_COMPANY)
                    val email = data.getStringExtra(Constants.EXTRA_EMAIL)
                    val phone = data.getStringExtra(Constants.EXTRA_PHONE)

                    if (name != null && company != null && email != null && phone != null) {
                        val customer = Customer(name, email, phone, company)
                        if (id == -1) {
                            customerViewModel.insertCustomer(customer)
                            Snackbar.make(recyclerView, "Customer saved locally", Snackbar.LENGTH_SHORT).show()
                        } else {
                            customer.id = id
                            customerViewModel.updateCustomer(customer)
                            Snackbar.make(recyclerView, "Customer updated locally", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        recyclerView = findViewById(R.id.rvCustomers)
        addCustomerButton = findViewById(R.id.Add_CustomerBtn)
        val toolbar: Toolbar = findViewById(R.id.DeleteToolbar)
        setSupportActionBar(toolbar)

        setupRecyclerView()
        observeViewModel()

        addCustomerButton.setOnClickListener {
            val intent = Intent(this, CustomerAddEditActivity::class.java)
            getResult.launch(intent)
        }

        setupSwipeToDelete()
    }

    private fun setupRecyclerView() {
        customerAdapter = CustomerAdapter(this)
        recyclerView.adapter = customerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        customerViewModel.allCustomers.observe(this) { customers ->
            customers?.let {
                customerAdapter.setCustomers(it)
            }
        }
    }

    override fun onClickItem(customer: Customer) {
        val intent = Intent(this, CustomerAddEditActivity::class.java).apply {
            putExtra(Constants.EXTRA_CUSTOMER_ID, customer.id)
            putExtra(Constants.EXTRA_NAME, customer.name)
            putExtra(Constants.EXTRA_COMPANY, customer.company)
            putExtra(Constants.EXTRA_EMAIL, customer.email)
            putExtra(Constants.EXTRA_PHONE, customer.phone)
        }
        getResult.launch(intent)
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                customerViewModel.deleteCustomer(customerAdapter.getCustomerAt(viewHolder.adapterPosition))
                Snackbar.make(recyclerView, "Customer deleted", Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // First, check for an internet connection if a cloud action is selected.
        if (!isNetworkAvailable() && (item.itemId == R.id.menu_save_to_cloud || item.itemId == R.id.menu_fetch_from_cloud)) {
            Toast.makeText(this, "No internet connection available.", Toast.LENGTH_SHORT).show()
            return true // Consume the click event so nothing else happens.
        }

        // If there is a connection (or the action is local), proceed.
        return when (item.itemId) {
            R.id.menu_save_to_cloud -> {
                customerViewModel.saveToCloud()
                Toast.makeText(this, "Saving customers to cloud...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_fetch_from_cloud -> {
                customerViewModel.fetchFromCloud()
                Toast.makeText(this, "Fetching customers from cloud...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.delete_all -> {
                customerViewModel.deleteAllCustomers()
                Snackbar.make(recyclerView, "All local customers deleted", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Checks if the device has an active internet connection (Wi-Fi or Mobile Data).
     * @return True if connected, false otherwise.
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}