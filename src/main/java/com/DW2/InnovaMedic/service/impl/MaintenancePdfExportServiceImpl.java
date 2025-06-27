package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Transactional
public class MaintenancePdfExportServiceImpl implements MaintenancePdfExportService {
    @Override
    public byte[] exportarRecetaComoPDF(CitaDTO citaDTO) throws Exception {
        RecetaDTO recetaDTO = citaDTO.recetaDTO();
        PacienteResumenDTO pacienteResumenDTO = citaDTO.paciente();
        MedicoResumenDTO medicoResumenDTO = citaDTO.medico();

        String nombrePaciente = pacienteResumenDTO.nombre() + " " + pacienteResumenDTO.apellido();
        String grupoSanguineo = pacienteResumenDTO.grupoSanguineo();
        String nombreMedico = medicoResumenDTO.nombre() + " " + medicoResumenDTO.apellido();
        String especialidad = medicoResumenDTO.especialidad();

        Document documento = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(documento, baos);

        documento.open();

        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
        Font subTituloFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font negritaFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        Paragraph titulo = new Paragraph("Innova Medicine\nReceta Médica", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);

        documento.add(new Paragraph("Paciente: " + nombrePaciente, normalFont));
        documento.add(new Paragraph("Grupo sanguíneo: " + grupoSanguineo, normalFont));
        documento.add(new Paragraph("Fecha: " + recetaDTO.fecha(), normalFont));
        documento.add(new Paragraph("Médico: " + nombreMedico, normalFont));
        documento.add(new Paragraph("Especialidad: " + especialidad, normalFont));
        documento.add(new Paragraph(" "));

        documento.add(new Paragraph("Diagnóstico:", subTituloFont));
        documento.add(new Paragraph(citaDTO.diagnostico(), normalFont));
        documento.add(new Paragraph(" "));

        documento.add(new Paragraph("Tratamiento:", subTituloFont));
        documento.add(new Paragraph(citaDTO.tratamiento(), normalFont));
        documento.add(new Paragraph(" "));

        documento.add(new Paragraph("Medicamentos recetados:", subTituloFont));
        PdfPTable table = new PdfPTable(4); // N°, Nombre, Dosis, Frecuencia
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell(new PdfPCell(new Phrase("N°", negritaFont)));
        table.addCell(new PdfPCell(new Phrase("Nombre", negritaFont)));
        table.addCell(new PdfPCell(new Phrase("Dosis", negritaFont)));
        table.addCell(new PdfPCell(new Phrase("Frecuencia", negritaFont)));

        List<MedicamentoRecetaDTO> medicamentos = recetaDTO.medicamentos();
        for (int i = 0; i < medicamentos.size(); i++) {
            MedicamentoRecetaDTO med = medicamentos.get(i);
            table.addCell(String.valueOf(i + 1));
            table.addCell(med.nombre());
            table.addCell(med.dosis());
            table.addCell(med.frecuencia());
        }

        documento.add(table);

        if (recetaDTO.instruccionesAdicionales() != null && !recetaDTO.instruccionesAdicionales().isBlank()) {
            documento.add(new Paragraph("Instrucciones adicionales:", subTituloFont));
            documento.add(new Paragraph(recetaDTO.instruccionesAdicionales(), normalFont));
            documento.add(new Paragraph(" "));
        }

        documento.add(new Paragraph("Firma del médico: ____________________________", normalFont));

        documento.close();

        return baos.toByteArray();
    }
}
