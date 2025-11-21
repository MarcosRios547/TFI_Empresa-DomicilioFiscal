package com.tfi.empresa.main;

import com.tfi.empresa.entities.*;
import com.tfi.empresa.service.*;

public class TestServicio {
    public static void main(String[] args) {
        EmpresaServicio empresaService = new EmpresaServicio();

        DomicilioFiscal domicilio = new DomicilioFiscal(null, false, "Calle Falsa", 123, "Buenos Aires", "BA", "1000", "Argentina");
        Empresa empresa = new Empresa(null, false, "Tech Solutions", "30-12345678-1", "Software", "info@tech.com", domicilio);

        empresaService.crear(empresa); 
    }
}
