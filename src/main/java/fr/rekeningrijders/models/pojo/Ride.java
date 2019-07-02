package fr.rekeningrijders.models.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.Month;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Ride
{
    @Id @GeneratedValue
    private long id;
    private String trackerId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="lat",column=@Column(name="fromLat")),
        @AttributeOverride(name="lon",column=@Column(name="fromLon")),
        @AttributeOverride(name="timestamp",column=@Column(name="fromTimestamp")),
    })
    private Point from;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="lat",column=@Column(name="toLat")),
        @AttributeOverride(name="lon",column=@Column(name="toLon")),
        @AttributeOverride(name="timestamp",column=@Column(name="toTimestamp")),
    })
    private Point to;

    private short year;
    private Month month;
    private double totalDistance;

    public Ride(Movement from, Movement to, short year, Month month, double totalDisance)
    {
        this.year = year;
        this.month = month;
        this.trackerId = from.getTrackerId();
        this.from = new Point(from.getLat(), from.getLon(), from.getTimestamp());
        this.to = new Point(to.getLat(), to.getLon(), to.getTimestamp());
        this.totalDistance = totalDisance;
    }
}