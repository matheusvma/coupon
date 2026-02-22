package com.example.coupon.controller;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.dto.CouponResponseDto;
import com.example.coupon.domain.dto.CreateCouponRequest;
import com.example.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Coupons", description = "Endpoints para gerenciamento de cupoms")
public class CouponController {

    private CouponService service;

    @Autowired
    public CouponController(CouponService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar cupom por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Criar cupom")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupom criado"),
            @ApiResponse(responseCode = "400", description = "Payload inválido")
    })
    @PostMapping
    public ResponseEntity<CouponResponseDto> save(@RequestBody CreateCouponRequest couponDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(couponDto));
    }

    @Operation(summary = "Soft delete de cupom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupom deletado"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
            @ApiResponse(responseCode = "406", description = "Cupom já estava deletado")
    })
    @DeleteMapping("/{id}")
    public  ResponseEntity<CouponResponseDto> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(service.delete(id));
    }

}
