package gmfonseca.strider.gertarefas.api;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gmfonseca.strider.gertarefas.controller.TarefaController;
import gmfonseca.strider.gertarefas.domain.Tarefa;
import gmfonseca.strider.gertarefas.exceptions.InvalidFieldsException;
import javassist.NotFoundException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value="/tarefas")
public class TarefaRestService {
	
	private TarefaController tarefaController;
	private ObjectMapper mapper;
	
	public TarefaRestService(TarefaController tarefaController) {
		this.tarefaController = tarefaController;
		this.mapper = new ObjectMapper();
	}
	
	/**
	 * 
	 */
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Tarefa> all() {
		
		return tarefaController.listAll();
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, params = {"concluido"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Tarefa> allFinished(boolean concluido) {
		
		return tarefaController.listAllFinished(concluido);
	}
	
	/**
	 * 
	 */
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa one(@PathVariable("id") int tarefaId) throws NotFoundException {
		
		return tarefaController.findOne(tarefaId);
	}
	
	/**
	 * 
	 */
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Tarefa create(@RequestBody String json) throws InvalidFieldsException {
		String titulo, descricao;
		
		try {
			
			JsonNode node = mapper.readTree(json);
			
			titulo = node.get("titulo").asText();
		    descricao = node.get("descricao").asText();		
		   
		} catch (Exception e) {
			throw new InvalidFieldsException();
		}
		
		return tarefaController.create(titulo, descricao);
	}
	
	/**
	 * ATUALIZAR DADOS DA TAREFA
	 */
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa update(@PathVariable("id") int tarefaId, @RequestBody String json) throws InvalidFieldsException, NotFoundException {
		
		String titulo, descricao;
		
		try {
			
			JsonNode node = mapper.readTree(json);
			
			titulo = node.get("titulo").asText();
		    descricao = node.get("descricao").asText();		
		   
		} catch (IOException e) {
			throw new InvalidFieldsException();
		}
		
		return tarefaController.update(tarefaId, titulo, descricao);
	}
	
	/**
	 * CONCLUIR TAREFA
	 */
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa concluir(@PathVariable("id") int tarefaId, @RequestBody String json) throws InvalidFieldsException, NotFoundException {
		String imagePath;
		
		try {
			
			JsonNode node = mapper.readTree(json);
			
			imagePath = node.get("imagePath").asText();
		   
		} catch (IOException e) {
			throw new InvalidFieldsException();
		}
		
		return tarefaController.concluir(tarefaId, imagePath);
	}
	
	/**
	 * DELETE TAREFA
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa delete(@PathVariable("id") int tarefaId) throws NotFoundException {
		
		return tarefaController.delete(tarefaId);
	}

}