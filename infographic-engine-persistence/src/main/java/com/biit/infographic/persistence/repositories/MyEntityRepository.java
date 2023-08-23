package com.biit.infographic.persistence.repositories;

import com.biit.infographic.persistence.entities.MyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {

    Optional<MyEntity> findByName(String name);

}
