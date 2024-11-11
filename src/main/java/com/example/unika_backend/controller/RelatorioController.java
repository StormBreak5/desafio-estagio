package com.example.unika_backend.controller;

import com.example.unika_backend.service.ExcelService;
import com.example.unika_backend.service.RelatorioService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = "http://localhost:4200")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private ExcelService excelService;

    @GetMapping("/clientes/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPDF() throws JRException, IOException {

        byte[] relatorioPDF = relatorioService.gerarRelatorioDeClientes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "relatorio.pdf");

        return new ResponseEntity<>(relatorioPDF, headers, HttpStatus.OK);
    }

    @GetMapping("/clientes/pdfPJ")
    public ResponseEntity<byte[]> gerarRelatorioPDFPJ() throws JRException, IOException {

        byte[] relatorioPDF = relatorioService.gerarRelatorioDeClientesPJ();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "relatorioPJ.pdf");

        return new ResponseEntity<>(relatorioPDF, headers, HttpStatus.OK);
    }

    @GetMapping("/clientes/excel")
    public ResponseEntity<byte[]> gerarRelatorioExcel() throws IOException {

        byte[] relatorioExcel = excelService.gerarClientesExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "clientes_pf.xlsx");

        return new ResponseEntity<>(relatorioExcel, headers, HttpStatus.OK);
    }

    @GetMapping("/clientes/excelPJ")
    public ResponseEntity<byte[]> gerarRelatorioExcelPJ() throws IOException {

        byte[] relatorioExcel = excelService.gerarClientesExcelPJ();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "clientes_pj.xlsx");

        return new ResponseEntity<>(relatorioExcel, headers, HttpStatus.OK);
    }
}
