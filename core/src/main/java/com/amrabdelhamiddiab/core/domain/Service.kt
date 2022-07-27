package com.amrabdelhamiddiab.core.domain

data class Service(
    val category: String ="",
    val name_of_service: String ="",
    val specialization_of_service: String ="",
    val period_per_each_service: Int? =0,
)