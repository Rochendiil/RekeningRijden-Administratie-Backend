package fr.rekeningrijders.service;

import fr.rekeningrijders.doa.context.IOwnerStorage;
import fr.rekeningrijders.models.pojo.Owner;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class OwnerService
{
    @Inject
    private IOwnerStorage ownerStorage;

    public OwnerService() {
    }

    public OwnerService(IOwnerStorage ownerStorage) {
        this.ownerStorage = ownerStorage;
    }

    public Owner create(Owner owner){
        ownerStorage.create(owner);
        return owner;
    }
    public Owner find(long id){
        return ownerStorage.read(id);
    }

    public void update(Owner owner) {
        ownerStorage.update(owner);
    }

    public List<Owner> getAll(){
        return ownerStorage.getAll();
    }

    public List<Owner> findPartialName(String partialName){
        return ownerStorage.findPartialName(partialName);
    }

    public Owner getByBsn(String bsn){
        return ownerStorage.getByBsn(bsn);
    }

    public Owner findByUuid(String uuid) {
        return  ownerStorage.getByUuid(uuid);
    }
}
