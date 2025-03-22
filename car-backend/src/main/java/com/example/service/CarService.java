package com.example.service;

import com.example.dto.ValidationRequest;
import com.example.dto.ValidationResponse;
import com.example.model.Car;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import java.util.Optional;

@ApplicationScoped
public class CarService {

    private static final Logger LOG = Logger.getLogger(CarService.class);

    @Inject
    @Channel("validation-requests")
    Emitter<ValidationRequest> validationRequestEmitter;

    @Transactional
    public Car saveCar(Car car) {
        car.persist();
        return car;
    }

    public void saveCarAndSendValidation(Car car) {
        Car savedCar = saveCar(car);
        sendValidationRequestAsync(savedCar);
    }

    private void sendValidationRequestAsync(Car car) {
        LOG.infof("Sending validation request for Car ID: %s", car.id);
        ValidationRequest request = new ValidationRequest(car.id, car.name, car.description);
        validationRequestEmitter.send(request)
            .toCompletableFuture().join();
        
        LOG.info("Kafka message sent successfully: " + request);
    }
    

    @Incoming("validation-responses-out")
    @Transactional
    public void processValidationResponse(ValidationResponse response) {
        LOG.infof("Received validation response: ID=%s, Valid=%s", response.id(), response.valid());

        Optional<Car> carOptional = Car.findByIdOptional(response.id());
        if (carOptional.isEmpty()) {
            LOG.warn("Car not found");
            return;
        }

        Car car = carOptional.get();
        car.validated = response.valid();
        car.persist();

        LOG.infof("Updated car validation status: %s -> %s", car.id, response.valid());
    }

    @Transactional
    public void deleteCar(Long id) {
        Optional<Car> carOptional = Car.findByIdOptional(id);
        if (carOptional.isEmpty()) {
            LOG.warnf("Car with ID %s not found, cannot delete.", id);
            throw new NotFoundException("Car not found");
        }

        carOptional.get().delete();
        LOG.infof("Deleted car with ID: %s", id);
    }
}