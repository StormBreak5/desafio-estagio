package com.example.unika_backend.service;

import com.example.unika_backend.model.Cliente;
import com.example.unika_backend.repository.ClienteRepository;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired
    private ClienteRepository clienteRepository;

    public byte[] gerarRelatorioDeClientes() throws JRException, FileNotFoundException {

        File arquivo = ResourceUtils.getFile("classpath:clientes_template.jrxml");
        JasperReport report = JasperCompileManager.compileReport(arquivo.getAbsolutePath());

        //InputStream relatorioStream = this.getClass().getResourceAsStream("/clientes_template.jrxml");

        List<Cliente> clientes = clienteRepository.findAll();
        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(clientes);

        Map<String, Object> params = new HashMap<>();
        params.put("titulo", "Clientes");

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, datasource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
