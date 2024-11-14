package com.example.unika_backend.service;

import com.example.unika_backend.model.Cliente;
import com.example.unika_backend.repository.ClienteRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void importClientesPF(MultipartFile planilha) throws IOException {
        List<Cliente> clientes = new ArrayList<>();

        if(!planilha.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            throw new IllegalArgumentException("O tipo de arquivo selecionado não é aceito. Apenas arquivos do tipo .xlsx são aceitos.");
        }

        Workbook workbook = new XSSFWorkbook((planilha.getInputStream()));
        Sheet sheet = workbook.getSheetAt(0);

        if(!sheet.getRow(0).getCell(0).getStringCellValue().equals("Nome")) {
            throw new IllegalArgumentException("A planilha enviada não é equivalente a uma do cadastro de pessoa física.");
        }

        for(Row row : sheet) {
            if(row.getRowNum() == 0) {continue;}

            Cliente cliente = new Cliente();
            cliente.setNome(row.getCell(0).getStringCellValue());
            cliente.setCpfCnpj(row.getCell(1).getStringCellValue());
            cliente.setRg(row.getCell(2).getStringCellValue());
            cliente.setDataNascimento(row.getCell(3).getDateCellValue());
            cliente.setEmail(row.getCell(4).getStringCellValue());
            if(row.getCell(5).getStringCellValue().equals("Ativo")) {
                cliente.setAtivo(true);
            } else {
                cliente.setAtivo(false);
            }
            cliente.setTipoPessoa("FISICA");

            clientes.add(cliente);
        }

        clienteRepository.saveAll(clientes);
        workbook.close();
    }

    public void importClientesPJ(MultipartFile planilha) throws IOException {
        List<Cliente> clientes = new ArrayList<>();

        if(!planilha.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            throw new IllegalArgumentException("O tipo de arquivo selecionado não é aceito. Apenas arquivos do tipo .xlsx são aceitos.");
        }

        Workbook workbook = new XSSFWorkbook((planilha.getInputStream()));
        Sheet sheet = workbook.getSheetAt(0);

        if(!sheet.getRow(0).getCell(0).getStringCellValue().equals("Razão Social")) {
            throw new IllegalArgumentException("A planilha enviada não é equivalente a uma do cadastro de pessoa jurídica.");
        }

        for(Row row : sheet) {
            if(row.getRowNum() == 0) {continue;}

            Cliente cliente = new Cliente();
            cliente.setRazaoSocial(row.getCell(0).getStringCellValue());
            cliente.setCpfCnpj(row.getCell(1).getStringCellValue());
            cliente.setInscricaoEstadual(row.getCell(2).getStringCellValue());
            cliente.setDataCriacao(row.getCell(3).getDateCellValue());
            cliente.setEmail(row.getCell(4).getStringCellValue());
            if(row.getCell(5).getStringCellValue().equals("Ativo")) {
                cliente.setAtivo(true);
            } else {
                cliente.setAtivo(false);
            }
            cliente.setTipoPessoa("JURIDICA");

            clientes.add(cliente);
        }

        clienteRepository.saveAll(clientes);
        workbook.close();
    }
}
