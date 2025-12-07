package com.elanexo.app.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elanexo.app.data.model.Product
import com.elanexo.app.data.repository.ProductRepository
import kotlinx.coroutines.launch

/**
 * ViewModel: ProductViewModel
 * Manages UI-related data for Product screens
 * Part of the MVVM architecture
 */
class ProductViewModel : ViewModel() {
    
    private val repository = ProductRepository()
    
    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    
    init {
        loadProducts()
    }
    
    /**
     * GET: Load all products
     */
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getProducts().fold(
                onSuccess = { productList ->
                    _products.value = productList
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
            )
        }
    }
    
    /**
     * POST: Create a new product
     */
    fun createProduct(product: Product, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.createProduct(product).fold(
                onSuccess = {
                    loadProducts() // Reload products list
                    onSuccess()
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
            )
        }
    }
    
    /**
     * PUT: Update an existing product
     */
    fun updateProduct(id: Int, product: Product, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.updateProduct(id, product).fold(
                onSuccess = {
                    loadProducts() // Reload products list
                    onSuccess()
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
            )
        }
    }
    
    /**
     * DELETE: Remove a product
     */
    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.deleteProduct(id).fold(
                onSuccess = {
                    loadProducts() // Reload products list
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
            )
        }
    }
}
