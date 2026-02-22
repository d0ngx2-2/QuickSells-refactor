package com.example.quicksells.domain.information.repository;

import com.example.quicksells.domain.information.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long>, InformationCustomRepository {

    boolean existsByTitle(String title);

}
