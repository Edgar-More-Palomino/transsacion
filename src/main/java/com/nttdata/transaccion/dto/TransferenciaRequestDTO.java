package com.nttdata.transaccion.dto;

import lombok.Data;

@Data
public class TransferenciaRequestDTO {
    private Double monto;
    private String cuentaOrigen;
    private String cuentaDestino;
}
