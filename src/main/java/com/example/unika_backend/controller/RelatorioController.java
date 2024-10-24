package com.example.unika_backend.controller;

import com.example.unika_backend.service.RelatorioService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/clientes/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPDF() throws JRException, IOException {

        byte[] relatorioPDF = relatorioService.gerarRelatorioDeClientes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "relatorio.pdf");

        return new ResponseEntity<>(relatorioPDF, headers, HttpStatus.OK);
    }

}
