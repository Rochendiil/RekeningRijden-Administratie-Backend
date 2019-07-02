package fr.rekeningrijders.models.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Movement
{
    private long id;
    private double lat;
    private double lon;
    private long timestamp;
    private String trackerId;

    public Movement()
    {
        // Null
    }

    public Movement(long id, double lat, double lon, Date timestamp, String trackerId)
    {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp.getTime();
        this.trackerId = trackerId;
    }
}
