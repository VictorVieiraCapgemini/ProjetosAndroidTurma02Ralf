package com.example.aplicacaocepmvvm.repository

import com.example.aplicacaocepmvvm.model.Contact
import com.example.aplicacaocepmvvm.network.ViaCepResponse
import com.example.aplicacaocepmvvm.network.ViaCepService
import com.example.aplicacaocepmvvm.repository.local.ContactDao
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContactRepository(private val contactDao: ContactDao) {

    private val viaCepService: ViaCepService = Retrofit.Builder()
        .baseUrl("https://viacep.com.br/ws/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ViaCepService::class.java)

    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    suspend fun update(contact: Contact) {
        contactDao.update(contact)
    }

    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    suspend fun getAddressByCep(cep: String): ViaCepResponse? {
        return try {
            viaCepService.getAddressByCep(cep)
        } catch (e: Exception) {
            null
        }
    }
}
