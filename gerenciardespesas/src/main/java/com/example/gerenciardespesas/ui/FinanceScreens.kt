package com.example.gerenciardespesas.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gerenciardespesas.viewmodel.FinanceViewModel

@Composable
fun DashboardScreen(navController: NavController, viewModel: FinanceViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resumo Financeiro", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Saldo Atual", fontSize = 18.sp)
                Text("R$ ${"%.2f".format(state.saldoMensal)}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoCard("Ganhos", state.totalGanhos, Color(0xFF4AA74E), Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard("Gastos", state.totalGastos, Color(0xFFF44336), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Progresso dos Sonhos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.sonhos) { sonho ->
                val progress = if (sonho.valorObjetivo > 0) (state.saldoMensal / sonho.valorObjetivo).coerceIn(0.0, 1.0) else 0.0
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(sonho.nome, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.removeSonho(sonho.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                            }
                        }
                        LinearProgressIndicator(
                            progress = { progress.toFloat() },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        )
                        Text("Meta: R$ ${"%.2f".format(sonho.valorObjetivo)} (${(progress * 100).toInt()}%)")
                        if (state.saldoMensal >= sonho.valorObjetivo) {
                            Text("Viável!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        } else {
                            Text("Faltam: R$ ${"%.2f".format(sonho.valorObjetivo - state.saldoMensal)}", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: Double, color: Color, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 14.sp)
            Text("R$ ${"%.2f".format(value)}", color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GanhosScreen(viewModel: FinanceViewModel) {
    val state by viewModel.uiState.collectAsState()
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ganhos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = valor, onValueChange = { valor = it }, label = { Text("Valor") }, modifier = Modifier.fillMaxWidth())
        
        Button(
            onClick = {
                val v = valor.toDoubleOrNull() ?: 0.0
                if (descricao.isNotBlank() && v > 0) {
                    if (editingId == null) {
                        viewModel.addGanho(descricao, v, "hoje")
                    } else {
                        viewModel.updateGanho(editingId!!, descricao, v, "hoje")
                        editingId = null
                    }
                    descricao = ""
                    valor = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Icon(if (editingId == null) Icons.Default.Add else Icons.Default.Edit, contentDescription = null)
            Text(if (editingId == null) "Adicionar Ganho" else "Salvar Alterações")
        }

        if (editingId != null) {
            TextButton(onClick = { editingId = null; descricao = ""; valor = "" }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Cancelar Edição")
            }
        }

        LazyColumn {
            items(state.ganhos) { ganho ->
                ListItem(
                    modifier = Modifier.clickable { 
                        editingId = ganho.id
                        descricao = ganho.descricao
                        valor = ganho.valor.toString()
                    },
                    headlineContent = { Text(ganho.descricao) },
                    supportingContent = { Text("R$ ${"%.2f".format(ganho.valor)}") },
                    trailingContent = {
                        IconButton(onClick = { viewModel.removeGanho(ganho.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GastosScreen(viewModel: FinanceViewModel) {
    val state by viewModel.uiState.collectAsState()
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gastos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = valor, onValueChange = { valor = it }, label = { Text("Valor") }, modifier = Modifier.fillMaxWidth())
        
        Button(
            onClick = {
                val v = valor.toDoubleOrNull() ?: 0.0
                if (descricao.isNotBlank() && v > 0) {
                    if (editingId == null) {
                        viewModel.addGasto(descricao, v, categoria, "hoje")
                    } else {
                        viewModel.updateGasto(editingId!!, descricao, v, categoria, "hoje")
                        editingId = null
                    }
                    descricao = ""
                    categoria = ""
                    valor = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Icon(if (editingId == null) Icons.Default.Add else Icons.Default.Edit, contentDescription = null)
            Text(if (editingId == null) "Adicionar Gasto" else "Salvar Alterações")
        }

        if (editingId != null) {
            TextButton(onClick = { editingId = null; descricao = ""; categoria = ""; valor = "" }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Cancelar Edição")
            }
        }

        LazyColumn {
            items(state.gastos) { gasto ->
                ListItem(
                    modifier = Modifier.clickable { 
                        editingId = gasto.id
                        descricao = gasto.descricao
                        categoria = gasto.categoria
                        valor = gasto.valor.toString()
                    },
                    headlineContent = { Text("${gasto.descricao} (${gasto.categoria})") },
                    supportingContent = { Text("R$ ${"%.2f".format(gasto.valor)}") },
                    trailingContent = {
                        IconButton(onClick = { viewModel.removeGasto(gasto.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SonhosScreen(viewModel: FinanceViewModel) {
    val state by viewModel.uiState.collectAsState()
    var nome by remember { mutableStateOf("") }
    var valorMeta by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Sonhos e Desejos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Sonho") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = valorMeta, onValueChange = { valorMeta = it }, label = { Text("Valor Objetivo") }, modifier = Modifier.fillMaxWidth())
        
        Button(
            onClick = {
                val v = valorMeta.toDoubleOrNull() ?: 0.0
                if (nome.isNotBlank() && v > 0) {
                    viewModel.addSonho(nome, v)
                    nome = ""
                    valorMeta = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text("Adicionar Sonho")
        }

        LazyColumn {
            items(state.sonhos) { sonho ->
                ListItem(
                    headlineContent = { Text(sonho.nome) },
                    supportingContent = { Text("Meta: R$ ${"%.2f".format(sonho.valorObjetivo)}") },
                    trailingContent = {
                        IconButton(onClick = { viewModel.removeSonho(sonho.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
        }
    }
}
