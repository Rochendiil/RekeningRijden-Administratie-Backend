/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rekeningrijders.models.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "uuid" }) })
public class Invoice
{
    @Id
    @GeneratedValue
    private long id;
    private long ownerId;
    private String uuid;
    @Column(name = "_year")
    private short year;
    @Column(name = "_month")
    private Month month;
    private double totalAmount;
    private boolean isPayed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceVehicle> vehicles = new ArrayList<>();

    public Invoice(String uuid, long ownerId, short year, Month month, double totalAmount, List<InvoiceVehicle> vehicles)
    {
        this.uuid = uuid;
        this.ownerId = ownerId;
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
        this.vehicles = vehicles;
    }


    public Invoice(long ownerId, short year, Month month, double totalAmount, boolean isPayed, List<InvoiceVehicle> vehicles) {
        uuid = UUID.randomUUID().toString();
        this.ownerId = ownerId;
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
        this.isPayed = isPayed;
        this.vehicles = vehicles;
    }

    public Invoice setId(long id)
    {
        this.id = id;
        return this;
    }

    // Called when the invoice is regenerated
    public void update(Invoice updatedInvoice)
    {
        totalAmount = updatedInvoice.totalAmount;
    }

    public void markPayed()
    {
        isPayed = true;
    }
}
