package br.com.CalculadoraAlmoco;

public class GastoEletrico {
    private String nomeAparelho;
    private double consumoKwh;

    private GastoEletrico(String nomeAparelho, double consumoKwh) {
        this.nomeAparelho = nomeAparelho;
        this.consumoKwh = consumoKwh;
    }

    /**
     * Calcula o consumo de energia de um aparelho em kWh.
     * @param nomeAparelho O nome do aparelho (ex: "Airfryer").
     * @param potenciaWatts A potência do aparelho em Watts.
     * @param minutosDeUso Por quantos minutos o aparelho foi usado.
     * @return um objeto GastoEletrico com o consumo em kWh calculado.
     */

    public static GastoEletrico calcularConsumo(String nomeAparelho, double potenciaWatts, double minutosDeUso) {
        double horasDeUso = minutosDeUso / 60.0;
        double consumoCalculadoKwh = (potenciaWatts / 1000.0) * horasDeUso;
        return new GastoEletrico(nomeAparelho, consumoCalculadoKwh);
    }

    public String getNomeAparelho() {
        return nomeAparelho;
    }

    public double getConsumoKwh() {
        return consumoKwh;
    }
}