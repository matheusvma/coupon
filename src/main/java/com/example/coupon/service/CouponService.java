package com.example.coupon.service;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.dto.CouponDto;
import com.example.coupon.repository.CouponRepository;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponService {

	private final CouponRepository repository;

	@Autowired
	public CouponService(CouponRepository repository) {
		this.repository = repository;
	}

	public Coupon findById(UUID id) {
		return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon não encontrado"));
	}

    public Coupon save(CouponDto couponDto) {
        return null;
    }

    public Coupon delete(UUID id) {
        Coupon coupon = findById(id);

        if(coupon != null && !coupon.isDeleted()) {
            coupon.softDelete();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Coupon já foi deletado ou não existe");
        }

        return coupon;
    }

}
