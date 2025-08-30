# Calculadora de Custo e Consumo do Almoço

Este é um programa em Java com interface gráfica (Swing) que calcula o custo detalhado de uma refeição, separando os gastos com ingredientes e o consumo de energia de aparelhos de cozinha.

## Sobre o Projeto

Este projeto foi construído do zero com o auxílio de uma Inteligência Artificial (Google AI). Mesmo sem conhecimento prévio em ferramentas como o Maven, foi possível estruturar um projeto completo, depurar erros e evoluir de uma simples aplicação de console para um programa com interface gráfica, demonstrando uma colaboração eficaz entre um desenvolvedor e uma IA.

## Funcionalidades

-   **Cálculo de Custo de Comida:** Adicione itens por peso ou por unidade (copos), calculando o custo a partir do preço do pacote.
-   **Cálculo de Consumo de Energia:** Adicione o consumo de aparelhos elétricos (em kWh) com base em sua potência e tempo de uso.
-   **Interface Gráfica Simples:** Uma interface construída com Java Swing para facilitar a entrada de dados.
-   **Geração de Planilha:** Exporta um resumo detalhado para um arquivo Excel (`.xlsx`), permitindo escolher onde salvar o arquivo.

## Como Usar o Aplicativo (Para Usuários)

1.  Vá para a seção **"Releases"** nesta página do GitHub.
2.  Baixe o arquivo `CalculadoraAlmoco.exe` da versão mais recente.
3.  Execute o arquivo. (Requer Java 11 ou superior instalado no computador).

## Como Compilar e Rodar (Para Desenvolvedores)

1.  **Pré-requisitos:**
    -   Java Development Kit (JDK) - Versão 11 ou superior.
    -   Apache Maven.

2.  **Clone e compile:**
    ```sh
    # Clone o repositório
    git clone https://github.com/[SEU_USUARIO]/[SEU_REPOSITORIO].git
    cd CalculadoraAlmoco

    # Compile e empacote
    mvn clean package

    # Execute o .jar gerado
    java -jar target/CalculadoraAlmoco-1.0-SNAPSHOT-jar-with-dependencies.jar
    ```