package fr.rekeningrijders.models.pojo;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Point
{
    private double lat;
    private double lon;
    private long timestamp;
}