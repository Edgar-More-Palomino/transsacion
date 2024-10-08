package com.nttdata.transaccion.service;
import com.nttdata.transaccion.dto.TransferenciaRequestDTO;
import com.nttdata.transaccion.dto.TransaccionRequestDTO;
import com.nttdata.transaccion.model.Transaccion;

import java.util.List;

public interface TransaccionService {
    Transaccion registrarDeposito(TransaccionRequestDTO dto);
    Transaccion registrarRetiro(TransaccionRequestDTO dto);
    Transaccion registrarTransferencia(TransferenciaRequestDTO dto);
    List<Transaccion> consultarHistorial();
}
