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
public class EuMovement {
    private List<EuStep> steps;
    private double price;
}
