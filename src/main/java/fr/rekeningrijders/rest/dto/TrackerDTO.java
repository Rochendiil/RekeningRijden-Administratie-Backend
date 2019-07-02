package fr.rekeningrijders.rest.dto;

import fr.rekeningrijders.models.pojo.Tracker;
import fr.rekeningrijders.rest.validators.TrackerValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrackerDTO
{
    @TrackerValid(allowNull = false)
    private String trackerId;
    private boolean isActive;

    public TrackerDTO(Tracker tracker)
    {
        trackerId = tracker.getTrackerId();
        isActive = tracker.isActive();
    }

    public static Tracker transform(TrackerDTO tracker)
    {
        return new Tracker(tracker.getTrackerId());
    }

    public static List<TrackerDTO> transform(List<Tracker> trackers)
    {
        List<TrackerDTO> dtos = new ArrayList<>();
        for(Tracker tracker : trackers) {
            dtos.add(new TrackerDTO(tracker));
        }
        return dtos;
    }
}
