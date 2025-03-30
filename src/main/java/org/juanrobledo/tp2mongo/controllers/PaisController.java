package org.juanrobledo.tp2mongo.controllers;

import org.juanrobledo.tp2mongo.entities.Pais;
import org.juanrobledo.tp2mongo.services.PaisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/migracion")
public class PaisController {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        this.paisService = paisService;
    }


    @GetMapping("/iniciar")
    public ResponseEntity<?> migrarPaises(@RequestParam int desde, @RequestParam int hasta) {
        try {
            List<Pais> listaPaises = paisService.migrarPaises(desde, hasta);
            return ResponseEntity.ok(listaPaises);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}