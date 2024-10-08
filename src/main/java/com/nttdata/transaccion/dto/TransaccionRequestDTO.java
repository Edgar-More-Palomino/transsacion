package com.nttdata.transaccion.dto;

import lombok.Data;

@Data
public class TransaccionRequestDTO {
    private Double monto;
    private String cuenta;
}
