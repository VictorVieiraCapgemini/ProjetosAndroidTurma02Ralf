package com.example.gerenciardespesas.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gerenciardespesas.model.FinanceState
import com.example.gerenciardespesas.model.Ganho
import com.example.gerenciardespesas.model.Gasto
import com.example.gerenciardespesas.model.Sonho
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FinanceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FinanceState())
    val uiState: StateFlow<FinanceState> = _uiState.asStateFlow()

    fun addGanho(descricao: String, valor: Double, data: String) {
        val novoGanho = Ganho(descricao = descricao, valor = valor, data = data)
        _uiState.update { it.copy(ganhos = it.ganhos + novoGanho) }
    }

    fun updateGanho(id: String, descricao: String, valor: Double, data: String) {
        _uiState.update { state ->
            state.copy(ganhos = state.ganhos.map { 
                if (it.id == id) it.copy(descricao = descricao, valor = valor, data = data) else it 
            })
        }
    }

    fun removeGanho(id: String) {
        _uiState.update { it.copy(ganhos = it.ganhos.filterNot { g -> g.id == id }) }
    }

    fun addGasto(descricao: String, valor: Double, categoria: String, data: String) {
        val novoGasto = Gasto(descricao = descricao, valor = valor, categoria = categoria, data = data)
        _uiState.update { it.copy(gastos = it.gastos + novoGasto) }
    }

    fun updateGasto(id: String, descricao: String, valor: Double, categoria: String, data: String) {
        _uiState.update { state ->
            state.copy(gastos = state.gastos.map { 
                if (it.id == id) it.copy(descricao = descricao, valor = valor, categoria = categoria, data = data) else it 
            })
        }
    }

    fun removeGasto(id: String) {
        _uiState.update { it.copy(gastos = it.gastos.filterNot { g -> g.id == id }) }
    }

    fun addSonho(nome: String, valorObjetivo: Double) {
        val novoSonho = Sonho(nome = nome, valorObjetivo = valorObjetivo)
        _uiState.update { it.copy(sonhos = it.sonhos + novoSonho) }
    }

    fun removeSonho(id: String) {
        _uiState.update { it.copy(sonhos = it.sonhos.filterNot { s -> s.id == id }) }
    }
}
