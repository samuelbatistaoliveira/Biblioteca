package com.example.teste3

import com.example.teste3.home_aluno.Book

object BookRepository {

    private val books = mutableListOf(

        // ══════════════════════════════════════════════════
        //  TÉRREO — 4 livros distribuídos pela área de estantes
        //  O térreo tem estantes na parte superior/esquerda
        // ══════════════════════════════════════════════════

        // Livro 1 — canto superior esquerdo do térreo
        Book(1, "Entendendo Algoritmos", "Aditya Y. Bhargava",
            "android.resource://com.example.teste3/drawable/capa_entendendo_algoritmos",
            year = "2017", genre = "Tecnologia",
            andar = 0, pontoX = 0.18f, pontoY = 0.12f,
            localizacao = "Térreo — Estante 1, Prateleira A"),

        // Livro 2 — superior centro-esquerdo do térreo
        Book(2, "Pai Rico Pai Pobre", "Robert T. Kiyosaki",
            "android.resource://com.example.teste3/drawable/capa_pai_rico_pai_pobre",
            year = "2000", genre = "Finanças",
            andar = 0, pontoX = 0.18f, pontoY = 0.30f,
            localizacao = "Térreo — Estante 1, Prateleira B"),

        // Livro 3 — meio esquerdo do térreo
        Book(3, "É Assim Que Acaba", "Colleen Hoover",
            "android.resource://com.example.teste3/drawable/capa_assim_que_acaba",
            year = "2016", genre = "Romance",
            andar = 0, pontoX = 0.18f, pontoY = 0.50f,
            localizacao = "Térreo — Estante 2, Prateleira A"),

        // Livro 4 — inferior esquerdo do térreo
        Book(4, "Netter Atlas de Anatomia", "Frank H. Netter",
            "android.resource://com.example.teste3/drawable/capa_anatomia_humana",
            year = "2015", genre = "Medicina",
            andar = 0, pontoX = 0.18f, pontoY = 0.68f,
            localizacao = "Térreo — Estante 2, Prateleira B"),

        // ══════════════════════════════════════════════════
        //  1º ANDAR — 5 livros distribuídos pelas estantes
        //  O 1º andar tem estantes em fileiras centrais e laterais
        // ══════════════════════════════════════════════════

        // Livro 5 — fileira central superior, lado esquerdo
        Book(5, "Introdução à Nutrição", "Vários Autores",
            "android.resource://com.example.teste3/drawable/capa_introducao_a_nutricao",
            year = "2018", genre = "Nutrição",
            andar = 1, pontoX = 0.30f, pontoY = 0.15f,
            localizacao = "1º Andar — Estante 3, Prateleira A"),

        // Livro 6 — fileira central, um pouco abaixo
        Book(6, "Fisiologia Humana", "Dee Unglaub Silverthorn",
            "android.resource://com.example.teste3/drawable/capa_fisiologia_humana",
            year = "2017", genre = "Medicina",
            andar = 1, pontoX = 0.30f, pontoY = 0.35f,
            localizacao = "1º Andar — Estante 3, Prateleira B"),

        // Livro 7 — fileira central, meio
        Book(7, "Fundamentos de Enfermagem", "Patricia A. Potter",
            "android.resource://com.example.teste3/drawable/capa_fundamentos_de_enfermagem",
            year = "2018", genre = "Enfermagem",
            andar = 1, pontoX = 0.30f, pontoY = 0.55f,
            localizacao = "1º Andar — Estante 4, Prateleira A"),

        // Livro 8 — lateral direita, superior
        Book(8, "Código Limpo", "Robert C. Martin",
            "https://covers.openlibrary.org/b/isbn/9788576082675-L.jpg",
            year = "2009", genre = "Tecnologia",
            andar = 1, pontoX = 0.75f, pontoY = 0.20f,
            localizacao = "1º Andar — Estante 5, Prateleira A"),

        // Livro 9 — lateral direita, abaixo
        Book(9, "Django Essencial", "Vários Autores",
            "android.resource://com.example.teste3/drawable/capa_django_essencial",
            year = "2020", genre = "Tecnologia",
            andar = 1, pontoX = 0.75f, pontoY = 0.45f,
            localizacao = "1º Andar — Estante 5, Prateleira B"),
    )

    fun getAll(): List<Book> = books.toList()

    fun add(book: Book) { books.add(book) }

    fun remove(title: String) { books.removeAll { it.title == title } }

    fun update(oldTitle: String, updatedBook: Book) {
        val index = books.indexOfFirst { it.title == oldTitle }
        if (index != -1) books[index] = updatedBook
    }

    fun updateStatus(title: String, newStatus: String) {
        val index = books.indexOfFirst { it.title == title }
        if (index != -1) books[index] = books[index].copy(status = newStatus)
    }

    fun getByTitle(title: String): Book? = books.find { it.title == title }
}