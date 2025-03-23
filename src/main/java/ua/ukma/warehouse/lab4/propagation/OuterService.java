package ua.ukma.warehouse.lab4.propagation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ua.ukma.warehouse.item.Item;
import ua.ukma.warehouse.item.ItemRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OuterService {

    private final InnerService innerService;
    private final ItemRepository itemRepo;

    public OuterService(InnerService innerService, ItemRepository itemRepo) {
        this.innerService = innerService;
        this.itemRepo = itemRepo;
    }

    @Transactional
    public void outerWithInnerRequired_OuterThrows() {
        itemRepo.save(new Item(null, "OuterItem", 1, 1.0, null));
        innerService.innerRequiredThrows();
        throw new RuntimeException("Rollback outer");
    }

    @Transactional
    public void outerWithInnerRequired_InnerThrows() {
        itemRepo.save(new Item(null,"outer", 1, 10.0, null));

        try {
            innerService.innerRequiredThrows();
        } catch (Exception e) {
            // перехопили, але rollback відбудеться
        }
        System.out.println("Is rollback only? " + TransactionAspectSupport.currentTransactionStatus().isRollbackOnly()); // true
    }

    @Transactional
    public void outerWithInnerRequiresNew_OuterThrows() {
        itemRepo.save(new Item(null, "OuterItem", 1, 1.0, null));
        innerService.innerRequiresNew();
        throw new RuntimeException("Rollback outer");
    }

    @Transactional
    public void outerWithInnerRequiresNew_InnerThrows() {
        itemRepo.save(new Item(null, "OuterItem", 1, 1.0, null));
        try {
            innerService.innerRequiresNewThrows();
        } catch (Exception e) {
            // перехопили — outer виконується далі
        }
    }

    @Transactional
    public void outerWithInnerNested_OuterThrows() {
        itemRepo.save(new Item(null, "OuterItem", 1, 1.0, null));
        innerService.innerNested();
        throw new RuntimeException("Rollback outer");
    }

    @Transactional
    public void outerWithInnerNested_InnerThrows() {
        itemRepo.save(new Item(null, "OuterItem", 1, 1.0, null));
        try {
            innerService.innerNestedThrows();
        } catch (Exception e) {
            // кидає помилку → rollback тільки inner
        }
    }
}

