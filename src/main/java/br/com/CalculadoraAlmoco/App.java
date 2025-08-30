package br.com.CalculadoraAlmoco;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI gui = new CalculadoraGUI();
            gui.setVisible(true);
        });
    }

    public static void gerarPlanilha(List<GastoItem> itensComida, List<GastoEletrico> itensEletricos, String caminhoArquivo) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório Detalhado de Custos");

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("\"R$\" #,##0.00"));
        CellStyle kwhStyle = workbook.createCellStyle();
        kwhStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0000\" kWh\""));
        CellStyle genericNumberStyle = workbook.createCellStyle();
        genericNumberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        int rowNum = 0;
        double totalComida = 0;

        String[] colunasComida = {"Item", "Tipo de Cálculo", "Preço Pago (R$)", "Peso/Unid. Totais Pacote", "Qtd. Usada", "Unidade", "Subtotal (R$)"};
        Row headerComida = sheet.createRow(rowNum++);
        for (int i = 0; i < colunasComida.length; i++) {
            headerComida.createCell(i).setCellValue(colunasComida[i]);
            headerComida.getCell(i).setCellStyle(headerStyle);
        }

        for (GastoItem item : itensComida) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            row.createCell(cellNum++).setCellValue(item.getNome());
            row.createCell(cellNum++).setCellValue(item.getTipoCalculo());
            Cell precoInputCell = row.createCell(cellNum++);
            precoInputCell.setCellValue(item.getPrecoInput());
            precoInputCell.setCellStyle(currencyStyle);
            Cell pesoUnidCell = row.createCell(cellNum++);
            if (item.getPesoOuTotalUnidadesInput() > 0) {
                pesoUnidCell.setCellValue(item.getPesoOuTotalUnidadesInput());
            } else {
                pesoUnidCell.setCellValue("N/A");
            }
            row.createCell(cellNum++).setCellValue(item.getQuantidadeUsada());
            row.createCell(cellNum++).setCellValue(item.getUnidade());
            Cell subtotalCell = row.createCell(cellNum++);
            subtotalCell.setCellValue(item.getSubtotal());
            subtotalCell.setCellStyle(currencyStyle);
            totalComida += item.getSubtotal();
        }

        rowNum += 2;

        double totalEnergia = 0;
        String[] colunasEletro = {"Aparelho", "Potência (W)", "Tempo de Uso (min)", "Consumo (kWh)"};
        Row headerEletro = sheet.createRow(rowNum++);
        for (int i = 0; i < colunasEletro.length; i++) {
            headerEletro.createCell(i).setCellValue(colunasEletro[i]);
            headerEletro.getCell(i).setCellStyle(headerStyle);
        }

        for (GastoEletrico item : itensEletricos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNomeAparelho());
            row.createCell(1).setCellValue(item.getPotenciaWatts());
            row.createCell(2).setCellValue(item.getMinutosDeUso());
            Cell consumoCell = row.createCell(3);
            consumoCell.setCellValue(item.getConsumoKwh());
            consumoCell.setCellStyle(kwhStyle);
            totalEnergia += item.getConsumoKwh();
        }

        rowNum += 2;

        Row totalComidaRow = sheet.createRow(rowNum++);
        totalComidaRow.createCell(0).setCellValue("Total Custo Comida");
        totalComidaRow.getCell(0).setCellStyle(headerStyle);
        Cell totalComidaCell = totalComidaRow.createCell(1);
        totalComidaCell.setCellValue(totalComida);
        totalComidaCell.setCellStyle(currencyStyle);

        Row totalEnergiaRow = sheet.createRow(rowNum++);
        totalEnergiaRow.createCell(0).setCellValue("Total Consumo Energia");
        totalEnergiaRow.getCell(0).setCellStyle(headerStyle);
        Cell totalEnergiaCell = totalEnergiaRow.createCell(1);
        totalEnergiaCell.setCellValue(totalEnergia);
        totalEnergiaCell.setCellStyle(kwhStyle);

        for(int i = 0; i < colunasComida.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}