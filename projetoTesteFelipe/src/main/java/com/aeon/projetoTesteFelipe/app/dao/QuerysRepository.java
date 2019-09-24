package com.aeon.projetoTesteFelipe.app.dao;

import java.util.List;

import com.aeon.projetoTesteFelipe.app.model.LogRequest;
import com.aeon.projetoTesteFelipe.app.vo.LogRequestVO;

public interface QuerysRepository {
	public List<LogRequest> buscarLogsByFiltro(LogRequestVO filtro);
}
