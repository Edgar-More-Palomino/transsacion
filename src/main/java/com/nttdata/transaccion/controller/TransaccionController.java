package com.nttdata.transaccion.controller;

import com.nttdata.transaccion.dto.TransferenciaRequestDTO;
import com.nttdata.transaccion.dto.TransaccionRequestDTO;
import com.nttdata.transaccion.model.Transaccion;
import com.nttdata.transaccion.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping("/deposito")
    @Operation(summary = "Registrar un depósito")
    public ResponseEntity<Transaccion> registrarDeposito(@RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.registrarDeposito(dto));
    }

    @PostMapping("/retiro")
    @Operation(summary = "Registrar un retiro")
    public ResponseEntity<Transaccion> registrarRetiro(@RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.registrarRetiro(dto));
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Registrar una transferencia")
    public ResponseEntity<Transaccion> registrarTransferencia(@RequestBody TransferenciaRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.registrarTransferencia(dto));
    }

    @GetMapping("/historial")
    @Operation(summary = "Consultar historial de transacciones")
    public ResponseEntity<List<Transaccion>> consultarHistorial() {
        return ResponseEntity.ok(transaccionService.consultarHistorial());
    }


}
