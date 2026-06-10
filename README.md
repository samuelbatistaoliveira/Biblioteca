# 📚 Biblioteca Unifor — Aplicativo Mobile

Aplicativo mobile desenvolvido para a biblioteca da Universidade de Fortaleza (Unifor), com o objetivo de facilitar o acesso aos serviços bibliotecários por meio de uma solução digital intuitiva e eficiente.

---

## 📋 Descrição do Projeto

O sistema permite que alunos busquem, visualizem, localizem e realizem o aluguel de livros, consultem disponibilidade de salas, acompanhem prazos e multas e interajam com uma assistente virtual. Administradores e bibliotecários contam com funcionalidades de gerenciamento de acervo, controle de aluguéis e organização de salas.

---

## 👥 Integrantes da Equipe

| Nome | Função |
|---|---|
| Edmilson Cavalcante | Líder do Projeto |
| Vinícius Estevam Silva | Analista de Requisitos |
| Davi Laurentino Oliveira de Souza | Desenvolvedor |
| Suzani Jia Yi Wang | Desenvolvedora |
| Yasmin Miranda | Desenvolvedora |
| Gabriel Oliveira Rios | Desenvolvedor |

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Kotlin
- **IDE:** Android Studio
- **Banco de Dados:** Firebase 
- **Autenticação:** Firebase Authentication
- **Compatibilidade:** Android 8.0+
- **Conformidade:** LGPD

---

## ⚙️ Instruções de Execução

### Pré-requisitos

- Android Studio instalado (versão recomendada: 2024.3.2)
- JDK 11 ou superior
- Conta no Firebase configurada
- Dispositivo Android ou emulador com Android 8.0+

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/biblioteca-unifor.git
   ```

2. Abra o projeto no Android Studio.

3. Configure o arquivo `google-services.json` com as credenciais do seu projeto Firebase e coloque-o na pasta `app/`.

4. Sincronize as dependências do Gradle:
   ```
   File > Sync Project with Gradle Files
   ```

5. Execute o projeto em um dispositivo ou emulador:
   ```
   Run > Run 'app'
   ```

---

## 📁 Estrutura de Pastas

```
biblioteca-unifor/
├── .idea/
├── app/
│   └── src/
│       ├── androidTest/
│       └── main/
│           └── java/com/example/teste3/
│               ├── BotaoNav/        # Componente de navegação inferior
│               ├── admin/           # Telas e lógica do administrador
│               ├── aluguel_livros/  # Fluxo de aluguel de livros
│               ├── detalhe_livro/   # Tela de detalhes do livro
│               ├── home_aluno/      # Tela inicial do aluno
│               ├── login/           # Autenticação e acesso
│               ├── mapa/            # Localização de livros por mapa
│               ├── perfil/          # Perfil do usuário
│               └── salas/           # Gerenciamento de salas

├── google-services.json             
├── build.gradle
└── README.md
```

---

## 📱 Funcionalidades Principais

### Aluno
- Login e autenticação
- Busca e localização de livros por mapa interativo
- Realização e acompanhamento de aluguéis
- Consulta de multas e prazos
- Reserva e verificação de disponibilidade de salas
- Chat com assistente virtual
- Gerenciamento de perfil

### Administrador
- Gerenciamento do acervo (cadastro, edição, exclusão de livros)
- Controle de aluguéis e devoluções
- Aplicação e gestão de multas
- Gerenciamento de salas
- Localização e atualização de coordenadas do acervo físico


---

## 🏛️ Disciplina

Projeto desenvolvido para a disciplina de **Requisitos e Modelagem de Sistemas**
Universidade de Fortaleza — UNIFOR | 2026
