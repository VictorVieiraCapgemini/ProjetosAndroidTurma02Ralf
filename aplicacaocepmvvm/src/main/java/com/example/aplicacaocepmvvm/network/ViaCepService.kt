package com.example.aplicacaocepmvvm.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    suspend fun getAddressByCep(@Path("cep") cep: String): ViaCepResponse
}
