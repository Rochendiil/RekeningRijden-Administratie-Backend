package fr.rekeningrijders.system;

import fr.rekeningrijders.models.pojo.Movement;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import java.util.Date;

@Stateless
@NoArgsConstructor
public class Rekenregistratie
{
    private static final String REAL_URL = "http://registratie:8080/api";
    private static final String DEBUG_URL = "http://localhost:8089";
    private static final int PAGESIZE = 1500;

    private static String baseUrl = REAL_URL;
    
    public static void enableDebug()
    {
        baseUrl = DEBUG_URL;
    }

    public Iterable<Movement> getMovementsTrackerGrouped(final Date start, final Date end)
    {
        String url = String.format("%s/movements?start=%s&end=%s&pagesize=%s&page=%s", baseUrl, start.getTime(), end.getTime(), PAGESIZE, "%s");
        return () -> new MovementIterator(url, PAGESIZE);
    }

    public Iterable<Movement> getMovements(String trackerId, Date start, Date end)
    {
        String url = String.format("%s/tracker/%s/movements?start=%s&end=%s&pagesize=%s&page=%s", baseUrl, trackerId, start.getTime(), end.getTime(), PAGESIZE, "%s");
        return () -> new MovementIterator(url, PAGESIZE);
    }
}
