package fr.rekeningrijders.rest.dto;

import fr.rekeningrijders.models.pojo.Invoice;
import fr.rekeningrijders.models.pojo.InvoiceVehicle;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceDTO
{
    private long id;
    private long ownerId;
    private String uuid;
    private short year;
    private Month month;
    private double totalAmount;
    private boolean isPayed;

    private List<VehicleDTO> vehicles;

    private InvoiceDTO(Invoice invoice)
    {
        id = invoice.getId();
        ownerId = invoice.getOwnerId();
        uuid = invoice.getUuid();
        year = invoice.getYear();
        month = invoice.getMonth();
        totalAmount = invoice.getTotalAmount();
        isPayed = invoice.isPayed();

        if (invoice.getVehicles() != null) {
            vehicles = VehicleDTO.transform(invoice.getVehicles()
                .stream()
                .map(InvoiceVehicle::getVehicle)
                .collect(Collectors.toList()));
        }
        else {
            vehicles = new ArrayList<>(0);
        }
    }

    public Invoice asInvoice()
    {
        return new Invoice(uuid, ownerId, year, month, totalAmount, null).setId(id);
    }

    public static InvoiceDTO create(Invoice invoice)
    {
        return invoice != null ? new InvoiceDTO(invoice) : null;
    }

    public static List<InvoiceDTO> create(List<Invoice> invoices)
    {
        List<InvoiceDTO> result = new ArrayList<>(invoices.size());
        for (Invoice invoice : invoices) {
            result.add(new InvoiceDTO(invoice));
        }
        return result;
    }
}