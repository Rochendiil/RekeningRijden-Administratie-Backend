package fr.rekeningrijders.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFixer implements ContainerResponseFilter
{
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext cres) throws IOException
    {
        MultivaluedMap<String, Object> headers = cres.getHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        headers.add("Access-Control-Allow-Headers", "origin, X-Requested-With, accept, Content-Type, X-Codingpedia, Authorization, token");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Max-Age", "1800");
    }
}
