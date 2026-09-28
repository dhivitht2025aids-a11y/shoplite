package com.shoplite.service;

import com.shoplite.entity.Bill;
import com.shoplite.entity.BillItem;
import com.shoplite.entity.Product;
import com.shoplite.repository.BillRepository;
import com.shoplite.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ProductRepository productRepository;

    public BillService(
            BillRepository billRepository,
            ProductRepository productRepository) {

        this.billRepository = billRepository;
        this.productRepository = productRepository;
    }

    // Create bill without reducing stock
    @Transactional
    public Bill createBill(Bill bill) {

        double total = 0;

        for (BillItem item : bill.getItems()) {

            Product product = productRepository
                    .findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found with ID: "
                                            + item.getProduct().getId()
                            ));

            if (item.getQuantity() <= 0) {
                throw new RuntimeException(
                        "Quantity must be greater than 0"
                );
            }

            item.setBill(bill);
            item.setProduct(product);
            item.setPrice(product.getPrice());

            double subtotal =
                    product.getPrice() * item.getQuantity();

            item.setSubtotal(subtotal);

            total += subtotal;
        }

        bill.setTotalAmount(total);
        bill.setFinalized(false);

        return billRepository.save(bill);
    }

    // Finalize bill and reduce stock
    @Transactional
    public Bill finalizeBill(Long billId) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bill not found with ID: " + billId
                        ));

        if (bill.isFinalized()) {
            throw new RuntimeException(
                    "Bill is already finalized"
            );
        }

        // Check ALL stock before reducing anything
        for (BillItem item : bill.getItems()) {

            Product product = item.getProduct();

            if (item.getQuantity() > product.getStockQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName()
                                + ". Available: "
                                + product.getStockQuantity()
                                + ", Requested: "
                                + item.getQuantity()
                );
            }
        }

        // Reduce stock only after every item passes validation
        for (BillItem item : bill.getItems()) {

            Product product = item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            - item.getQuantity()
            );

            productRepository.save(product);
        }

        bill.setFinalized(true);

        return billRepository.save(bill);
    }

    public Bill getBillById(Long id) {

        return billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bill not found with ID: " + id
                        ));
    }

    public List<Bill> getBillsByDate(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return billRepository.findByBillDateBetween(
                start,
                end
        );
    }

    public Double getTotalSalesForDate(LocalDate date) {

        return getBillsByDate(date)
                .stream()
                .filter(Bill::isFinalized)
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }
}