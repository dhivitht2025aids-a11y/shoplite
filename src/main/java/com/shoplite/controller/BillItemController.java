package com.shoplite.controller;

import com.shoplite.entity.BillItem;
import com.shoplite.repository.BillItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-items")
public class BillItemController {

    private final BillItemRepository billItemRepository;

    public BillItemController(BillItemRepository billItemRepository) {
        this.billItemRepository = billItemRepository;
    }

    // Get all bill items
    @GetMapping
    public ResponseEntity<List<BillItem>> getAllBillItems() {
        return ResponseEntity.ok(
                billItemRepository.findAll()
        );
    }

    // Get bill item by ID
    @GetMapping("/{id}")
    public ResponseEntity<BillItem> getBillItemById(
            @PathVariable Long id) {

        BillItem billItem = billItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bill Item not found with ID: " + id
                        ));

        return ResponseEntity.ok(billItem);
    }

    // Delete bill item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBillItem(
            @PathVariable Long id) {

        if (!billItemRepository.existsById(id)) {
            throw new RuntimeException(
                    "Bill Item not found with ID: " + id
            );
        }

        billItemRepository.deleteById(id);

        return ResponseEntity.ok(
                "Bill Item deleted successfully"
        );
    }
}