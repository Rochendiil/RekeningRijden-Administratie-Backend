package fr.rekeningrijders.rest.dto.eu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

import fr.rekeningrijders.models.pojo.Movement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EuStep
{
    private double x;
    private double y;
    private long time;

    public static List<EuStep> transform(Iterable<Movement> movements)
    {
        var result = new ArrayList<EuStep>();
        movements.forEach(m -> result.add(new EuStep(m.getLon(), m.getLat(), m.getTimestamp())));
        return result;
    }
}
