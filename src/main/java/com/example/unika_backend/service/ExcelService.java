package com.example.unika_backend.service;

import com.example.unika_backend.model.Cliente;
import com.example.unika_backend.repository.ClienteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private ClienteRepository clienteRepository;

    private static String[] colunas = {"Nome", "CPF", "RG", "Data de Nascimento", "E-mail", "Status"};
    private static String[] colunasPJ = {"Razão Social", "CNPJ", "Inscrição Estadual", "Data de Criação", "E-mail", "Status"};

    List<Cliente> clientes;

    //Criação do arquivo xls de pessoas Físicas
    public byte[] gerarClientesExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes PF");
        int row = 1;
        Row header = sheet.createRow(0);

        for(int i = 0; i < colunas.length; i++){
            header.createCell(i).setCellValue(colunas[i]);

        }

        //Pega todos os clientes do banco de dados
        clientes = clienteRepository.findByTipoPessoa("FISICA");

        //Formatação de data
        CellStyle estiloData = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        estiloData.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

        //Designação de cada cliente nas linhas
        for (Cliente cliente : clientes) {
            Row linha = sheet.createRow(row++);
            linha.createCell(0).setCellValue(cliente.getNome());
            linha.createCell(1).setCellValue(cliente.getCpfCnpj());
            linha.createCell(2).setCellValue(cliente.getRg());

            //tratamento para campo de Data de nascimento
            Cell nascimento = linha.createCell(3);
            nascimento.setCellStyle(estiloData);
            nascimento.setCellValue(cliente.getDataNascimento());

            linha.createCell(4).setCellValue(cliente.getEmail());
            linha.createCell(5).setCellValue(cliente.getAtivo() ? "Ativo" : "Inativo"); //Evitar que traga 'true' ou 'false'
        }

        for(int i = 0; i < colunas.length; i++){
            sheet.autoSizeColumn(i); //após a inserção dos dados para que todos os dados sejam totalmente visíveis
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    public byte[] gerarClientesExcelPJ() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes PJ");
        int row = 1;
        Row header = sheet.createRow(0);

        for(int i = 0; i < colunas.length; i++){
            header.createCell(i).setCellValue(colunasPJ[i]);
        }

        //Pega todos os clientes do banco de dados
        clientes = clienteRepository.findByTipoPessoa("JURIDICA");

        //Formatação de data
        CellStyle estiloData = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        estiloData.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

        //Designação de cada cliente nas linhas
        for (Cliente cliente : clientes) {
            Row linha = sheet.createRow(row++);
            linha.createCell(0).setCellValue(cliente.getRazaoSocial());
            linha.createCell(1).setCellValue(cliente.getCpfCnpj());
            linha.createCell(2).setCellValue(cliente.getInscricaoEstadual());

            //tratamento para campo de Data de nascimento
            Cell nascimento = linha.createCell(3);
            nascimento.setCellStyle(estiloData);
            nascimento.setCellValue(cliente.getDataCriacao());

            linha.createCell(4).setCellValue(cliente.getEmail());
            linha.createCell(5).setCellValue(cliente.getAtivo() ? "Ativo" : "Inativo"); //Evitar que traga 'true' ou 'false'
        }

        for(int i = 0; i < colunas.length; i++){
            sheet.autoSizeColumn(i); //após a inserção dos dados para que todos os dados sejam totalmente visíveis
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
