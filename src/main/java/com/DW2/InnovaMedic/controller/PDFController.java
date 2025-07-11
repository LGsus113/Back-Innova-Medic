package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pdf")
public class PDFController {
    private final MaintenancePdfExportService maintenancePdfExportService;

    @GetMapping("/estado/receta")
    public ResponseEntity<?> estadoPDF(@RequestParam Integer idCita) {
        boolean listo = maintenancePdfExportService.estadoPDF(idCita);
        return ResponseUtil.successWith(Map.of(
                "listo", listo,
                "mensaje", listo ? "¡Tu receta ya está lista! Tienes 5 minutos para descargarla." : "Estamos generando tu receta médica, vuelve en unos segundos."
        ));
    }

    @GetMapping("/descargar/receta/{idCita}")
    public ResponseEntity<?> descargarPDFGenerado(@PathVariable Integer idCita) {
        String pdfURL = maintenancePdfExportService.descargarPDFGenerado(idCita);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(pdfURL))
                .build();
    }
}
