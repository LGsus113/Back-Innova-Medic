package com.DW2.InnovaMedic.service;

public interface MaintenancePdfExportService {
    void validarPDFGenerable(Integer idCita);
    void generarPDFAsync(Integer idCita);
    boolean estadoPDF(Integer idCita);
    String descargarPDFGenerado(Integer idCita);
    void eliminarArchivo(Integer idCita);
}
