package fr.rekeningrijders.rest.dto;

import fr.rekeningrijders.models.pojo.Owner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OwnerDTO
{
    private long id;
    private String uuid;

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String bsn;
    @NotBlank
    private String address;
    @NotBlank
    private String city;
    @NotBlank
    private String zipcode;

    private OwnerDTO(Owner owner)
    {
        this.id = owner.getId();
        this.firstname = owner.getFirstname();
        this.lastname = owner.getLastname();
        this.bsn = owner.getBsn();
        this.address = owner.getAddress();
        this.city = owner.getCity();
        this.zipcode = owner.getZipcode();
        this.uuid = owner.getUuid();
    }

    public static OwnerDTO transform(Owner owner)
    {
        return new OwnerDTO(owner);
    }

    public static Owner transform(OwnerDTO ownerDTO)
    {
        return new Owner(
            ownerDTO.firstname,
            ownerDTO.lastname,
            ownerDTO.bsn,
            ownerDTO.address,
            ownerDTO.city,
            ownerDTO.zipcode,
            ownerDTO.uuid
        ).setId(ownerDTO.getId());
    }

    public static List<OwnerDTO> transform(List<Owner> owners)
    {
        List<OwnerDTO> dtos = new ArrayList<>();
        for(Owner owner : owners){
            dtos.add(new OwnerDTO(owner));
        }
        return dtos;
    }

}
