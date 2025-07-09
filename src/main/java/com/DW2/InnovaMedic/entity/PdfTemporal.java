package com.DW2.InnovaMedic.entity;

import lombok.Getter;

@Getter
public class PdfTemporal {
    private final byte[] contenido;
    private final long timestamp;

    public PdfTemporal(byte[] contenido) {
        this.contenido = contenido;
        this.timestamp = System.currentTimeMillis();
    }
}
