package com.example.coupon.service;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.dto.CouponResponseDto;
import com.example.coupon.domain.dto.CreateCouponRequest;
import com.example.coupon.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class CouponService {

	private final CouponRepository repository;

	@Autowired
	public CouponService(CouponRepository repository) {
		this.repository = repository;
	}

	public CouponResponseDto findById(UUID id) {
		Coupon coupon = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon não encontrado"));

        return CouponResponseDto.toResponseDto(coupon);
	}

    public CouponResponseDto save(CreateCouponRequest dto) {
        try {
            OffsetDateTime now = OffsetDateTime.now();

            Coupon coupon = Coupon.create(
                    dto.getCode(),
                    dto.getDescription(),
                    dto.getDiscountValue(),
                    dto.getExpirationDate(),
                    dto.getPublished(),
                    now
            );

            Coupon saved = repository.save(coupon);
            return CouponResponseDto.toResponseDto(saved);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public CouponResponseDto delete(UUID id) {
        Coupon coupon = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon não encontrado"));

        if (coupon.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Coupon já foi deletado");
        }

        coupon.softDelete(OffsetDateTime.now());

        Coupon saved = repository.save(coupon);
        return CouponResponseDto.toResponseDto(saved);
    }

}
