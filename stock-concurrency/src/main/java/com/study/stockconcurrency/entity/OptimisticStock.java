package com.study.stockconcurrency.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptimisticStock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Long quantity;

    @Version
    private Long version;

    public OptimisticStock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decreaseStock(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        this.quantity -= quantity;
    }
}
