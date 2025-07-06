package com.pinkcat.quickreservemvp.category.repository;

import com.pinkcat.quickreservemvp.category.entity.QCategoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class CategoryQueryRepository implements CategoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    public CategoryQueryRepository(final JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

     // 자기 자신 하위의 카테고리들을 조회하는 쿼리
    @Override
    public List<Long> findSubCategoryIds(Long categoryId) {
        Set<Long> ids = new HashSet<>();

        QCategoryEntity category = QCategoryEntity.categoryEntity;
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(categoryId);

        while(!stack.isEmpty()){
            Long current = stack.pop();

            List<Long> childIds = queryFactory
                    .select(category.pk)
                    .from(category)
                    .where(category.topCategory.pk.eq(current))
                    .fetch();

            for(Long childId : childIds){
                if(ids.add(childId)){
                    stack.push(childId);
                }
            }
        }
        ids.add(categoryId);

        return new ArrayList<>(ids);
        
    }
}
