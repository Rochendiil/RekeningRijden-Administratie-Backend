package fr.rekeningrijders.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author luuk
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rate
{

    @Id
    @GeneratedValue
    private long id;
    private double price;

    private LocalTime fromTime;

    private LocalTime untilTime;

    @ManyToOne
    private Zone zone;

    public Rate(long id, double price) {
        this.id = id;
        this.price = price;
    }
    public Rate(double price){
        this.price = price;
    }

    public Rate(double price, LocalTime from, LocalTime until) {
        this.price = price;
        this.fromTime = from;
        this.untilTime = until;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Double.compare(rate.price, price) == 0 &&
                Objects.equals(fromTime, rate.fromTime) &&
                Objects.equals(untilTime, rate.untilTime) &&
                Objects.equals(zone, rate.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, fromTime, untilTime, zone);
    }
}
