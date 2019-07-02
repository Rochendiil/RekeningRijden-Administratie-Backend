package fr.rekeningrijders.rest.dto;

import fr.rekeningrijders.models.pojo.Movement;
import fr.rekeningrijders.models.pojo.Point;
import fr.rekeningrijders.service.ReverseGeoService;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointDTO
{
    private static final Date garbage = new Date();

    private double lat;
    private double lon;
    private long timestamp;
    private String province;

    public PointDTO(Point point, ReverseGeoService rgs)
    {
        lat = point.getLat();
        lon = point.getLon();
        timestamp = point.getTimestamp();
        province = rgs.getProvinceOf(new Movement(0L, lat, lon, garbage, null));
    }
}