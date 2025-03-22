package com.example;

import com.example.model.Car;
import com.example.service.CarService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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