package com.example.aplicacaocepmvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val birthDate: String,
    val zipCode: String,
    val neighborhood: String,
    val street: String,
    val number: String,
    val state: String,
    val city: String
)
