package fr.rekeningrijders.rest.dto;

import fr.rekeningrijders.models.pojo.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateDTO {

    private long id;
    private double price;
    private int fromHour;
    private int fromMinute;
    private int untilHour;
    private int untilMinute;
    private long zoneId;


    public static RateDTO transform(Rate rate){
        return new RateDTO(rate.getId(),
                rate.getPrice(),
                rate.getFromTime().getHour(),
                rate.getFromTime().getMinute(),
                rate.getUntilTime().getHour(),
                rate.getUntilTime().getMinute(),
                rate.getZone().getId());
    }

    public static Rate transform(RateDTO rateDTO) {
        return new Rate(
                rateDTO.price,
                LocalTime.of(rateDTO.fromHour,rateDTO.getFromMinute(), 00),
                LocalTime.of(rateDTO.getUntilHour(), rateDTO.getUntilMinute(), 00));
    }

    public static List<RateDTO> transform(List<Rate> customRates) {
        List<RateDTO> dtos = new ArrayList<>();
        for(Rate rate : customRates){
            dtos.add(new RateDTO(rate.getId(),
                    rate.getPrice(),
                    rate.getFromTime().getHour(),
                    rate.getFromTime().getMinute(),
                    rate.getUntilTime().getHour(),
                    rate.getUntilTime().getMinute(),
                    rate.getZone().getId()));
        }
        return dtos;
    }
}
