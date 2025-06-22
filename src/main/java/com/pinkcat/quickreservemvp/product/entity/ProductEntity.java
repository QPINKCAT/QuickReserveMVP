package com.pinkcat.quickreservemvp.product.entity;

import com.pinkcat.quickreservemvp.common.enums.ProductStatusEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "product")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long productPk;

    @Comment("상품명")
    @Column(name = "product_name", length = 30, unique = true, nullable = false)
    private String productName;

    @Comment("상품 설명")
    @Column(name = "product_description", length = 10000, nullable = false)
    private String productDescription;

    @Comment("상품 가격")
    @Column(name = "product_price")
    private Integer price;

    @Comment("재고 수량")
    @Column(name = "product_stock")
    private Integer stock;

    @Comment("평균 평점")
    @Column(name = "product_avg_rating")
    private Float avgRating;

    @Comment("상품 리뷰 개수")
    @Column(name = "product_review_count")
    private Integer reviewCnt;

    @Comment("상품 상태")
    @Column(name = "product_status")
    private ProductStatusEnum productStatus;

    @Comment("상품 판매 시작일")
    @Column(name = "sale_start_at")
    private Timestamp saleStartAt;

    @Comment("상품 판매 종료일")
    @Column(name = "sale_end_at")
    private Timestamp saleEndAt;

}