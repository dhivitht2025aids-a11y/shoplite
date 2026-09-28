package com.shoplite.service;

import com.shoplite.entity.BillItem;
import com.shoplite.repository.BillItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillItemService {

    private final BillItemRepository billItemRepository;

    public BillItemService(BillItemRepository billItemRepository) {
        this.billItemRepository = billItemRepository;
    }

    // Get all bill items
    public List<BillItem> getAllBillItems() {
        return billItemRepository.findAll();
    }

    // Get bill item by ID
    public BillItem getBillItemById(Long id) {

        return billItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bill Item not found with ID: " + id
                        ));
    }

    // Delete bill item
    public void deleteBillItem(Long id) {

        if (!billItemRepository.existsById(id)) {
            throw new RuntimeException(
                    "Bill Item not found with ID: " + id
            );
        }

        billItemRepository.deleteById(id);
    }
}
