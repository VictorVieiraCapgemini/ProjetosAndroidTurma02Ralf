package com.example.projeto1
import java.util.Scanner
class Pessoa(
    val id: Int,
    var nome: String?,
    var idade: Int?
)

class PessoaManager {
    private val listaPessoas = mutableListOf<Pessoa>()
    private var proximoId = 1

    fun cadastrar(nome: String?, idade: Int?) {

        val nomeValido = nome?.takeIf { it.isNotBlank() && it.none { char -> char.isDigit() } } ?: run {
            println("Erro: Nome inválido, vazio ou contém números.")
            return
        }

        val pessoa = Pessoa(proximoId++, nomeValido, idade)
        listaPessoas.add(pessoa)
        println("Pessoa cadastrada com sucesso! ID: ${pessoa.id}")
    }
    fun listar() {
        if (listaPessoas.isEmpty()) {
            println("A lista está vazia.")
        } else {
            println("\n--- Listagem de Pessoas ---")
            listaPessoas.forEach { p ->
                println("ID: ${p.id} | Nome: ${p.nome} | Idade: ${p.idade ?: "Não informada"}")
            }
        }
    }
    fun pesquisarPorId(id: Int): Pessoa? {
        return listaPessoas.find { it.id == id }
    }

    fun alterar(id: Int, novoNome: String?, novaIdade: Int?): Boolean {
        val pessoa = pesquisarPorId(id)
        if (pessoa != null) {
            if (!novoNome.isNullOrBlank() && novoNome.none { it.isDigit() }) {
                pessoa.nome = novoNome
            } else if (!novoNome.isNullOrBlank()) {
                println("Erro: O novo nome contém números e não será alterado.")
            }
            pessoa.idade = novaIdade
            return true
        }
        return false
    }
    fun remover(id: Int): Boolean {
        return listaPessoas.removeIf { it.id == id }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val manager = PessoaManager()
    var continuar = true

    println("Bem-vindo ao Sistema de Cadastro de Pessoas!")

    while (continuar) {
        println("\n--- MENU PRINCIPAL ---")
        println("1. Cadastrar")
        println("2. Listar")
        println("3. Pesquisar")
        println("4. Alterar")
        println("5. Remover")
        println("6. Finalizar")
        print("Escolha uma opção: ")

        val opcao = scanner.nextLine().toIntOrNull() ?: 0

        when (opcao) {
            1 -> {
                print("Digite o nome: ")
                val nome = scanner.nextLine()
                print("Digite a idade (ou deixe vazio): ")
                val idadeInput = scanner.nextLine()
                val idade = idadeInput.toIntOrNull()
                manager.cadastrar(nome, idade)
            }
            2 -> manager.listar()
            3 -> {
                print("Digite o ID para pesquisa: ")
                val id = scanner.nextLine().toIntOrNull() ?: -1
                val p = manager.pesquisarPorId(id)
                if (p != null) {
                    println("Encontrado: ID: ${p.id}, Nome: ${p.nome}, Idade: ${p.idade ?: "N/A"}")
                } else {
                    println("Pessoa com ID $id não encontrada.")
                }
            }
            4 -> {
                print("Digite o ID da pessoa que deseja alterar: ")
                val id = scanner.nextLine().toIntOrNull() ?: -1
                val p = manager.pesquisarPorId(id)
                if (p != null) {
                    print("Novo nome (atual: ${p.nome}): ")
                    val novoNome = scanner.nextLine()
                    print("Nova idade (atual: ${p.idade ?: "N/A"}): ")
                    val novaIdade = scanner.nextLine().toIntOrNull()
                    if (manager.alterar(id, novoNome, novaIdade)) {
                        println("Dados atualizados com sucesso!")
                    }
                } else {
                    println("Pessoa com ID $id não encontrada.")
                }
            }
            5 -> {
                print("Digite o ID da pessoa que deseja remover: ")
                val id = scanner.nextLine().toIntOrNull() ?: -1
                if (manager.remover(id)) {
                    println("Pessoa removida com sucesso!")
                } else {
                    println("Pessoa com ID $id não encontrada.")
                }
            }
            6 -> {
                println("Encerrando o programa. Até logo!")
                continuar = false
            }
            else -> println("Opção inválida! Tente novamente.")
        }
    }
}

