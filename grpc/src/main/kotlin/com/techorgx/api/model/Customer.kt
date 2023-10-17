package com.techorgx.api.model

data class Customer(
    private val id: String = "",
    private val firstName: String = "",
    private val lastName: String = "",
    private val address: String = "",
    private val city: String = "",
    private val pincode: String = "",
    private val email: String = "",
)
