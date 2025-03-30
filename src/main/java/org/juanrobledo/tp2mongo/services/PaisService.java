package org.juanrobledo.tp2mongo.services;


import com.mongodb.client.model.Indexes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.juanrobledo.tp2mongo.entities.Pais;
import org.juanrobledo.tp2mongo.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.List;


@Service
public class PaisService {

    @Autowired
    private PaisRepository paisRepository;

    private final WebClient webClient = WebClient.create("https://restcountries.com/v3.1/alpha/");

    @Autowired
    private MongoTemplate mongoTemplate;

    // Metodo para migrar los países de un rango de códigos
    public List<Pais> migrarPaises(int desde, int hasta) {
        for (int codigo = desde; codigo <= hasta; codigo++) {
            String codigoString = String.format("%03d", codigo);  // Formatea el código a 3 dígitos
            try {
                migrarPais(codigoString);  // Llama al metodo de migración para cada código
            } catch (Exception e) {
                System.out.println("Error procesando código " + codigoString + ": " + e.getMessage());
            }
        }
        return paisRepository.findAll();  // Devuelve todos los países almacenados en la base de datos
    }

    // Metodo para migrar un país a la base de datos
    public void migrarPais(String codigoStr) {
        try {
            // Realiza la llamada a la API con WebClient
            String response = webClient.get()
                    .uri(codigoStr)
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            errorResponse -> errorResponse.bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new RuntimeException("Error HTTP: " + error)))
                    )
                    .bodyToMono(String.class)
                    .block();

            // Parsea la respuesta JSON
            JSONArray jsonArray = new JSONArray(response);
            JSONObject json = jsonArray.getJSONObject(0);

            // Extrae los campos de la respuesta JSON
            String nombrePais = json.getJSONObject("name").getString("common");
            String capitalPais = json.optJSONArray("capital") != null ? json.getJSONArray("capital").getString(0) : "No disponible";
            String region = json.getString("region");
            long poblacion = json.getLong("population");
            double latitud = json.getJSONArray("latlng").getDouble(0);
            double longitud = json.getJSONArray("latlng").getDouble(1);
            double superficie = json.has("area") ? json.getDouble("area") : 0.0;  // Superficie opcional

            // Busca si el país ya existe en la base de datos
            Pais paisExistente = paisRepository.findById(codigoStr).orElse(null);

            // Si existe, actualiza el documento, si no, inserta uno nuevo
            if (paisExistente != null) {
                paisExistente.setNombrePais(nombrePais);
                paisExistente.setCapitalPais(capitalPais);
                paisExistente.setRegion(region);
                paisExistente.setPoblacion(poblacion);
                paisExistente.setCoordenadas(Pais.Coordenadas.builder().latitud(latitud).longitud(longitud).build());
                paisExistente.setSuperficie(superficie);
                paisRepository.save(paisExistente);
                System.out.println("País actualizado: " + nombrePais);
            } else {
                // Si no existe, crea un nuevo objeto Pais
                Pais nuevoPais = Pais.builder()
                        .codigoPais(codigoStr)
                        .nombrePais(nombrePais)
                        .capitalPais(capitalPais)
                        .region(region)
                        .poblacion(poblacion)
                        .coordenadas(Pais.Coordenadas.builder().latitud(latitud).longitud(longitud).build())
                        .superficie(superficie)
                        .build();
                paisRepository.save(nuevoPais);  // Guarda el nuevo país en la base de datos
                System.out.println("País insertado: " + nombrePais);
            }

        } catch (WebClientResponseException.NotFound e) {
            // Si el código no tiene datos, se maneja la excepción y se continúa con el siguiente código
            System.out.println("Código " + codigoStr + " no tiene datos disponibles.");
        } catch (Exception e) {
            // Manejo general de excepciones
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 5.1 - Países de Americas
    public List<Pais> obtenerPaisesDeAmerica() {
        return paisRepository.findByRegion("Americas");
    }

    // 5.2 - Países de Americas con población > 100M
    public List<Pais> obtenerPaisesDeAmericaConAltaPoblacion() {
        return paisRepository.findByRegionAndPoblacionGreaterThan("Americas", 100000000);
    }

    // 5.3 - Países cuya región no es Africa
    public List<Pais> obtenerPaisesNoAfricanos() {
        return paisRepository.findByRegionNot("Africa");
    }

    // 5.4 - Actualizar datos de Egipto
    public void actualizarDatosDeEgipto() {
        Pais egipto = paisRepository.findById("Egypt").orElse(null);
        if (egipto != null) {
            egipto.setNombrePais("Egipto");
            egipto.setPoblacion(95000000L);
            paisRepository.save(egipto);
        }
    }

    // 5.5 - Eliminar país por código 258
    public void eliminarPaisPorCodigo() {
        paisRepository.deleteByCodigoPais("258");
    }

    // 5.7 - Países con población entre 50M y 150M
    public List<Pais> obtenerPaisesConPoblacionIntermedia() {
        return paisRepository.findByPoblacionBetween(50000000, 150000000);
    }

    // 5.8 - Ordenar países por nombre
    public List<Pais> obtenerPaisesOrdenadosPorNombre() {
        return paisRepository.findAllByOrderByNombrePaisAsc();
    }

    // 5.9 - Saltar documentos con paginación
    public List<Pais> obtenerPaisesConSkip(int skip) {
        return paisRepository.findAll(PageRequest.of(skip, 5)).getContent();
    }

    // 5.10 - Buscar países por inicial usando regex
    public List<Pais> obtenerPaisesPorNombreRegex(String letra) {
        return paisRepository.findByNombrePaisRegex("^" + letra);
    }
    // 5.11 - Crear un índice en el campo códigoPais
    public void crearIndiceEnCodigoPais() {
        mongoTemplate.getCollection("paises").createIndex(Indexes.ascending("codigoPais"));
    }

}