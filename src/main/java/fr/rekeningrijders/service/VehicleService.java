package fr.rekeningrijders.service;

import fr.rekeningrijders.doa.context.IVehicleStorage;
import fr.rekeningrijders.models.pojo.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless

public class VehicleService {

    @Inject
    IVehicleStorage vehicleStorage;

    public Vehicle create(Vehicle vehicle) {
        vehicleStorage.create(vehicle);
        return vehicle;
    }

    public Vehicle update(Vehicle vehicle) {
        vehicleStorage.update(vehicle);
        return vehicle;
    }

    public List<Vehicle> byOwner(long ownerId) {
        return vehicleStorage.byOwner(ownerId);
    }

    public Vehicle find(long id) {
        return vehicleStorage.find(id);
    }

    public List<Vehicle> getAll() {
        return vehicleStorage.getAll();
    }

    public Vehicle findByTrackerId(String trackerId) {
        return vehicleStorage.findByTrackerId(trackerId);
    }

    public Vehicle findVehicleByLicense(String license) {
        return vehicleStorage.findVehicleByLicense(license);
    }
}
