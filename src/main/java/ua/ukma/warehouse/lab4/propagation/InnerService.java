package ua.ukma.warehouse.lab4.propagation;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;

@Service
public class InnerService {

    private final ItemRepository itemRepo;

    public InnerService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void innerRequiredThrows() {
        itemRepo.save(new Item(null,"InnerItem", 1, 10.0, null));
        throw new RuntimeException("Fail in REQUIRED");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerRequiresNew() {
        itemRepo.save(new Item(null, "InnerItem", 1, 1.0, null));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerRequiresNewThrows() {
        itemRepo.save(new Item(null, "InnerItem", 1, 1.0, null));
        throw new RuntimeException("Fail in REQUIRES_NEW");
    }

    @Transactional(propagation = Propagation.NESTED)
    public void innerNested() {
        itemRepo.save(new Item(null, "InnerItem", 1, 10.0, null));
    }

    @Transactional(propagation = Propagation.NESTED)
    public void innerNestedThrows() {
        itemRepo.save(new Item(null, "InnerItem", 1, 10.0, null));
        throw new RuntimeException("Fail in NESTED");
    }
}

