package fr.rekeningrijders.rest.dto;


import fr.rekeningrijders.models.pojo.Rate;
import fr.rekeningrijders.models.pojo.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {
    private long id;
    private String name;
    private double basicRate;
    private List<Long> customRatesId;

    public static ZoneDTO transform(Zone zone){
        List<Long> idList = new ArrayList<>();
        for(Rate rate : zone.getCustomRates()){
            idList.add(rate.getId());
        }
        return new ZoneDTO(zone.getId(),zone.getName(),zone.getBasicRate(), idList);
    }


    public static List<ZoneDTO> transform(List<Zone> zones) {
        List<ZoneDTO> dtos = new ArrayList<>();
        for (Zone zone : zones){
            List<Long> idList = new ArrayList<>();
            for(Rate rate : zone.getCustomRates()){
                idList.add(rate.getId());
            }
            dtos.add(new ZoneDTO(zone.getId(),zone.getName(),zone.getBasicRate(), idList));
        }
        return dtos;
    }
}
