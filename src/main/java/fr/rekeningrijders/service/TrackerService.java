package fr.rekeningrijders.service;

import fr.rekeningrijders.doa.context.ITrackerStrorage;
import fr.rekeningrijders.models.pojo.Tracker;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Stateless
public class TrackerService implements Serializable
{
    private static final long serialVersionUID = -4707315334717782911L;

    @Inject
    private ITrackerStrorage trackerStrorage;

    public Tracker create(Tracker tracker){
        trackerStrorage.create(tracker);
        return tracker;
    }
    public void update(Tracker tracker){
        trackerStrorage.update(tracker);
    }

    public List<Tracker> getAll() {
        return trackerStrorage.getAll();
    }

    public Tracker find(String id) {
        return trackerStrorage.read(id);
    }

    public List<Tracker> getFrenchCars(int limit)
    {
        return trackerStrorage.getFrenchCars(limit);
    }

    public byte getFuelTypeByTracker(String trackerId) {
        return trackerStrorage.getFuelTypeByTracker(trackerId);
    }
}
