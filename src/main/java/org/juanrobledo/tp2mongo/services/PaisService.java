package org.juanrobledo.tp2mongo.services;


import org.json.JSONArray;
import org.json.JSONObject;
import org.juanrobledo.tp2mongo.entities.Pais;
import org.juanrobledo.tp2mongo.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<Pais> migrarPaises(int desde, int hasta) {
        for (int codigo = desde; codigo <= hasta; codigo++) {
            String codigoString = String.format("%03d", codigo);
            try {
                migrarPais(codigoString);
            } catch (Exception e) {
                System.out.println("Error procesando código " + codigoString + ": " + e.getMessage());
            }
        }
        return paisRepository.findAll();
    }

    public void migrarPais(String codigoStr) {
        try {
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

            JSONArray jsonArray = new JSONArray(response);
            JSONObject json = jsonArray.getJSONObject(0);

            String nombrePais = json.getJSONObject("name").getString("common");
            String capitalPais = json.getJSONArray("capital").getString(0);
            String region = json.getString("region");
            String subregion = json.getString("subregion");
            long poblacion = json.getLong("population");
            double latitud = json.getJSONArray("latlng").getDouble(0);
            double longitud = json.getJSONArray("latlng").getDouble(1);
            int codigoPais = Integer.parseInt(codigoStr);

            Pais paisExistente = paisRepository.findById(codigoPais).orElse(null);

            if (paisExistente != null) {
                paisExistente.setNombrePais(nombrePais);
                paisExistente.setCapitalPais(capitalPais);
                paisExistente.setRegion(region);
                paisExistente.setSubregion(subregion);
                paisExistente.setPoblacion(poblacion);
                paisExistente.setLatitud(latitud);
                paisExistente.setLongitud(longitud);
                paisRepository.save(paisExistente);
                System.out.println("País actualizado: " + nombrePais);
            } else {
                Pais nuevoPais = new Pais(codigoPais, nombrePais, capitalPais, region, subregion, poblacion, latitud, longitud);
                paisRepository.save(nuevoPais);
                System.out.println("País insertado: " + nombrePais);
            }

        } catch (WebClientResponseException.NotFound e) {
            System.out.println("Código " + codigoStr + " no tiene datos disponibles.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}