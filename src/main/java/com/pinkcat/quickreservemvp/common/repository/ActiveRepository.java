package com.pinkcat.quickreservemvp.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ActiveRepository<T, ID> extends JpaRepository<T, ID> {
    public T findByIdAndDeletedAtIsNotNull(ID id);
    public Page<T> findAllByDeletedAtIsNotNull(Pageable pageable);
    public List<T> findAllByDeletedAtIsNull();
}