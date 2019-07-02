package fr.rekeningrijders.rest.dto;

import fr.rekeningrijders.models.pojo.Owner;
import fr.rekeningrijders.models.pojo.Tracker;
import fr.rekeningrijders.models.pojo.Vehicle;
import fr.rekeningrijders.rest.validators.TrackerValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehicleDTO
{
    private long id;
    private long ownerId;
    private byte fuelType;

    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String licenseNumber;
    @Min(1900)
    private short buildYear;
    @TrackerValid(allowNull = true)
    private String trackerId;

    private VehicleDTO(Vehicle vehicle)
    {
        id = vehicle.getId();
        brand = vehicle.getBrand();
        model = vehicle.getModel();
        licenseNumber = vehicle.getLicenseNumber();
        buildYear = vehicle.getBuildYear();
        trackerId = vehicle.getTracker() != null ? vehicle.getTracker().getTrackerId() : null;
        ownerId = vehicle.getOwner() != null ? vehicle.getOwner().getId() : null;
        fuelType = vehicle.getFuelType();
    }

    public Vehicle asVehicle(Tracker tracker, Owner owner)
    {
        return new Vehicle(brand, model, licenseNumber, buildYear, tracker, owner).setId(id);
    }

    public static VehicleDTO make(Vehicle vehicle)
    {
        return vehicle != null ? new VehicleDTO(vehicle) : null;
    }

    public static List<VehicleDTO> transform(List<Vehicle> vehicles)
    {
        List<VehicleDTO> result = new ArrayList<>(vehicles.size());
        for (Vehicle vehicle : vehicles) {
            result.add(new VehicleDTO(vehicle));
        }
        return result;
    }
}
