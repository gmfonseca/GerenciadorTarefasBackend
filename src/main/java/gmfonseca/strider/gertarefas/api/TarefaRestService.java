package gmfonseca.strider.gertarefas.api;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gmfonseca.strider.gertarefas.controller.TarefaController;
import gmfonseca.strider.gertarefas.domain.Tarefa;
import gmfonseca.strider.gertarefas.exceptions.AlreadyFinishedException;
import gmfonseca.strider.gertarefas.exceptions.ImageNotFoundException;
import gmfonseca.strider.gertarefas.exceptions.InvalidFieldsException;
import gmfonseca.strider.gertarefas.exceptions.NotFinishedException;
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
	 * RECUPERAR TODAS AS TAREFAS
	 */
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Tarefa> all() {
		
		return tarefaController.listAll();
	}
	
	/**
	 * RECUPERAR TODAS AS TAREFAS COM FILTRO (Y/N) CONCLUIDO
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, params = {"concluido"})
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Tarefa> allFinished(boolean concluido) {
		
		return tarefaController.listAllFinished(concluido);
	}
	
	/**
	 * RECUPERAR TAREFA ESPECIFICA
	 * 
	 * @throws NotFoundException 
	 */
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa one(@PathVariable("id") int tarefaId) throws NotFoundException {
		
		return tarefaController.findOne(tarefaId);
	}
	
	/**
	 * RECUPERAR IMAGEM DE RESOLUCAO
	 *  
	 * @throws NotFoundException 
	 * @throws NotFinishedException 
	 * @throws ImageNotFoundException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/{id}/resolucao", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<byte[]> getResolucaoTarefa(@PathVariable("id") int tarefaId) throws NotFoundException, NotFinishedException, ImageNotFoundException, IOException {
		byte[] bytes = tarefaController.getResolucaoTarefa(tarefaId);
				
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
	}
	
	/**
	 * CRIAR TAREFA
	 * 
	 * @throws InvalidFieldsException 
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
	 * CONCLUIR TAREFA
	 * 
	 * @throws NotFoundException 
	 * @throws AlreadyFinishedException 
	 */
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa concluir(@PathVariable("id") int tarefaId, @RequestParam MultipartFile image) throws AlreadyFinishedException, NotFoundException {

		return tarefaController.concluir(tarefaId, image);
	}
	
	/**
	 * DELETAR TAREFA
	 * 
	 * @throws NotFoundException 
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Tarefa delete(@PathVariable("id") int tarefaId) throws NotFoundException {
		
		return tarefaController.delete(tarefaId);
	}

}