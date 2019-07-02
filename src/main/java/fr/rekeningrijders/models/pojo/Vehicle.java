package fr.rekeningrijders.models.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehicle
{
    @Id
    @GeneratedValue
    private long id;
    private String brand;
    private String model;
    private String licenseNumber;
    private short buildYear;
    private byte fuelType;

    @OneToOne
    private Tracker tracker;
    
    @ManyToOne
    private Owner owner;

    public Vehicle(String brand, String model, String licenseNumber, short buildYear)
    {
        this.brand = brand;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.buildYear = buildYear;
        this.fuelType = FuelType.BENZINE;
    }
    
    public Vehicle(String brand, String model, String licenseNumber, short buildYear, Tracker tracker, Owner owner)
    {
        this.brand = brand;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.buildYear = buildYear;
        this.tracker = tracker;
        this.owner = owner;
        this.fuelType = FuelType.BENZINE;
    }

    public Vehicle setId(long id)
    {
        this.id = id;
        return this;
    }
}
