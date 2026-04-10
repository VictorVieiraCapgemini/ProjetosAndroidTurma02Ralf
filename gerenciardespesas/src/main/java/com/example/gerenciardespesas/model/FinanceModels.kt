package com.example.gerenciardespesas.model

import java.util.UUID

data class Ganho(
    val id: String = UUID.randomUUID().toString(),
    val descricao: String,
    val valor: Double,
    val data: String
)

data class Gasto(
    val id: String = UUID.randomUUID().toString(),
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val data: String
)

data class Sonho(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val valorObjetivo: Double
)

data class FinanceState(
    val ganhos: List<Ganho> = emptyList(),
    val gastos: List<Gasto> = emptyList(),
    val sonhos: List<Sonho> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalGanhos: Double get() = ganhos.sumOf { it.valor }
    val totalGastos: Double get() = gastos.sumOf { it.valor }
    val saldoMensal: Double get() = totalGanhos - totalGastos
}
