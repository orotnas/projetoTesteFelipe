package com.aeon.projetoTesteFelipe.app.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aeon.projetoTesteFelipe.app.dao.LogRequestRepository;
import com.aeon.projetoTesteFelipe.app.model.LogRequest;
import com.aeon.projetoTesteFelipe.app.vo.LogRequestVO;

@Service
public class LogRequestService {

	@Autowired
	private LogRequestRepository repository;
	
	public List<LogRequest> findAllOld(Integer pageNo, Integer pageSize) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("data").ascending());
		
		Page<LogRequest> result = repository.findAll(paging);
		
		if(result.hasContent()) {
            return result.getContent();
        } else {
            return new ArrayList<LogRequest>();
        }
	}
	
	
	public Page<LogRequest> findall(Pageable pageable){
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        
        Page<LogRequest> list = repository.findAll(PageRequest.of(currentPage, pageSize, Sort.by("data").ascending()));
        
        return list;
	}
	
	public Page<LogRequest> buscarLogsByFiltos(Pageable pageable, LogRequestVO filtro){
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        
        List<LogRequest> busca = repository.buscarLogsByFiltro(filtro);
        
        Page<LogRequest> logPage = new PageImpl<LogRequest>(busca, PageRequest.of(currentPage, pageSize, Sort.by("data").ascending()), startItem);
        
        return logPage;
	}
	
	
	public LogRequest findById(Long id) {
		Optional<LogRequest> log = repository.findById(id);
		return log.get();
	}
	
	public LogRequest save(LogRequest logRequest) {
		if(logRequest.getSeq()  == null){
			logRequest = repository.saveAndFlush(logRequest);
             
            return logRequest;
        } else {
            Optional<LogRequest> log = repository.findById(logRequest.getSeq());
             
            if(log.isPresent()){
                LogRequest newLog = log.get();
                newLog.setData(logRequest.getData());
                newLog.setIp(logRequest.getIp());
                newLog.setRequest(logRequest.getRequest());
                newLog.setStatus(logRequest.getStatus());
                newLog.setUserAgent(logRequest.getUserAgent());
 
                newLog = repository.saveAndFlush(newLog);
                 
                return newLog;
            } else {
                logRequest = repository.saveAndFlush(logRequest);
                 
                return logRequest;
            }
        }
	}

	public void delete(Long seqLog) {
		Optional<LogRequest> log = repository.findById(seqLog);
		repository.delete(log.get());	
	}
	
	public void insertArquivoLog(MultipartFile file) {
		List<LogRequest> listaObj = new ArrayList<>();
		 try {
			byte[] txt = file.getBytes();
			InputStream input = new ByteArrayInputStream(txt);
			
			InputStreamReader reader = new InputStreamReader(input);
	        BufferedReader br = new BufferedReader(reader);
	        String linha = br.readLine();
	        while(linha != null) {
	        	LogRequest log = new LogRequest();
	            System.out.println(linha);
	            
	            String[] atributos = linha.split(Pattern.quote("|"));
	            log.setData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(atributos[0]));
	            log.setIp(atributos[1]);
	            log.setRequest(atributos[2]);
	            log.setStatus(atributos[3]);
	            log.setUserAgent(atributos[4]);
	            
	            listaObj.add(log);
	            
	            linha = br.readLine();
	        }
	        repository.saveAll(listaObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}