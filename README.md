# Endless Runner 🏃‍♂️

Jogo do gênero *Endless Runner* desenvolvido em **Java** como projeto acadêmico para a disciplina de **Programação Orientada a Objetos**.

## Inicialização ⚙️

Para executar o projeto:

1. Compile o arquivo principal ``` src/core/JogoEndlessRunner.java```.

2. Execute a aplicação pela sua IDE ou via terminal após a compilação.

> Recomendado: Java 17 ou superior.

## Arquitetura 🏗️

A estrutura do projeto foi organizada de forma simples, visando clareza e facilidade de manutenção:
```
src/
├── assets/
│ ├── Inimigos/
│ ├── Rostos/
│ ├── Skins/
│ └── ...
│
├── core/
│ └── JogoEndlessRunner.java
│
├── ui/
│ ├── TelaInicial.java
│ └── TelaPontuacao.java
│
├── entidades/
│ ├── jogador
│ │ └── Jogador.java
│ │
│ ├── inimigos
│ │ ├── Inimigo.java
│ │ └── ...
│ │
│ └── ElementoDoJogo.java
└────
```


## Observações 📌

- Projeto desenvolvido com foco educacional
- Estrutura propositalmente simples
- Utiliza Java Swing para componentes gráficos
- Não utiliza frameworks externos
