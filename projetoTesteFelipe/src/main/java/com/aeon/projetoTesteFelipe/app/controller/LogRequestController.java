package com.aeon.projetoTesteFelipe.app.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.aeon.projetoTesteFelipe.app.model.LogRequest;
import com.aeon.projetoTesteFelipe.app.service.LogRequestService;
import com.aeon.projetoTesteFelipe.app.vo.LogRequestVO;

@RestController
//@RequestMapping(value = "/prevent")
public class LogRequestController {

	@Autowired
	private LogRequestService service;
	
	//listagem do log de arquivos
	@RequestMapping(value = "/prevent", method = RequestMethod.GET)
    public ModelAndView listLogs(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
 
        Page<LogRequest> logPage = service.findall(PageRequest.of(currentPage - 1, pageSize));
 
        model.addAttribute("logs", logPage);
 
        int totalPages = logPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("filtro", new LogRequestVO());
        return new ModelAndView("listLogsRequest");
	}
	
	
	//pesquisar log
	@RequestMapping(path = "/buscar", method = RequestMethod.POST)
    public ModelAndView pesquisar(Model model, @Valid @ModelAttribute("filtro") LogRequestVO vo, 
    		@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        
        Page<LogRequest> list = service.buscarLogsByFiltos(PageRequest.of(currentPage - 1, pageSize), vo);
        
        model.addAttribute("logs", list);
        int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
		model.addAttribute("filtro", new LogRequestVO());
        return new ModelAndView("listLogsRequest");
    }
	
	
	@RequestMapping(path = "/limpar")
    public ModelAndView limpar(){
		return new ModelAndView("redirect:/prevent");
    }
	
	@RequestMapping(path = {"/edit", "/edit/{id}"})
    public ModelAndView editLogById(Model model, @PathVariable("id") Optional<Long> id){
        if (id.isPresent()) {
        	LogRequest entity = service.findById(id.get());
            model.addAttribute("log", entity);
        } else {
            model.addAttribute("log", new LogRequest());
        }
        return new ModelAndView("addEditLogRequest");
    }
	
	@RequestMapping(path = "/delete/{id}")
    public ModelAndView deleteEmployeeById(@PathVariable("id") Long id){
        service.delete(id);
        return new ModelAndView("redirect:/prevent");
    }
	
	@RequestMapping(path = "/createLog", method = RequestMethod.POST)
    public ModelAndView createOrUpdateEmployee(@Valid LogRequest logRequest){
		LocalDate dataTela = LocalDateTime.ofInstant(logRequest.getData().toInstant(), ZoneId.systemDefault()).toLocalDate();
		LocalDate dataSistema = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalDate();
		if(dataTela.equals(dataSistema)) {
			logRequest.setData(new Date());
		}
        service.save(logRequest);
        return new ModelAndView("redirect:/prevent");
    }
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ModelAndView fileUpload(@PathVariable("file") MultipartFile file) {
		 if (!file.isEmpty()) {
			 System.out.println("Arquivo Carregado");
			 service.insertArquivoLog(file);
		 }
		return new ModelAndView("redirect:/prevent");
	}
	
}
