package org.juanrobledo.tp2mongo.controllers;

import org.juanrobledo.tp2mongo.entities.Pais;
import org.juanrobledo.tp2mongo.services.PaisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 5.1 - Obtener países de América
    @GetMapping("/region/americas")
    public List<Pais> obtenerPaisesDeAmerica() {
        return paisService.obtenerPaisesDeAmerica();
    }

    // 5.2 - Países de América con población > 100M
    @GetMapping("/region/americas/altapoblacion")
    public List<Pais> obtenerPaisesDeAmericaConAltaPoblacion() {
        return paisService.obtenerPaisesDeAmericaConAltaPoblacion();
    }

    // 5.3 - Países cuya región no es África
    @GetMapping("/region/noafrica")
    public List<Pais> obtenerPaisesNoAfricanos() {
        return paisService.obtenerPaisesNoAfricanos();
    }

    // 5.4 - Actualizar datos de Egipto
    @PutMapping("/actualizar/egipto")
    public String actualizarDatosDeEgipto() {
        paisService.actualizarDatosDeEgipto();
        return "Datos de Egipto actualizados";
    }

    // 5.5 - Eliminar país con código 258
    @DeleteMapping("/eliminar/258")
    public String eliminarPaisPorCodigo() {
        paisService.eliminarPaisPorCodigo();
        return "País con código 258 eliminado";
    }

    // 5.7 - Países con población entre 50M y 150M
    @GetMapping("/poblacion/intermedia")
    public List<Pais> obtenerPaisesConPoblacionIntermedia() {
        return paisService.obtenerPaisesConPoblacionIntermedia();
    }

    // 5.8 - Ordenar países por nombre
    @GetMapping("/ordenar/nombre")
    public List<Pais> obtenerPaisesOrdenadosPorNombre() {
        return paisService.obtenerPaisesOrdenadosPorNombre();
    }

    // 5.9 - Saltar documentos
    @GetMapping("/skip/{skip}")
    public List<Pais> obtenerPaisesConSkip(@PathVariable int skip) {
        return paisService.obtenerPaisesConSkip(skip);
    }

    // 5.10 - Buscar países por letra inicial
    @GetMapping("/buscar/{letra}")
    public List<Pais> obtenerPaisesPorNombreRegex(@PathVariable String letra) {
        return paisService.obtenerPaisesPorNombreRegex(letra);
    }

    // 5.11 - Crear índice en el campo códigoPais
    @PostMapping("/crearIndice")
    public String crearIndiceEnCodigoPais() {
        paisService.crearIndiceEnCodigoPais();
        return "Índice creado en el campo codigoPais";
    }
}