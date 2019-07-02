package fr.rekeningrijders.models.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class InvoiceVehicle
{
    @Id @GeneratedValue
    private long id;

    @NotNull
    private String trackerId;
    
    @NotNull @OneToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;

    public InvoiceVehicle(Vehicle vehicle)
    {
        this.vehicle = vehicle;
        this.trackerId = vehicle.getTracker().getTrackerId();
    }
}