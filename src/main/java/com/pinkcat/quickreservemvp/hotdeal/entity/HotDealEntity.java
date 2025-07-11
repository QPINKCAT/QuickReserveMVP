package com.pinkcat.quickreservemvp.hotdeal.entity;

import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "hot_deal")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "hot_deal_pk"))
public class HotDealEntity extends BaseEntity {
    @Comment("이름")
    @Column(name = "hot_deal_name")
    @Length(max = 255)
    private String name;

    @Comment("설명")
    @Column(name = "hot_deal_description")
    private String description;

    @Comment("썸네일")
    @Column(name = "hot_deal_thumbnail")
    private String thumbnail;

    @Comment("핫딜 시작일")
    @Column(name = "hot_deal_start_at")
    private LocalDateTime startAt;

    @Comment("핫딜 종료일")
    @Column(name = "hot_deal_end_at")
    private LocalDateTime endAt;
}
