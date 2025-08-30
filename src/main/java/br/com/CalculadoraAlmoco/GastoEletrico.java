package br.com.CalculadoraAlmoco;

public class GastoEletrico {
    private String nomeAparelho;
    private double consumoKwh;
    private double potenciaWatts;
    private double minutosDeUso;

    private GastoEletrico() {}

    public static GastoEletrico calcularConsumo(String nomeAparelho, double potenciaWatts, double minutosDeUso) {
        double horasDeUso = minutosDeUso / 60.0;
        double consumoCalculadoKwh = (potenciaWatts / 1000.0) * horasDeUso;

        GastoEletrico novoGasto = new GastoEletrico();
        novoGasto.nomeAparelho = nomeAparelho;
        novoGasto.consumoKwh = consumoCalculadoKwh;
        novoGasto.potenciaWatts = potenciaWatts;
        novoGasto.minutosDeUso = minutosDeUso;

        return novoGasto;
    }

    // NOVO MÉTODO: Ensina a JList a exibir o objeto de forma amigável
    @Override
    public String toString() {
        // Formata o texto para alinhar, mostrando o nome e o consumo
        return String.format("%-25s | %6.4f kWh", nomeAparelho, consumoKwh);
    }

    // Getters
    public String getNomeAparelho() { return nomeAparelho; }
    public double getConsumoKwh() { return consumoKwh; }
    public double getPotenciaWatts() { return potenciaWatts; }
    public double getMinutosDeUso() { return minutosDeUso; }
}