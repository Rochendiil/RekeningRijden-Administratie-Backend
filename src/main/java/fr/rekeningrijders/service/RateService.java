package fr.rekeningrijders.service;

import fr.rekeningrijders.doa.context.IRatesStorage;
import fr.rekeningrijders.models.pojo.Rate;
import fr.rekeningrijders.models.pojo.Zone;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.List;

@Stateless
@NoArgsConstructor
public class RateService
{
    @Inject
    private IRatesStorage rateStorage;

    public List<Zone> getZones()
    {
        return rateStorage.readZones();
    }

    public Zone getZone(long id)
    {
        return rateStorage.readZone(id);
    }

    public void setBasicRate(long id, double rate)
    {
        Zone zone = rateStorage.readZone(id);
        zone.setBasicRate(rate);
        rateStorage.updateZone(zone);
    }

    public boolean setCustomRate(long id, Rate rate)
    {
        Zone zone = rateStorage.readZone(id);
        boolean overlaps = zone.getCustomRates()
            .stream()
            .anyMatch(zoneRate -> overlaps(zoneRate, rate));
        if (overlaps) {
            return false;
        }

        zone.addCustomRate(rate);
        rateStorage.updateZone(zone);
        return true;

    }

    public List<Rate> getCustomRates(long id) {
        return rateStorage.getCustomRates(id);
    }

    public Rate getCurrentRate(String zoneName, LocalTime localTime)
    {
        Zone zone = rateStorage.getZonebyName(zoneName);
        if (zone == null) {
            return null;
        }

        for(Rate rateItem: zone.getCustomRates()) {
            if (isInBetween(localTime, rateItem.getFromTime(), rateItem.getUntilTime())) {
                return rateItem;
            }
        }
        return new Rate(zone.getBasicRate());
    }

    static boolean overlaps(Rate a, Rate b)
    {
        return a.getFromTime().compareTo(b.getUntilTime()) <= 0
            && a.getUntilTime().compareTo(b.getFromTime()) >= 0;
    }

    public static boolean isNotEqual(LocalTime target, LocalTime start, LocalTime end)
    {
        return (!target.equals(start) && !target.equals(end));
    }

    public static boolean isInBetween(LocalTime target, LocalTime start, LocalTime end)
    {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
    }

    public void removeCustomRate(long id) {
        Rate rate = rateStorage.readRate(id);
        rateStorage.removeCustomRate(rate);
    }
}
