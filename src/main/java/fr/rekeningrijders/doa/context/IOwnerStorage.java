package fr.rekeningrijders.doa.context;

import fr.rekeningrijders.models.pojo.Owner;

import java.util.List;

public interface IOwnerStorage {
    Owner read(long id);
    void create(Owner user);
    void update(Owner user);

    List<Owner> getAll();

    List<Owner> findPartialName(String partialName);

    Owner getByBsn (String bsn);

    Owner getByUuid(String uuid);
}
