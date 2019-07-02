package fr.rekeningrijders.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import fr.rekeningrijders.TestData;

import java.io.File;
import java.net.URI;

public abstract class BaseArqTest
{
    protected static final String AUTHORIZATION = "Authorization";
    protected static final String TOKEN = "Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhOTQzYTQ2MC1hY2QyLTQxZmItOTMwMC04ZTU0ZDMyOWRjMmMiLCJpc3MiOiJxdWlja3N0YXJ0LWp3dC1pc3N1ZXIiLCJhdWQiOiJqd3QtYXVkaWVuY2UiLCJncm91cHMiOlsiRU1QTE9ZRUUiXSwiZXhwIjoxNTU4NTUwNzczfQ.Jk6abRhem_4RopjDFy9l9vWSECAHrNCDYj_0eVdwxrY45lyJfMxlUN6m3qdJ36fd-70t5qgBWG3-HOUrFuUGCzdmF9CZ8gB45H8d_1aomqMfOpiudN5tPO_VzNQlqoEH9pdA8my-8HcLYZKm6vXmnvgoM84eLT8N3wP6MsQB_Bzby4Gkiw_D8Pz8PImco_JE7TaC8yYIaJiNmMkFJxrrsz_qqC_PgXPJONoaaFei_v0UZGC8hEF4ttK5WQohTL3GPX38mQ_mW7Wxdj7cPGPNG_zraQ-FFD-eGZeotX-4ZUULrcACluDE85Ii9tHebRqE53lQTs2xp_f-MahGSlV5Dw";
    
    @Deployment
    public static Archive<WebArchive> deploy()
    {
        TestData.setupWiremock();
        
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "fr.rekeningrijders")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsResource("provinces", "provinces")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsLibraries(files);
    }

    @ArquillianResource
    protected URI url;
}