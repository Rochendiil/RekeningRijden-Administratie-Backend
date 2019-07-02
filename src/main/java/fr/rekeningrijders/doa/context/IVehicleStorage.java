/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rekeningrijders.doa.context;

import fr.rekeningrijders.models.pojo.Vehicle;

import java.util.List;

/**
 *
 * @author piet
 */
public interface IVehicleStorage {

    void create(Vehicle vehicle);

    Vehicle find(long id);

    void update(Vehicle vehicle);

    List<Vehicle> getAll();

    List<Vehicle> byOwner(long ownerId);

    Vehicle findByTrackerId(String trackerId);

    Vehicle findVehicleByLicense(String license);
}
