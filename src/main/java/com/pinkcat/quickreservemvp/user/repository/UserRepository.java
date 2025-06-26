package com.pinkcat.quickreservemvp.user.repository;

import com.pinkcat.quickreservemvp.common.repository.ActiveRepository;
import com.pinkcat.quickreservemvp.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends ActiveRepository<UserEntity, Long> {
  Optional<UserEntity> findById(String userId);
}
