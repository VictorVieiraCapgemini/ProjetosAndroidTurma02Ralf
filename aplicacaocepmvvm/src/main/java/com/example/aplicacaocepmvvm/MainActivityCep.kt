package com.example.aplicacaocepmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacaocepmvvm.repository.ContactRepository
import com.example.aplicacaocepmvvm.repository.local.AppDatabase
import com.example.aplicacaocepmvvm.ui.screens.ContactFormScreen
import com.example.aplicacaocepmvvm.ui.screens.ContactListScreen
import com.example.aplicacaocepmvvm.ui.theme.ProjetosKotlinTurma02AndroidRalfTheme
import com.example.aplicacaocepmvvm.viewmodel.ContactViewModel
import com.example.aplicacaocepmvvm.viewmodel.ContactViewModelFactory

class MainActivityCep : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(this)
        val repository = ContactRepository(database.contactDao())
        val factory = ContactViewModelFactory(repository)

        setContent {
            ProjetosKotlinTurma02AndroidRalfTheme {
                val navController = rememberNavController()
                val viewModel: ContactViewModel = viewModel(factory = factory)

                NavHost(navController = navController, startDestination = "contactList") {
                    composable("contactList") {
                        ContactListScreen(
                            viewModel = viewModel,
                            onAddContact = { navController.navigate("contactForm") },
                            onEditContact = { navController.navigate("contactForm") }
                        )
                    }
                    composable("contactForm") {
                        ContactFormScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
