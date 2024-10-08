package com.nttdata.transaccion.model;

import lombok.Data;

@Data
public class Cuenta {
    private String id;
    private String numeroCuenta;
    private Double saldo;
    private String tipoCuenta;  // AHORROS o CORRIENTE
    private String clienteId;
}
