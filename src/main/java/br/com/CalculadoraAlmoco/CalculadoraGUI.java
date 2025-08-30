package br.com.CalculadoraAlmoco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class CalculadoraGUI extends JFrame {

    private final DefaultListModel<GastoItem> comidaListModel = new DefaultListModel<>();
    private final DefaultListModel<GastoEletrico> energiaListModel = new DefaultListModel<>();

    private JComboBox<String> tipoItemComboBox;
    private JTextField nomeField, campo1, campo2, campo3;
    private JLabel label1, label2, label3;

    private JTextField nomeAparelhoField, potenciaField, minutosField;

    private JList<GastoItem> comidaList;
    private JList<GastoEletrico> energiaList;
    private JLabel totalComidaLabel, totalEnergiaLabel;

    public CalculadoraGUI() {
        setTitle("Calculadora de Custo e Consumo do Almoço");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DE ENTRADAS (ESQUERDA) ---
        // (Nenhuma mudança aqui)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JPanel comidaPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        comidaPanel.setBorder(BorderFactory.createTitledBorder("Adicionar Item de Comida"));
        tipoItemComboBox = new JComboBox<>(new String[]{"Item por Peso", "Item por Copo (de Pacote)", "Item por Unidade (de Pacote)"});
        nomeField = new JTextField();
        label1 = new JLabel(); label2 = new JLabel(); label3 = new JLabel();
        campo1 = new JTextField(); campo2 = new JTextField(); campo3 = new JTextField();
        comidaPanel.add(new JLabel("Tipo de Item:")); comidaPanel.add(tipoItemComboBox);
        comidaPanel.add(new JLabel("Nome do Item:")); comidaPanel.add(nomeField);
        comidaPanel.add(label1); comidaPanel.add(campo1);
        comidaPanel.add(label2); comidaPanel.add(campo2);
        comidaPanel.add(label3); comidaPanel.add(campo3);
        JButton addComidaButton = new JButton("Adicionar Comida");
        comidaPanel.add(new JLabel());
        comidaPanel.add(addComidaButton);
        inputPanel.add(comidaPanel);
        JPanel eletroPanel = new JPanel(new GridLayout(0, 2, 10, 10));
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
        inputPanel.add(eletroPanel);
        inputPanel.add(Box.createVerticalGlue());

        // --- PAINEL DE DISPLAY (DIREITA) ---
        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));
        displayPanel.setBorder(BorderFactory.createTitledBorder("Resumo do Almoço"));
        comidaList = new JList<>(comidaListModel);
        energiaList = new JList<>(energiaListModel);

        // NOVO: Adiciona a funcionalidade de deselecionar com um clique na área vazia
        addDeselectListener(comidaList);
        addDeselectListener(energiaList);

        JPanel listPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        listPanel.add(new JScrollPane(comidaList));
        listPanel.add(new JScrollPane(energiaList));
        displayPanel.add(listPanel, BorderLayout.CENTER);

        // MUDANÇA: Usando GridLayout para garantir que os dois botões apareçam
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton deleteComidaButton = new JButton("Excluir Comida Selecionada");
        JButton deleteEnergiaButton = new JButton("Excluir Energia Selecionada");
        buttonPanel.add(deleteComidaButton);
        buttonPanel.add(deleteEnergiaButton);
        displayPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, displayPanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        // --- PAINEL DE TOTAIS E AÇÕES (EMBAIXO) ---
        // (Nenhuma mudança aqui)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalComidaLabel = new JLabel("Custo Comida: R$ 0,00");
        totalComidaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalEnergiaLabel = new JLabel("Consumo Energia: 0,0000 kWh");
        totalEnergiaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JButton gerarPlanilhaButton = new JButton("Gerar Planilha...");
        bottomPanel.add(totalComidaLabel);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(totalEnergiaLabel);
        bottomPanel.add(gerarPlanilhaButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- AÇÕES ---
        // (Nenhuma mudança aqui)
        tipoItemComboBox.addActionListener(e -> atualizarInterfaceComida());
        addComidaButton.addActionListener(e -> adicionarItemComida());
        addEletroButton.addActionListener(e -> adicionarItemEletrico());
        deleteComidaButton.addActionListener(e -> excluirItemComida());
        deleteEnergiaButton.addActionListener(e -> excluirItemEnergia());
        gerarPlanilhaButton.addActionListener(e -> gerarPlanilha());

        atualizarInterfaceComida();
    }

    // NOVO MÉTODO: Adiciona um listener a uma JList para limpar a seleção
    private void addDeselectListener(JList<?> list) {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!list.getCellBounds(0, list.getModel().getSize() - 1).contains(e.getPoint())) {
                    list.clearSelection();
                }
            }
        });
    }

    // O restante dos métodos permanece o mesmo
    private void atualizarInterfaceComida() {
        String tipo = (String) tipoItemComboBox.getSelectedItem();
        label3.setVisible(true);
        campo3.setVisible(true);
        if ("Item por Peso".equals(tipo)) {
            label1.setText("Preço do Quilo (R$):");
            label2.setText("Gramas Usadas:");
            label3.setVisible(false);
            campo3.setVisible(false);
        } else if ("Item por Copo (de Pacote)".equals(tipo)) {
            label1.setText("Preço do Pacote (R$):");
            label2.setText("Peso do Pacote (Kg):");
            label3.setText("Copos Usados:");
        } else if ("Item por Unidade (de Pacote)".equals(tipo)) {
            label1.setText("Preço Total Pago (R$):");
            label2.setText("Total de Unidades no Pacote:");
            label3.setText("Unidades Usadas:");
        }
    }

    private void adicionarItemComida() {
        try {
            String nome = nomeField.getText();
            if (nome.isEmpty()) throw new IllegalArgumentException("O nome do item não pode ser vazio.");
            String tipo = (String) tipoItemComboBox.getSelectedItem();
            double val1 = Double.parseDouble(campo1.getText().replace(',', '.'));
            double val2 = Double.parseDouble(campo2.getText().replace(',', '.'));

            GastoItem novoItem = null;
            if ("Item por Peso".equals(tipo)) {
                novoItem = GastoItem.porPeso(nome, val1, val2);
            } else {
                double val3 = Double.parseDouble(campo3.getText().replace(',', '.'));
                if ("Item por Copo (de Pacote)".equals(tipo)) {
                    novoItem = GastoItem.porCopoDePacote(nome, val1, val2, val3);
                } else if ("Item por Unidade (de Pacote)".equals(tipo)) {
                    novoItem = GastoItem.porUnidadeDePacote(nome, val1, val2, val3);
                }
            }
            if (novoItem != null) comidaListModel.addElement(novoItem);

            atualizarTotais();
            limparCamposComida();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarItemEletrico() {
        try {
            String nome = nomeAparelhoField.getText();
            if (nome.isEmpty()) throw new IllegalArgumentException("O nome do aparelho não pode ser vazio.");
            double potencia = Double.parseDouble(potenciaField.getText().replace(',', '.'));
            double minutos = Double.parseDouble(minutosField.getText().replace(',', '.'));
            energiaListModel.addElement(GastoEletrico.calcularConsumo(nome, potencia, minutos));
            atualizarTotais();
            limparCamposEletricos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirItemComida() {
        int selectedIndex = comidaList.getSelectedIndex();
        if (selectedIndex != -1) {
            comidaListModel.remove(selectedIndex);
            atualizarTotais();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um item da lista de comida para excluir.", "Nenhum Item Selecionado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirItemEnergia() {
        int selectedIndex = energiaList.getSelectedIndex();
        if (selectedIndex != -1) {
            energiaListModel.remove(selectedIndex);
            atualizarTotais();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um aparelho da lista de energia para excluir.", "Nenhum Item Selecionado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gerarPlanilha() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Planilha Como...");
        fileChooser.setSelectedFile(new File("almoco_custo_consumo.xlsx"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                App.gerarPlanilha(Collections.list(comidaListModel.elements()), Collections.list(energiaListModel.elements()), fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Planilha gerada com sucesso em:\n" + fileToSave.getAbsolutePath(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar a planilha: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarTotais() {
        double totalComida = 0;
        for (int i = 0; i < comidaListModel.size(); i++) {
            totalComida += comidaListModel.get(i).getSubtotal();
        }
        totalComidaLabel.setText(String.format("Custo Comida: R$ %.2f", totalComida).replace('.', ','));

        double totalEnergia = 0;
        for (int i = 0; i < energiaListModel.size(); i++) {
            totalEnergia += energiaListModel.get(i).getConsumoKwh();
        }
        totalEnergiaLabel.setText(String.format("Consumo Energia: %.4f kWh", totalEnergia).replace('.', ','));
    }

    private void limparCamposComida() { nomeField.setText(""); campo1.setText(""); campo2.setText(""); campo3.setText(""); nomeField.requestFocus(); }
    private void limparCamposEletricos() { nomeAparelhoField.setText(""); potenciaField.setText(""); minutosField.setText(""); nomeAparelhoField.requestFocus(); }
}