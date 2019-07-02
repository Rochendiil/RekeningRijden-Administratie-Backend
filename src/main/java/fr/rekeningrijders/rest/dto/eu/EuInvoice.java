package fr.rekeningrijders.rest.dto.eu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EuInvoice {

    private String trackerId;

    private List<EuMovement> movements;

    private double totalPrice;

    private long billedDate;
}
