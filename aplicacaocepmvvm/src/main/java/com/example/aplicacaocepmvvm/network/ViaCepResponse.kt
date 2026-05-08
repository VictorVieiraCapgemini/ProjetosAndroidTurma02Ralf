package com.example.aplicacaocepmvvm.network

import com.google.gson.annotations.SerializedName

data class ViaCepResponse(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?
)
