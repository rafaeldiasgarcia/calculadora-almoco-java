package br.com.CalculadoraAlmoco;

public class GastoItem {
    private String nome;
    private double subtotal;
    private double quantidadeUsada;
    private String unidade;
    private String tipoCalculo;
    private double precoInput;
    private double pesoOuTotalUnidadesInput;

    private GastoItem() {}

    public static GastoItem porPeso(String nome, double precoPorKg, double gramasUsados) {
        GastoItem item = new GastoItem();
        item.nome = nome;
        item.quantidadeUsada = gramasUsados;
        item.unidade = "gramas";
        item.subtotal = (precoPorKg / 1000.0) * gramasUsados;
        item.tipoCalculo = "Por Peso";
        item.precoInput = precoPorKg;
        item.pesoOuTotalUnidadesInput = 0;
        return item;
    }

    public static GastoItem porCopoDePacote(String nome, double precoPacote, double pesoPacoteKg, double coposUsados) {
        final double GRAMAS_POR_COPO = 200.0;
        double precoPorGrama = precoPacote / (pesoPacoteKg * 1000.0);
        double precoPorCopo = precoPorGrama * GRAMAS_POR_COPO;
        GastoItem item = new GastoItem();
        item.nome = nome;
        item.quantidadeUsada = coposUsados;
        item.unidade = "copos";
        item.subtotal = precoPorCopo * coposUsados;
        item.tipoCalculo = "Por Copo (Pacote)";
        item.precoInput = precoPacote;
        item.pesoOuTotalUnidadesInput = pesoPacoteKg;
        return item;
    }

    public static GastoItem porUnidadeDePacote(String nome, double precoTotalPago, double totalUnidades, double unidadesUsadas) {
        double precoPorUnidade = precoTotalPago / totalUnidades;
        GastoItem item = new GastoItem();
        item.nome = nome;
        item.quantidadeUsada = unidadesUsadas;
        item.unidade = "unidades";
        item.subtotal = precoPorUnidade * unidadesUsadas;
        item.tipoCalculo = "Por Unidade (Pacote)";
        item.precoInput = precoTotalPago;
        item.pesoOuTotalUnidadesInput = totalUnidades;
        return item;
    }

    // Usado para exibir na JList
    @Override
    public String toString() {
        return String.format("%-25s | R$ %5.2f", nome, subtotal);
    }

    // Getters para a planilha
    public String getNome() { return nome; }
    public double getSubtotal() { return subtotal; }
    public double getQuantidadeUsada() { return quantidadeUsada; }
    public String getUnidade() { return unidade; }
    public String getTipoCalculo() { return tipoCalculo; }
    public double getPrecoInput() { return precoInput; }
    public double getPesoOuTotalUnidadesInput() { return pesoOuTotalUnidadesInput; }
}