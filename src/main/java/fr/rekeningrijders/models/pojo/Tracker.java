package fr.rekeningrijders.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Tracker
{    
    @Id
    private String trackerId;

    private boolean active;

    public Tracker(String trackerId)
    {
        this.trackerId = trackerId;
        active = true;
    }
}
