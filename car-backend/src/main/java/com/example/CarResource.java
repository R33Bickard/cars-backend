package com.example;

import com.example.model.Car;
import com.example.service.CarService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Collections;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Car API", description = "API zum Verwalten von Autos")
@Transactional
public class CarResource {

    @Inject
    CarService carService;

    @GET
    @Operation(summary = "Alle Autos abrufen", description = "Gibt eine Liste aller gespeicherten Autos zurück")
    public List<Car> listAll() {
        return Car.listAll();
    }

    @GET
    @Path("/resilient")
    @Timeout(value = 3000) // Timeout nach 3 Sekunden
    @CircuitBreaker(requestVolumeThreshold = 2, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "listAllFallback")
    @Operation(summary = "Resiliente Autos abrufen", description = "Simuliert Verzögerungen, um den Fallback-Mechanismus zu testen")
    public List<Car> listAllResilient(@QueryParam("simulateDelay") Boolean simulateDelay) {
        if (simulateDelay != null && simulateDelay) {
            try {
                // Künstliche Verzögerung von 5 Sekunden – überschreitet den Timeout
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return Car.listAll();
    }

    // Fallback-Methode für listAllResilient – muss denselben Parameter haben und denselben Rückgabetyp liefern
    public List<Car> listAllFallback(Boolean simulateDelay) {
        // Hier kann auch ein spezieller Dummy-Wert zurückgegeben werden.
        // Für dieses Beispiel geben wir eine leere Liste zurück.
        return Collections.emptyList();
    }

    @POST
    @Operation(summary = "Neues Auto speichern", description = "Speichert ein neues Auto in der Datenbank")
    public Car create(Car car) {
        carService.saveCarAndSendValidation(car);
        return car;
    }

    @GET
    @Path("validated")
    @Operation(summary = "Validierte Autos abrufen", description = "Gibt eine Liste aller validierten Autos zurück")
    public List<Car> listValidated() {
        return Car.list("validated", true);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Auto löschen", description = "Löscht ein Auto anhand der ID aus der Datenbank")
    public void delete(@PathParam("id") Long id) {
        carService.deleteCar(id);
    }
}
