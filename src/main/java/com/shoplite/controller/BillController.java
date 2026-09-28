package com.shoplite.controller;

import com.shoplite.dto.BillResponse;
import com.shoplite.entity.Bill;
import com.shoplite.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    // Create a bill
    @PostMapping
    public ResponseEntity<BillResponse> createBill(
            @RequestBody Bill bill) {

        Bill savedBill = billService.createBill(bill);

        return new ResponseEntity<>(
                new BillResponse(savedBill),
                HttpStatus.CREATED
        );
    }

    // Finalize bill
    @PostMapping("/{id}/finalize")
    public ResponseEntity<BillResponse> finalizeBill(
            @PathVariable Long id) {

        Bill finalizedBill = billService.finalizeBill(id);

        return ResponseEntity.ok(
                new BillResponse(finalizedBill)
        );
    }

    // Get bill
    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBill(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                billService.getBillById(id)
        );
    }

    // Get bills for a date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Bill>> getBillsByDate(
            @PathVariable String date) {

        LocalDate localDate = LocalDate.parse(date);

        return ResponseEntity.ok(
                billService.getBillsByDate(localDate)
        );
    }

    // Total sales for a date
    @GetMapping("/sales/{date}")
    public ResponseEntity<Double> getTotalSales(
            @PathVariable String date) {

        LocalDate localDate = LocalDate.parse(date);

        return ResponseEntity.ok(
                billService.getTotalSalesForDate(localDate)
        );
    }
}