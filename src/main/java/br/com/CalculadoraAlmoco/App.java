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
        Sheet sheet = workbook.createSheet("Custo e Consumo do Almoço");

        // Estilos
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("\"R$\" #,##0.00"));
        CellStyle kwhStyle = workbook.createCellStyle();
        kwhStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0000\" kWh\""));
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        int rowNum = 0;
        double totalComida = 0;

        // Seção de Comida
        Row headerComida = sheet.createRow(rowNum++);
        headerComida.createCell(0).setCellValue("CUSTO DE COMIDA");
        headerComida.getCell(0).setCellStyle(headerStyle);
        for (GastoItem item : itensComida) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNome());
            Cell subtotalCell = row.createCell(1);
            subtotalCell.setCellValue(item.getSubtotal());
            subtotalCell.setCellStyle(currencyStyle);
            totalComida += item.getSubtotal();
        }

        rowNum++; // Linha em branco

        // Seção de Energia
        double totalEnergia = 0;
        Row headerEletro = sheet.createRow(rowNum++);
        headerEletro.createCell(0).setCellValue("CONSUMO DE ENERGIA");
        headerEletro.getCell(0).setCellStyle(headerStyle);
        for (GastoEletrico item : itensEletricos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getNomeAparelho());
            Cell consumoCell = row.createCell(1);
            consumoCell.setCellValue(item.getConsumoKwh());
            consumoCell.setCellStyle(kwhStyle);
            totalEnergia += item.getConsumoKwh();
        }

        rowNum++; // Linha em branco

        // Totais
        Row totalComidaRow = sheet.createRow(rowNum++);
        totalComidaRow.createCell(0).setCellValue("Total Comida");
        totalComidaRow.getCell(0).setCellStyle(headerStyle);
        Cell totalComidaCell = totalComidaRow.createCell(1);
        totalComidaCell.setCellValue(totalComida);
        totalComidaCell.setCellStyle(currencyStyle);

        Row totalEnergiaRow = sheet.createRow(rowNum++);
        totalEnergiaRow.createCell(0).setCellValue("Total Energia");
        totalEnergiaRow.getCell(0).setCellStyle(headerStyle);
        Cell totalEnergiaCell = totalEnergiaRow.createCell(1);
        totalEnergiaCell.setCellValue(totalEnergia);
        totalEnergiaCell.setCellStyle(kwhStyle);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}