package com.example.merco.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.Order
import com.example.merco.domain.model.Product
import com.example.merco.repository.OrderRepository
import com.example.merco.repository.OrderRepositoryImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class OrderViewModel(
    private val repository: OrderRepository = OrderRepositoryImpl()

) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _order = MutableLiveData<Order?>()
    val order: LiveData<Order?> = _order
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _errorr = MutableLiveData<String?>()
    val errorr: LiveData<String?> = _errorr


   
    val orderCreated = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    init {
        fetchOrders(userId = Firebase.auth.uid!!)
        fetchOrdersU(userId = Firebase.auth.uid!!)
    }

    fun changeStatus(orderId:String)= viewModelScope.launch {
        try {
            var order=repository.getOrderById(orderId)
            if (order != null) {
                repository.changeStatus(order)
            }
        } catch (e: Exception) {
            Log.e("changeStatus","Error fetching Orders: ${e.message}")
        }
    }

    fun fetchOrdersU(userId: String) = viewModelScope.launch {
        try {
            val ordersList = repository.getAllOrdersU(userId)
            _orders.postValue(ordersList)
        } catch (e: Exception) {
            Log.e("CategoryViewModel", "Error fetching Orders: ${e.message}")
        }
    }

    fun fetchOrders(userId: String) = viewModelScope.launch {
        try {
            val ordersList = repository.getAllOrders(userId)
            _orders.postValue(ordersList)
        } catch (e: Exception) {
            Log.e("CategoryViewModel", "Error fetching Orders: ${e.message}")
        }
    }


    fun createOrder(order: Order) {
        viewModelScope.launch {
            try {
                repository.createOrder(order)
                orderCreated.value = true
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    fun createOrderWithSellerDetails(
        product: Product,
        quantity: Int,
        onOrderCreated: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection("users").document(product.sellerId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storeName = document.getString("storename") ?: "Desconocido"
                    val storeAddress = document.getString("address") ?: "Sin dirección"

                    val newOrder = Order(
                        id = "",
                        productId = product.id,
                        customerId = Firebase.auth.uid!!,
                        sellerId = product.sellerId,
                        quantity = quantity,
                        totalPrice = product.newPrice * quantity,
                        storeName = storeName,
                        storeAddress = storeAddress,
                        imageId = product.id,
                        productName = product.name,
                        status = "Pendiente"
                    )
                    createOrder(newOrder)
                    onOrderCreated()
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
        }






}




