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
import com.example.androidcrm.OrderViewModel
import com.example.androidcrm.Orders
import com.example.androidcrm.R
import com.example.androidcrm.adapter.OrderAdapter
import com.example.androidcrm.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * This Activity displays a list of all orders from the local Room database.
 * It allows the user to add, edit, delete, and manually sync orders with Firebase.
 */
class OrderActivity : AppCompatActivity(), OrderAdapter.onClickListener {

    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addOrderButton: FloatingActionButton

    private val getResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val orderId = data.getIntExtra(Constants.EXTRA_ORDER_ID, -1)
                    val customerId = data.getIntExtra(Constants.EXTRA_ORDER_CUSTOMER_ID, -1)
                    val title = data.getStringExtra(Constants.EXTRA_ORDER_TITLE)
                    val amount = data.getDoubleExtra(Constants.EXTRA_ORDER_AMOUNT, 0.0)

                    if (title != null && customerId != -1) {
                        val order = Orders(customerId, title, amount)
                        if (orderId == -1) {
                            orderViewModel.insertOrder(order)
                            Snackbar.make(recyclerView, "Order saved locally", Snackbar.LENGTH_SHORT).show()
                        } else {
                            order.id = orderId
                            orderViewModel.updateOrder(order)
                            Snackbar.make(recyclerView, "Order updated locally", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Could not save. Invalid customer ID.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        recyclerView = findViewById(R.id.rvOrders)
        addOrderButton = findViewById(R.id.Add_OrderBtn)
        val toolbar: Toolbar = findViewById(R.id.DeleteToolbar)
        setSupportActionBar(toolbar)

        setupRecyclerView()
        observeViewModel()

        addOrderButton.setOnClickListener {
            val intent = Intent(this, OrderAddEditActivity::class.java)
            getResult.launch(intent)
        }

        setupSwipeToDelete()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(this)
        recyclerView.adapter = orderAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        orderViewModel.allOrders.observe(this) { orders ->
            orders?.let {
                orderAdapter.setOrders(it)
            }
        }
    }

    override fun onClickItem(order: Orders) {
        val intent = Intent(this, OrderAddEditActivity::class.java).apply {
            putExtra(Constants.EXTRA_ORDER_ID, order.id)
            putExtra(Constants.EXTRA_ORDER_CUSTOMER_ID, order.customer_id)
            putExtra(Constants.EXTRA_ORDER_TITLE, order.order_title)
            putExtra(Constants.EXTRA_ORDER_AMOUNT, order.order_amount)
        }
        getResult.launch(intent)
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                orderViewModel.deleteOrder(orderAdapter.getOrderAt(viewHolder.adapterPosition))
                Snackbar.make(recyclerView, "Order deleted", Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!isNetworkAvailable() && (item.itemId == R.id.menu_save_to_cloud || item.itemId == R.id.menu_fetch_from_cloud)) {
            Toast.makeText(this, "No internet connection available.", Toast.LENGTH_SHORT).show()
            return true
        }

        return when (item.itemId) {
            R.id.menu_save_to_cloud -> {
                orderViewModel.saveToCloud()
                Toast.makeText(this, "Saving orders to cloud...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_fetch_from_cloud -> {
                orderViewModel.fetchFromCloud()
                Toast.makeText(this, "Fetching orders from cloud...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.delete_all -> {
                orderViewModel.deleteAllOrders()
                Snackbar.make(recyclerView, "All local orders deleted", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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
