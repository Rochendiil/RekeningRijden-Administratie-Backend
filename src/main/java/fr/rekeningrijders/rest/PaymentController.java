package fr.rekeningrijders.rest;

import fr.rekeningrijders.service.PaymentService;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Stateless
@Path("/payment")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@NoArgsConstructor
public class PaymentController
{
    @Inject
    private PaymentService paymentService;
    
    @GET
    public void validePayment(
        @QueryParam("orderId") String orderId,
        @QueryParam("payerId") String payerId)
        throws IOException
    {
        if (orderId == null) {
            return;
        }
        paymentService.validatePayment(orderId);
    }
}
