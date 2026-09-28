package com.shoplite.repository;

import com.shoplite.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByBillDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}