package br.com.CalculadoraAlmoco;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraGUI extends JFrame {

    private final List<GastoItem> itensComida = new ArrayList<>();
    private final List<GastoEletrico> itensEletricos = new ArrayList<>();

    private JComboBox<String> tipoItemComboBox;
    private JTextField nomeField, campo1, campo2, campo3;
    private JTextField nomeAparelhoField, potenciaField, minutosField; // Campo de tarifa removido
    private JTextArea displayArea;
    private JLabel totalComidaLabel; // Label para o total de comida
    private JLabel totalEnergiaLabel; // Label para o total de energia

    public CalculadoraGUI() {
        setTitle("Calculadora de Custo e Consumo do Almoço");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel mainInputPanel = new JPanel();
        mainInputPanel.setLayout(new BoxLayout(mainInputPanel, BoxLayout.Y_AXIS));

        // Painel de Comida (sem alterações)
        JPanel comidaPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        comidaPanel.setBorder(BorderFactory.createTitledBorder("Adicionar Item de Comida"));
        // ... (código dos componentes de comida é o mesmo)
        tipoItemComboBox = new JComboBox<>(new String[]{"Item por Peso", "Item por Copo"});
        nomeField = new JTextField();
        campo1 = new JTextField();
        campo2 = new JTextField();
        campo3 = new JTextField();
        comidaPanel.add(new JLabel("Tipo de Item:")); comidaPanel.add(tipoItemComboBox);
        comidaPanel.add(new JLabel("Nome do Item:")); comidaPanel.add(nomeField);
        comidaPanel.add(new JLabel("Preço (Kg ou Pacote):")); comidaPanel.add(campo1);
        comidaPanel.add(new JLabel("Gramas / Peso Pacote (Kg):")); comidaPanel.add(campo2);
        comidaPanel.add(new JLabel("Copos Usados:")); comidaPanel.add(campo3);
        JButton addComidaButton = new JButton("Adicionar Comida");
        comidaPanel.add(new JLabel());
        comidaPanel.add(addComidaButton);
        mainInputPanel.add(comidaPanel);


        // Painel de Eletricidade (campo de tarifa removido)
        JPanel eletroPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        eletroPanel.setBorder(BorderFactory.createTitledBorder("Adicionar Consumo de Energia"));
        nomeAparelhoField = new JTextField();
        potenciaField = new JTextField();
        minutosField = new JTextField();
        eletroPanel.add(new JLabel("Nome do Aparelho:")); eletroPanel.add(nomeAparelhoField);
        eletroPanel.add(new JLabel("Potência (Watts):")); eletroPanel.add(potenciaField);
        eletroPanel.add(new JLabel("Minutos de Uso:")); eletroPanel.add(minutosField);
        JButton addEletroButton = new JButton("Adicionar Consumo");
        eletroPanel.add(new JLabel());
        eletroPanel.add(addEletroButton);
        mainInputPanel.add(eletroPanel);

        add(mainInputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Painel de Ações (com dois totais)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalComidaLabel = new JLabel("Custo Comida: R$ 0.00");
        totalComidaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalEnergiaLabel = new JLabel("Consumo Energia: 0.000 kWh");
        totalEnergiaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JButton gerarPlanilhaButton = new JButton("Gerar Planilha...");
        bottomPanel.add(totalComidaLabel);
        bottomPanel.add(Box.createHorizontalStrut(20)); // Espaçador
        bottomPanel.add(totalEnergiaLabel);
        bottomPanel.add(gerarPlanilhaButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ações dos botões
        addComidaButton.addActionListener(e -> adicionarItemComida());
        addEletroButton.addActionListener(e -> adicionarItemEletrico());
        gerarPlanilhaButton.addActionListener(e -> gerarPlanilha());
    }

    private void adicionarItemEletrico() {
        try {
            String nome = nomeAparelhoField.getText();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do aparelho não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double potencia = Double.parseDouble(potenciaField.getText());
            double minutos = Double.parseDouble(minutosField.getText());
            // A chamada agora é para o novo método sem a tarifa
            itensEletricos.add(GastoEletrico.calcularConsumo(nome, potencia, minutos));
            atualizarDisplay();
            limparCamposEletricos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira números válidos nos campos de energia.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDisplay() {
        displayArea.setText("");
        displayArea.append("--- ITENS DE COMIDA ---\n");
        double totalComida = 0;
        for (GastoItem item : itensComida) {
            displayArea.append(String.format("%-25s | R$ %5.2f\n", item.getNome(), item.getSubtotal()));
            totalComida += item.getSubtotal();
        }

        displayArea.append("\n--- CONSUMO DE ENERGIA ---\n");
        double totalEletricoKwh = 0;
        for (GastoEletrico item : itensEletricos) {
            // Mostra o consumo em kWh
            displayArea.append(String.format("%-25s | %6.4f kWh\n", item.getNomeAparelho(), item.getConsumoKwh()));
            totalEletricoKwh += item.getConsumoKwh();
        }

        totalComidaLabel.setText(String.format("Custo Comida: R$ %.2f", totalComida));
        totalEnergiaLabel.setText(String.format("Consumo Energia: %.4f kWh", totalEletricoKwh));
    }

    // O resto dos métodos (adicionarItemComida, gerarPlanilha, limparCampos) não precisa de grandes mudanças
    // e estão incluídos aqui para o código ficar completo.

    private void adicionarItemComida() {
        try {
            String tipo = (String) tipoItemComboBox.getSelectedItem();
            String nome = nomeField.getText();
            if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "O nome do item não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE); return; }
            if ("Item por Peso".equals(tipo)) {
                double precoKg = Double.parseDouble(campo1.getText());
                double gramas = Double.parseDouble(campo2.getText());
                itensComida.add(GastoItem.porPeso(nome, precoKg, gramas));
            } else {
                double precoPacote = Double.parseDouble(campo1.getText());
                double pesoPacote = Double.parseDouble(campo2.getText());
                double copos = Double.parseDouble(campo3.getText());
                itensComida.add(GastoItem.porCopoDePacote(nome, precoPacote, pesoPacote, copos));
            }
            atualizarDisplay();
            limparCamposComida();
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Por favor, insira números válidos nos campos de comida.", "Erro de Formato", JOptionPane.ERROR_MESSAGE); }
    }

    private void gerarPlanilha() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Planilha Como...");
        fileChooser.setSelectedFile(new File("almoco_custo_consumo.xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                App.gerarPlanilha(itensComida, itensEletricos, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Planilha gerada com sucesso em:\n" + fileToSave.getAbsolutePath(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) { JOptionPane.showMessageDialog(this, "Erro ao gerar a planilha: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void limparCamposComida() { nomeField.setText(""); campo1.setText(""); campo2.setText(""); campo3.setText(""); nomeField.requestFocus(); }
    private void limparCamposEletricos() { nomeAparelhoField.setText(""); potenciaField.setText(""); minutosField.setText(""); nomeAparelhoField.requestFocus(); }
}