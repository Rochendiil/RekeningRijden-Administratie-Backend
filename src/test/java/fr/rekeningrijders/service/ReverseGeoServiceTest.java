package fr.rekeningrijders.service;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;

import fr.rekeningrijders.models.pojo.Movement;

import java.io.IOException;
import java.util.Date;

public class ReverseGeoServiceTest
{
    @Test
    public void reverseGeoCode() throws IOException, CQLException
    {
        ReverseGeoService service = new ReverseGeoService();
        Movement move1 = new Movement(-1, 48.856613, 2.352222, new Date(), null);
        String result = service.getProvinceOf(move1);
        Assert.assertEquals("Île-de-France", result);
        Movement move2 = new Movement(0, 45.763420, 4.834277, new Date(), null);
        String result2 = service.getProvinceOf(move2);
        Assert.assertEquals("Rhône-Alpes", result2);
    }
}
