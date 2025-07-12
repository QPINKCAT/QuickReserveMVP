package com.pinkcat.quickreservemvp.payment.entity;

import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderEntity;
import jakarta.persistence.*;
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
@AttributeOverride(name = "pk", column = @Column(name = "payment_pk"))
public class PaymentEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Comment("결제상태")
    @Column(name = "payment_status")
    private PaymentStatusEnum status;

    @Comment("총결제금액")
    @Column(name = "payment_total_price")
    private Integer totalPrice;

    @Comment("유저")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_pk", nullable = false, updatable = false)
    private OrderEntity order;

}
