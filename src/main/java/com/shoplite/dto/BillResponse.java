package com.shoplite.dto;

import com.shoplite.entity.Bill;

public class BillResponse {

    private Long id;
    private Double totalAmount;
    private boolean finalized;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.totalAmount = bill.getTotalAmount();
        this.finalized = bill.isFinalized();
    }

    public Long getId() {
        return id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public boolean isFinalized() {
        return finalized;
    }
}