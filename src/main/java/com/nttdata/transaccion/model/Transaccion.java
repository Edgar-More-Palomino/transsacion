package com.nttdata.transaccion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    private String tipo;  // DEPÓSITO, RETIRO, TRANSFERENCIA
    private Double monto;
    private LocalDateTime fecha;
    private String cuentaOrigen;
    private String cuentaDestino;  // Solo en caso de transferencias
}
