package gmfonseca.strider.gertarefas.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import gmfonseca.strider.gertarefas.domain.Tarefa;
import gmfonseca.strider.gertarefas.exceptions.AlreadyFinishedException;
import gmfonseca.strider.gertarefas.exceptions.ImageNotFoundException;
import gmfonseca.strider.gertarefas.exceptions.InvalidFieldsException;
import gmfonseca.strider.gertarefas.exceptions.NotFinishedException;
import javassist.NotFoundException;

public class TarefaController {
	
	private EntityManager entityManager;
	
	public TarefaController(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Listar todas as tarefas cadastradas
	 */
	public List<Tarefa> listAll(){
	
		List<Tarefa> list = entityManager.createQuery("SELECT t FROM Tarefa t", Tarefa.class).getResultList();
		
		Collections.sort(list, new Comparator<Tarefa>() {
	        @Override
	        public int compare(Tarefa t1, Tarefa t2) {
	            return Boolean.compare(!t2.isConcluido(), !t1.isConcluido());
	        }
	    });
		
		return list;
	}
	
	/**
	 * Listar todas as tarefas cadastradas com status de concluido,
	 * filtrado pelo valor recebido
	 * 
	 * @param concluido
	 */
	public List<Tarefa> listAllFinished(boolean concluido){
	
		List<Tarefa> list = entityManager.createQuery("SELECT t FROM Tarefa t WHERE t.concluido = :concluido", Tarefa.class)
				.setParameter("concluido", concluido).getResultList();
		
		return list;
	}

	/**
	 * Recuperar uma tarefa especifica
	 * 
	 * @param tarefaId
	 * 
	 * @throws NotFoundException
	 */
	public Tarefa findOne(int tarefaId) throws NotFoundException {
		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada!");
		
		return tarefa;
	}

	/**
	 * Recuperar resolucao de tarefa especifica
	 * 
	 * @param tarefaId
	 * 
	 * @throws NotFoundException
	 * @throws NotFinishedException
	 * @throws ImageNotFoundException
	 */
	public byte[] getResolucaoTarefa(int tarefaId) throws NotFoundException, NotFinishedException, ImageNotFoundException {
		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada!");	
		if(!tarefa.isConcluido()) throw new NotFoundException("Tarefa não concluída.");	
		
		byte[] bytes = ResolucaoController.downloadImage(tarefa);
		
		if(bytes.length == 0) throw new NotFoundException("Resolução não encontrada");
		
		return bytes;
	}

	/**
	 * Criar uma nova tarefa
	 * 
	 * @param titulo
	 * @param descricao
	 * 
	 * @throws InvalidFieldsException 
	 */
	public Tarefa create(String titulo, String descricao) throws InvalidFieldsException {	
		
		if(StringUtils.isBlank(titulo) || StringUtils.isBlank(descricao)) throw new InvalidFieldsException();

		Tarefa tarefa = new Tarefa(titulo, descricao);
		
		entityManager.getTransaction().begin();
		entityManager.persist(tarefa);
		entityManager.getTransaction().commit();
		
		return tarefa;
	}
	
	/**
	 * Concluir uma tarefa específica e salvar a imagem de resolução
	 * 
	 * @param tarefaId
	 * @param image
	 * 
	 * @throws NotFoundException
	 * @throws AlreadyFinishedException
	 */
	public Tarefa concluir(int tarefaId, MultipartFile image) throws NotFoundException, AlreadyFinishedException {
		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada.");

		if(tarefa.isConcluido()) throw new AlreadyFinishedException();
			
		String imagePath = ResolucaoController.uploadImage(tarefa, image);
		
		if(imagePath.equals("notfound")) throw new NotFoundException("Não foi possível salvar a imagem.");
		
		tarefa.concluir(imagePath);
		
		entityManager.getTransaction().begin();
		entityManager.persist(tarefa);
		entityManager.getTransaction().commit();
		
		return tarefa;
	}
	
	/**
	 * Deletar uma tarefa especifica
	 * 
	 * @param tarefaId
	 * 
	 * @throws NotFoundException
	 */
	public Tarefa delete(int tarefaId) throws NotFoundException {
		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada!");
		
		entityManager.getTransaction().begin();
		entityManager.remove(tarefa);
		entityManager.getTransaction().commit();
		
		return tarefa;
	}
}
