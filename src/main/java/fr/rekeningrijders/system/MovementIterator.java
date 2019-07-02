package fr.rekeningrijders.system;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import fr.rekeningrijders.models.pojo.Movement;

class MovementIterator implements Iterator<Movement>
{
    private static final ResteasyClientBuilder resteasyClientBuilder;

    static
    {
        ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
        ResteasyProviderFactory.pushContext(javax.ws.rs.ext.Providers.class, factory);
        resteasyClientBuilder = new ResteasyClientBuilder().providerFactory(factory);
    }

    private Logger logger = Logger.getLogger(Rekenregistratie.class);
    
    public MovementIterator(String url, int pageSize)
    {
        this.url = url;
        this.pageSize = pageSize;
        movements = fetchNextPage();
        nextMovements = movements.length == pageSize
            ? async(this::fetchNextPage)
            : emptyPage();
    }

    private String url;
    private int page = 0;
    private int cursor = 0;
    private int pageSize;
    private Movement[] movements;
    private CompletableFuture<Movement[]> nextMovements;

    public boolean hasNext()
    {
        if (cursor == movements.length && movements.length != 0)
        {    
            movements = await(nextMovements);
            nextMovements = movements.length == pageSize
                ? async(this::fetchNextPage)
                : emptyPage();
            cursor = 0;
        }
        return movements.length > 0;
    }

    @Override
    public Movement next()
    {
        if (cursor == movements.length) {
            throw new NoSuchElementException();
        }
        return movements[cursor++];
    }

    Movement[] fetchNextPage()
    {
        String pageUrl = String.format(url, page);
        page += 1;

        ResteasyClient client = resteasyClientBuilder.build();
        WebTarget target = client.target(pageUrl);
        
        logger.info(pageUrl);
        return target.request()
            .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON+";charset=UTF-8")
            .get()
            .readEntity(Movement[].class);
    }

    CompletableFuture<Movement[]> emptyPage()
    {
        return CompletableFuture.completedFuture(new Movement[0]);
    }

    static CompletableFuture<Movement[]> async(Supplier<Movement[]> runnable)
    {
        return CompletableFuture.supplyAsync(runnable);
    }

    Movement[] await(CompletableFuture<Movement[]> movements)
    {
        try {
            return movements.get();
        }
        catch (ExecutionException e) {
            logger.error("An exception occured when fetching the next page", e);
            return new Movement[0];
        }
        catch (InterruptedException e) {
            logger.error("The CompletableFuture got intterupted... WTF");
            return new Movement[0];
        }
    }
}