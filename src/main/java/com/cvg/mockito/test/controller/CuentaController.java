package com.cvg.mockito.test.controller;

import com.cvg.mockito.test.models.Cuenta;
import com.cvg.mockito.test.models.TransactionDto;
import com.cvg.mockito.test.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> findAll(){
        return this.cuentaService.findAll();
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta save(@RequestBody Cuenta cuenta){
        return this.cuentaService.save(cuenta);
    }

    @GetMapping("/{cuentaId}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable Long cuentaId) {
        return this.cuentaService.findById(cuentaId);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransactionDto obj) {
        this.cuentaService.transferirSaldo(
                obj.getCuentaOrigen(),
                obj.getCuentaDestino(),
                obj.getMonto(),
                obj.getBancoId()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put( "status", "OK" );
        response.put("message", "Transacción exitosa");
        response.put("data", obj);
        return ResponseEntity.ok(response);
    }
}
