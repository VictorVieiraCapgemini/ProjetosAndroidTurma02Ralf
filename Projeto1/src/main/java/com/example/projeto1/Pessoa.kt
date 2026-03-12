package com.example.projeto1

class Pessoa {
    val id: Int
    var nome: String?
    var idade: Int?

    constructor(id: Int, nome: String?, idade: Int?) {
        this.id = id
        this.nome = nome
        this.idade = idade
    }
}