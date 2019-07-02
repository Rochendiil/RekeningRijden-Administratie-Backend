package fr.rekeningrijders.service;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.collection.SpatialIndexFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import fr.rekeningrijders.models.pojo.Movement;

import javax.ejb.Singleton;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Singleton
public class ReverseGeoService
{
    private static final String PROVINCE_FILE = "provinces/ne_10m_admin_1_states_provinces.shp";
    private static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private static final GeometryFactory gf = new GeometryFactory();
    private SpatialIndexFeatureCollection provinces;

    public ReverseGeoService() throws IOException
    {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        URL countryURL = classLoader.getResource(PROVINCE_FILE);
        if (countryURL == null) {
            countryURL = getClass().getResource('/'+PROVINCE_FILE);
        }
        
        if (countryURL == null) {
            throw new IOException("No province file found");
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("url", countryURL);
        DataStore ds = DataStoreFinder.getDataStore(params);
        if (ds == null) {
            throw new IOException("couldn't open " + params.get("url"));
        }
        Name name = ds.getNames().get(0);
        provinces = new SpatialIndexFeatureCollection(ds.getFeatureSource(name).getFeatures());
    }

    public String getProvinceOf(Movement move)
    {
        Point point = gf.createPoint(new Coordinate(move.getLon(), move.getLat()));
        return iterate(lookup(point));
    }

    private SimpleFeatureCollection lookup(Point point) {
        Filter f = ff.contains(ff.property("the_geom"), ff.literal(point));
        return provinces.subCollection(f);
    }

    private String iterate(SimpleFeatureCollection features) {
        SimpleFeatureIterator itr = features.features();
        try {
            if (itr.hasNext()) {
                SimpleFeature f = itr.next();
                Object o = f.getAttribute("region");
                return translateUTF8(o.toString());
            }
        } finally {
            itr.close();
        }
        return null;
    }

    private String translateUTF8(String untranslated)
    {
        byte[] charset = untranslated.getBytes(StandardCharsets.ISO_8859_1);
        return new String(charset, StandardCharsets.UTF_8);
    }

    // https://stackoverflow.com/a/16794680
    public static double distanceBetween(Movement aa, Movement bb)
    {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(bb.getLat() - aa.getLat());
        double lonDistance = Math.toRadians(bb.getLon() - aa.getLon());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(aa.getLat())) * Math.cos(Math.toRadians(bb.getLat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }
}
