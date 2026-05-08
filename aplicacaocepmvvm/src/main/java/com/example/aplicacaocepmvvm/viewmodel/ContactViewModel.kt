package com.example.aplicacaocepmvvm.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacaocepmvvm.model.Contact
import com.example.aplicacaocepmvvm.repository.ContactRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    val allContacts: StateFlow<List<Contact>> = repository.allContacts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var zipCode by mutableStateOf("")
    var neighborhood by mutableStateOf("")
    var street by mutableStateOf("")
    var number by mutableStateOf("")
    var state by mutableStateOf("")
    var city by mutableStateOf("")

    var selectedContact: Contact? by mutableStateOf(null)

    fun onZipCodeChange(newZipCode: String) {
        zipCode = newZipCode
        if (newZipCode.length == 8) {
            viewModelScope.launch {
                val response = repository.getAddressByCep(newZipCode)
                response?.let {
                    neighborhood = it.bairro ?: ""
                    street = it.logradouro ?: ""
                    city = it.localidade ?: ""
                    state = it.uf ?: ""
                }
            }
        }
    }

    fun saveContact() {
        val contact = Contact(
            id = selectedContact?.id ?: 0,
            name = name,
            email = email,
            phone = phone,
            birthDate = birthDate,
            zipCode = zipCode,
            neighborhood = neighborhood,
            street = street,
            number = number,
            state = state,
            city = city
        )
        viewModelScope.launch {
            if (selectedContact == null) {
                repository.insert(contact)
            } else {
                repository.update(contact)
            }
            clearFields()
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

    fun selectContact(contact: Contact) {
        selectedContact = contact
        name = contact.name
        email = contact.email
        phone = contact.phone
        birthDate = contact.birthDate
        zipCode = contact.zipCode
        neighborhood = contact.neighborhood
        street = contact.street
        number = contact.number
        state = contact.state
        city = contact.city
    }

    fun clearFields() {
        selectedContact = null
        name = ""
        email = ""
        phone = ""
        birthDate = ""
        zipCode = ""
        neighborhood = ""
        street = ""
        number = ""
        state = ""
        city = ""
    }
}

class ContactViewModelFactory(private val repository: ContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
