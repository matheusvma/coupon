package com.example.coupon.controller;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.dto.CouponDto;
import com.example.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private CouponService service;

    @Autowired
    public CouponController(CouponService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findById(@PathVariable UUID id) {
        Coupon coupon = service.findById(id);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping
    public ResponseEntity<Coupon> save(@RequestBody CouponDto couponDto) {
        Coupon created = service.save(couponDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
