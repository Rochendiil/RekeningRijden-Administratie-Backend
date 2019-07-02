package fr.rekeningrijders.models.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Zone {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private double basicRate;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Rate> customRates;


    public Zone(String name, double basicRate) {
        this.name = name;
        this.basicRate = basicRate;
    }
    public void addCustomRate(Rate rate){
        rate.setZone(this);
        customRates.add(rate);

    }



}
