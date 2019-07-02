package fr.rekeningrijders.rest.dto;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import fr.rekeningrijders.models.pojo.Ride;
import fr.rekeningrijders.service.ReverseGeoService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

@Data
@NoArgsConstructor
public class RideDTO
{
    private long id;
    private String trackerId;
    private PointDTO from;
    private PointDTO to;
    private double totalDistance;
    private short year;
    private Month month;

    private RideDTO(Ride ride, ReverseGeoService rgs)
    {
        id = ride.getId();
        trackerId = ride.getTrackerId();
        from = new PointDTO(ride.getFrom(), rgs);
        to = new PointDTO(ride.getTo(), rgs);
        totalDistance = ride.getTotalDistance();
        year = ride.getYear();
        month = ride.getMonth();
    }

    public static List<RideDTO> transform(List<Ride> rides, ReverseGeoService rgs)
    {
        var result = new ArrayList<RideDTO>(rides.size());
        for (Ride ride : rides) {
            result.add(new RideDTO(ride, rgs));
        }
        return result;
    }
}