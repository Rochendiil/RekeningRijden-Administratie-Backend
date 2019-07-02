package fr.rekeningrijders.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Ken
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Owner
{
    @Id
    @GeneratedValue
    private long id;
    private String firstname;
    private String lastname;
    private String bsn;
    private String address;
    private String city;
    private String zipcode;
    @Column(unique = true)
    private String uuid;

    private Role role;

    public Owner(String firstname, String lastname, String bsn,String address, String city, String zipcode, String uuid)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.bsn = bsn;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.uuid = uuid;
    }

    public Owner setId(long id)
    {
        this.id = id;
        return this;
    }
}
