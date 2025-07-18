package com.pinkcat.quickreservemvp.payment.entity;

import com.pinkcat.quickreservemvp.common.enums.PaymentStatusEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.order.entity.OrderItemEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_pk")
    private Long paymentPk;

    private PaymentStatusEnum status;

    @Comment("유저")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_pk", nullable = false, updatable = false)
    private OrderItemEntity orderItem;
}
