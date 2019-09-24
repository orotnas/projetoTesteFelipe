package com.aeon.projetoTesteFelipe.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aeon.projetoTesteFelipe.app.model.LogRequest;

@Repository
public interface LogRequestRepository extends JpaRepository<LogRequest, Long>,QuerysRepository{
	
}
