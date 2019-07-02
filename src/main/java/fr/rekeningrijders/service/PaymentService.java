package fr.rekeningrijders.service;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import com.paypal.orders.PurchaseUnit;
import fr.rekeningrijders.doa.context.IInvoiceStorage;
import fr.rekeningrijders.models.pojo.Invoice;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PaymentService
{
    private Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Inject
    private IInvoiceStorage invoiceStorage;

    private PayPalHttpClient paypal;

    public PaymentService()
    {
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
            "AZI2hQhrznQuxUT4w3s-vafUqpcOsd1Zp4r0CUFwHQMRYDutoDPQbpBC5lYTtXtL6hMjL47H7gMa3RtZ",
            "EH2uOEOijIvq-iidfUhmksaUBsgX8RvQbfnoRMJ8pD_J9tcfCWFjqjt2Tup2vNpO8tT7DqbTQ2Lq7rlq");
        paypal = new PayPalHttpClient(environment);
    }


    public void validatePayment(String paypalOrderId) throws IOException
    {
        OrdersGetRequest getOrder = new OrdersGetRequest(paypalOrderId);
        Order order = paypal.execute(getOrder).result();

        if (order == null) {
            throw new IOException();
        }

        if (order.purchaseUnits().size() != 1) {
            throw new IOException();
        }

        PurchaseUnit unit = order.purchaseUnits().get(0);
        String invoiceUuid = unit.customId();
        Invoice invoice = invoiceStorage.getByUuid(invoiceUuid);

        if (invoice == null) {
            logger.log(Level.SEVERE, "Invoice payed but not found in database: {0}", invoiceUuid);
            throw new IOException();
        }

        invoice.markPayed();
        invoiceStorage.update(invoice);
    }
}
