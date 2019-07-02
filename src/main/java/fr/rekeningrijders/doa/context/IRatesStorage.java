package fr.rekeningrijders.doa.context;

import fr.rekeningrijders.models.pojo.Rate;
import fr.rekeningrijders.models.pojo.Zone;

import java.util.List;

public interface IRatesStorage {

    List<Zone> readZones();
    void writeZone(Zone zone);

    Zone readZone(long id);
    Rate readRate(long id);
    void updateZone(Zone zone);

    List<Rate> getCustomRates(long id);

    void writeCustomRate(Rate rate);

    Zone getZonebyName(String zoneName);

    void removeCustomRate(Rate rate);
}
