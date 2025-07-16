package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.HistorialRecetas;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.HistorialRecetasRepository;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.DW2.InnovaMedic.util.UserUtil;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenancePdfExportServiceImpl implements MaintenancePdfExportService {
    private final MaintenanceCita maintenanceCita;
    private final CitaRepository citaRepository;
    private final HistorialRecetasRepository historialRecetasRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final Storage storage;

    private static final String BUCKET_NAME = "innovamedic-2e69b.firebasestorage.app";

    private byte[] exportarRecetaComoPDF(CitaDTO citaDTO) {
        RecetaDTO recetaDTO = citaDTO.recetaDTO();
        PacienteResumenDTO pacienteResumenDTO = citaDTO.paciente();
        MedicoResumenDTO medicoResumenDTO = citaDTO.medico();

        String nombrePaciente = pacienteResumenDTO.nombre() + " " + pacienteResumenDTO.apellido();
        String grupoSanguineo = pacienteResumenDTO.grupoSanguineo();
        String nombreMedico = medicoResumenDTO.nombre() + " " + medicoResumenDTO.apellido();
        String especialidad = medicoResumenDTO.especialidad();

        Document documento = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(documento, baos);
        documento.open();

        Color azulInnova = new Color(30, 136, 229);
        Color grisClaro = new Color(245, 245, 245);

        Font tituloFont = new Font(Font.HELVETICA, 20, Font.BOLD, azulInnova);
        Font subTituloFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        Paragraph titulo = new Paragraph("Innova Medicine", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        Paragraph subtitulo = new Paragraph("Receta Médica", subTituloFont);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(15);
        documento.add(subtitulo);

        LineSeparator separator = new LineSeparator();
        separator.setLineColor(Color.LIGHT_GRAY);
        documento.add(separator);

        documento.add(new Paragraph("Paciente: " + nombrePaciente, normalFont));
        documento.add(new Paragraph("Fecha: " + recetaDTO.fecha(), normalFont));
        documento.add(new Paragraph("Médico: " + nombreMedico, normalFont));
        documento.add(new Paragraph("Especialidad: " + especialidad, normalFont));
        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("Diagnóstico", subTituloFont));
        documento.add(new Paragraph(citaDTO.diagnostico(), normalFont));
        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("Tratamiento", subTituloFont));
        documento.add(new Paragraph("\n"));

        documento.add(new Paragraph("Medicamentos recetados", subTituloFont));
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 3f, 3f, 3f});

        String[] headers = {"N°", "Medicamento", "Dosis y Frecuencia", "Indicaciones y Duración"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(grisClaro);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            table.addCell(cell);
        }

        List<MedicamentoRecetaDTO> medicamentos = recetaDTO.medicamentos();
        for (int i = 0; i < medicamentos.size(); i++) {
            MedicamentoRecetaDTO med = medicamentos.get(i);

            String medicInfo = String.format(
                    "%s\n%s\nPresentación: %s\nVía: %s",
                    med.nombreGenerico(),
                    med.nombreFarmaceutico(),
                    med.presentacion(),
                    med.viaAdministracion()
            );

            String dosisFrecuencia = String.format(
                    "Dosis: %s\nFrecuencia: %s",
                    med.dosis(),
                    med.frecuencia()
            );

            String indicDuracion = String.format(
                    "Indicaciones: %s\nDuración: %s",
                    med.indicacionesUso(),
                    med.duracionTratamiento()
            );

            PdfPCell celdaNum = new PdfPCell(new Phrase(String.valueOf(i + 1), normalFont));
            PdfPCell celdaMedicamento = new PdfPCell(new Phrase(medicInfo, normalFont));
            PdfPCell celdaDosis = new PdfPCell(new Phrase(dosisFrecuencia, normalFont));
            PdfPCell celdaIndicaciones = new PdfPCell(new Phrase(indicDuracion, normalFont));

            for (PdfPCell cell : List.of(celdaNum, celdaMedicamento, celdaDosis, celdaIndicaciones)) {
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
            }

            table.addCell(celdaNum);
            table.addCell(celdaMedicamento);
            table.addCell(celdaDosis);
            table.addCell(celdaIndicaciones);
        }
        documento.add(table);

        if (recetaDTO.instruccionesAdicionales() != null && !recetaDTO.instruccionesAdicionales().isBlank()) {
            documento.add(new Paragraph("Instrucciones adicionales", subTituloFont));
            documento.add(new Paragraph(recetaDTO.instruccionesAdicionales(), normalFont));
            documento.add(new Paragraph("\n"));
        }

        Paragraph firma = new Paragraph("Firma del médico: ____________________________", normalFont);
        firma.setSpacingBefore(30);
        documento.add(firma);

        documento.close();
        return baos.toByteArray();
    }

    private String subirPdfAFirebase(String ruta, byte[] pdfBytes) {
        BlobId blobId = BlobId.of(BUCKET_NAME, ruta);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("application/pdf")
                .build();
        storage.create(blobInfo, pdfBytes);

        return "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME +
               "/o/" + URLEncoder.encode(ruta, StandardCharsets.UTF_8) + "?alt=media";
    }

    private void guardarHistorial(Integer idMedico, Integer idPaciente, Integer idCitas, String urlPDF) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        Cita cita = citaRepository.findByIdWithRecetaAndMedicamentos(idCitas)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        HistorialRecetas historial = historialRecetasRepository.findByCita_IdCitas(idCitas)
                .orElse(new HistorialRecetas());

        historial.setUrl_pdf(urlPDF);
        historial.setFecha_generado(LocalDate.now());
        historial.setPaciente(paciente);
        historial.setMedico(medico);
        historial.setCita(cita);

        historialRecetasRepository.save(historial);
    }

    @Async
    @Override
    public void generarPDFAsync(Integer idCita) {
        CitaDTO citaDTO = maintenanceCita.obtenerCitaCompletaPorID(idCita);
        byte[] pdf = exportarRecetaComoPDF(citaDTO);

        String pdfFirebase = UserUtil.formatPdfFirebase(idCita);
        String urlPDF = subirPdfAFirebase(pdfFirebase, pdf);

        guardarHistorial(citaDTO.medico().idUsuario(), citaDTO.paciente().idUsuario(), citaDTO.idCitas(), urlPDF);
    }

    @Override
    public boolean estadoPDF(Integer idCita) {
        String pdfFirebase = UserUtil.formatPdfFirebase(idCita);
        return storage.get(BlobId.of(BUCKET_NAME, pdfFirebase)) != null;
    }

    @Override
    public String descargarPDFGenerado(Integer idCita) {
        HistorialRecetas historialRecetas = historialRecetasRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new IllegalArgumentException("No existe PDF registrado para esta cita"));
        return historialRecetas.getUrl_pdf();
    }

    @Override
    public void eliminarArchivo(Integer idCita) {
        String pdfFirebase = UserUtil.formatPdfFirebase(idCita);
        storage.delete(BlobId.of(BUCKET_NAME, pdfFirebase));
    }
}
