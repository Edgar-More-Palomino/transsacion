package com.nttdata.transaccion.service;

import com.nttdata.transaccion.dto.TransferenciaRequestDTO;
import com.nttdata.transaccion.dto.TransaccionRequestDTO;
import com.nttdata.transaccion.exception.CuentaNoEncontradaException;
import com.nttdata.transaccion.exception.SaldoInsuficienteException;
import com.nttdata.transaccion.model.Cuenta;
import com.nttdata.transaccion.model.Movimiento;
import com.nttdata.transaccion.model.Transaccion;
import com.nttdata.transaccion.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final WebClient webClient;

    private static final String CUENTAS_SERVICE_URL = "/api/v1/cuentas";

    @Override
    public Transaccion registrarDeposito(TransaccionRequestDTO dto) {
        // Obtener los detalles de la cuenta
        Cuenta cuenta = obtenerDetallesCuenta(dto.getCuenta());

        // Realizar el depósito usando WebClient
        try {
            webClient.put()
                    .uri(CUENTAS_SERVICE_URL + "/{cuentaId}/depositar", cuenta.getNumeroCuenta())
                    .bodyValue(Movimiento.from(dto.getMonto()))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al realizar el depósito en la cuenta: " + e.getMessage());
        }

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("DEPOSITO");
        transaccion.setMonto(dto.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setCuentaOrigen(dto.getCuenta());

        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion registrarRetiro(TransaccionRequestDTO dto) {
        // Obtener los detalles de la cuenta
        Cuenta cuenta = obtenerDetallesCuenta(dto.getCuenta());

        // Validar que tenga saldo suficiente
        if (cuenta.getSaldo() < dto.getMonto()) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro.");
        }

        // Realizar el retiro usando WebClient
        try {
            webClient.put()
                    .uri(CUENTAS_SERVICE_URL + "/{cuentaId}/retirar", cuenta.getNumeroCuenta())
                    .bodyValue(Movimiento.from(dto.getMonto()))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al realizar el retiro de la cuenta: " + e.getMessage());
        }

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("RETIRO");
        transaccion.setMonto(dto.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setCuentaOrigen(dto.getCuenta());

        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion registrarTransferencia(TransferenciaRequestDTO dto) {
        // Obtener los detalles de las cuentas origen y destino
        Cuenta cuentaOrigen = obtenerDetallesCuenta(dto.getCuentaOrigen());
        Cuenta cuentaDestino = obtenerDetallesCuenta(dto.getCuentaDestino());

        // Validar que la cuenta origen tenga saldo suficiente
        if (cuentaOrigen.getSaldo() < dto.getMonto()) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transferencia.");
        }

        // Realizar el retiro de la cuenta origen
        try {
            webClient.put()
                    .uri(CUENTAS_SERVICE_URL + "/{cuentaId}/retirar", cuentaOrigen.getNumeroCuenta())
                    .bodyValue(Movimiento.from(dto.getMonto()))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al realizar el retiro de la cuenta origen: " + e.getMessage());
        }

        // Realizar el depósito en la cuenta destino
        try {
            webClient.put()
                    .uri(CUENTAS_SERVICE_URL + "/{cuentaId}/depositar", cuentaDestino.getNumeroCuenta())
                    .bodyValue(Movimiento.from(dto.getMonto()))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al realizar el depósito en la cuenta destino: " + e.getMessage());
        }

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("TRANSFERENCIA");
        transaccion.setMonto(dto.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setCuentaOrigen(dto.getCuentaOrigen());
        transaccion.setCuentaDestino(dto.getCuentaDestino());

        return transaccionRepository.save(transaccion);
    }

    @Override
    public List<Transaccion> consultarHistorial() {
        return transaccionRepository.findAll();
    }

    // Método privado para obtener los detalles de una cuenta a través del servicio REST
    private Cuenta obtenerDetallesCuenta(String cuentaId) {
        try {
            return webClient.get()
                    .uri(CUENTAS_SERVICE_URL + "/{id}", cuentaId)
                    .retrieve()
                    .bodyToMono(Cuenta.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new CuentaNoEncontradaException("La cuenta con ID " + cuentaId + " no fue encontrada.");
        }
    }
}