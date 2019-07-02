package fr.rekeningrijders.doa.context;

import fr.rekeningrijders.models.pojo.Tracker;

import java.util.List;

public interface ITrackerStrorage
{
    void create(Tracker tracker);
    Tracker read(String id);
    void update(Tracker tracker);
    List<Tracker> getAll();
    List<Tracker> byOwner(long ownerId);
    List<Tracker> getFrenchCars(int limit);

    byte getFuelTypeByTracker(String trackerId);
}
