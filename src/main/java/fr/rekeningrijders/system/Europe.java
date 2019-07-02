package fr.rekeningrijders.system;

import javax.ejb.Stateless;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import fr.rekeningrijders.rest.dto.eu.EuInvoice;
import lombok.var;

@Stateless
public class Europe
{    
    private Logger logger = Logger.getLogger(Europe.class);

    public void sendInvoice(EuInvoice invoice)
    {
        String url;
        String countyCode = invoice.getTrackerId().substring(0, 2);
        switch(countyCode)
        {
        case "NL": url = "http://192.168.24.198:8083/governmentBackend/api/europe/invoice"; break;
        case "BE": url = "http://192.168.24.160:8080/api/eu/invoices"; break;
        case "DE": url = "http://192.168.24.172:8010/european_union/invoices/"; break;
        default:
            logger.errorv("Unkown country code '{0}' in invoice", countyCode);
            return;
        }
        try {
			putInvoice(invoice, url);
		} catch (Exception e) {
            logger.error(e);
		}
    }

    protected void putInvoice(EuInvoice invoice, String url)
    {
        logger.infov("Sending foreign invoice to {0}", url);
        var client = new ResteasyClientBuilder().build();
        int response = client.target(url)
            .request()
            .post(Entity.entity(invoice, MediaType.APPLICATION_JSON))
            .getStatus();
        
        logger.infov("Response {0}", response);
        if (response < 200 || response >= 300) {
            logger.infov("Country didn't process invoice {0}", invoice.getTrackerId());
        }
    }
}