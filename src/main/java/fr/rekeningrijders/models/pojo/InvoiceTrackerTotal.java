package fr.rekeningrijders.models.pojo;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Month;

/**
 *
 * @author piet
 */
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "trackerId", "_year", "_month" }) })
public class InvoiceTrackerTotal implements Serializable
{
    private static final long serialVersionUID = -5181336322266255003L;

    @Id @GeneratedValue
    private long id;

    private String trackerId;
    
    @Column(name = "_year")
    private short year;
    @Column(name = "_month")
    private Month month;
    private double total;

    public InvoiceTrackerTotal(String trackerId, short year, Month month, double total)
    {
        this.trackerId = trackerId;
        this.year = year;
        this.month = month;
        this.total = total;
    }
    
    public String getTrackerId() {
        return trackerId;
    }

    public double getTotal() {
        return total;
    }

    public short getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }
}
