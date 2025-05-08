package com.salva.script_runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salva.script_runner.model.ExecutionDetailEntity;



@Repository
public interface ExecutionDetailRepository extends JpaRepository<ExecutionDetailEntity, Long> {

    
}
