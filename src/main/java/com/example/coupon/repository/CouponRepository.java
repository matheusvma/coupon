package com.example.coupon.repository;

import com.example.coupon.domain.Coupon;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CouponRepository extends CrudRepository<Coupon, UUID> {

}
