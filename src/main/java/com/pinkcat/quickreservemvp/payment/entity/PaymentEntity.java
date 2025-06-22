package com.pinkcat.quickreservemvp.payment.entity;

import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "payment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "payment_pk")
//    private Long paymentPk;

    @Enumerated(EnumType.STRING)
    @Comment("결제상태")
    @Column(name = "payment_status")
    private PaymentStatusEnum status;

    @Comment("총결제금액")
    @Column(name = "payment_total_price")
    private Integer totalPrice;

    @Comment("유저")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_item_pk", nullable = false, updatable = false)
    private OrderItemEntity orderItem;

}
