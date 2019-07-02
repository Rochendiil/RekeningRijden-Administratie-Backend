package fr.rekeningrijders.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EuInvoicesDTO
{
    private String countryCode;
    private List<InvoiceDTO> invoices;
}