package br.com.CalculadoraAlmoco;

public class GastoItem {
    private String nome;
    private double precoBase;
    private double quantidadeUsada;
    private String unidade;
    private double subtotal;

    // Construtor privado para forçar o uso dos métodos de fábrica.
    private GastoItem() {}

    /**
     * Cria um item de gasto baseado no seu peso.
     * @param nome Nome do item (ex: "Picanha").
     * @param precoPorKg Preço do quilo do item.
     * @param gramasUsados Quantidade em gramas usada na refeição.
     * @return um objeto GastoItem configurado.
     */
    public static GastoItem porPeso(String nome, double precoPorKg, double gramasUsados) {
        GastoItem item = new GastoItem();
        item.nome = nome;
        item.precoBase = precoPorKg; // Para carne, o preço base é por Kg
        item.quantidadeUsada = gramasUsados;
        item.unidade = "gramas";
        item.subtotal = (precoPorKg / 1000.0) * gramasUsados;
        return item;
    }

    /**
     * Cria um item de gasto baseado em copos, calculando o valor a partir de um pacote fechado.
     * @param nome Nome do item (ex: "Arroz").
     * @param precoPacote Preço do pacote (ex: 25.90).
     * @param pesoPacoteKg Peso do pacote em quilos (ex: 5.0).
     * @param coposUsados Quantidade de copos usada na refeição.
     * @return um objeto GastoItem configurado.
     */
    public static GastoItem porCopoDePacote(String nome, double precoPacote, double pesoPacoteKg, double coposUsados) {
        // Padrão de culinária: um copo de 250ml de arroz/grãos tem aproximadamente 200g.
        final double GRAMAS_POR_COPO = 200.0;

        double precoPorGrama = precoPacote / (pesoPacoteKg * 1000.0);
        double precoPorCopo = precoPorGrama * GRAMAS_POR_COPO;

        GastoItem item = new GastoItem();
        item.nome = nome;
        item.precoBase = precoPorCopo; // O preço base agora é o valor calculado do copo
        item.quantidadeUsada = coposUsados;
        item.unidade = "copos";
        item.subtotal = precoPorCopo * coposUsados;
        return item;
    }

    // Getters para acessar os dados
    public String getNome() { return nome; }
    public double getPrecoBase() { return precoBase; }
    public double getQuantidadeUsada() { return quantidadeUsada; }
    public String getUnidade() { return unidade; }
    public double getSubtotal() { return subtotal; }
}