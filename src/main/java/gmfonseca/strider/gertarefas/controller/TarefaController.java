package gmfonseca.strider.gertarefas.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import gmfonseca.strider.gertarefas.domain.Tarefa;
import gmfonseca.strider.gertarefas.exceptions.InvalidFieldsException;
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
		
		return entityManager.createQuery("SELECT t FROM Tarefa t", Tarefa.class).getResultList();
	}
	
	/**
	 * Listar todas as tarefas cadastradas com status de concluido
	 * filtrado pelo valor recebido
	 */
	public List<Tarefa> listAllFinished(boolean concluido){
		
		return entityManager.createQuery("SELECT t FROM Tarefa t WHERE t.concluido = :concluido", Tarefa.class)
				.setParameter("concluido", concluido).getResultList();
	}

	/**
	 * Recuperar uma tarefa especifica
	 */
	public Tarefa findOne(int tarefaId) throws NotFoundException {
		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		//TODO: alterar excecao
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada!");
		
		return tarefa;
	}

	/**
	 * Criar uma nova tarefa
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
	 * Deletar uma tarefa especifica
	 */
	public Tarefa concluir(int tarefaId, String imagePath) throws NotFoundException {
		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada!");
		
		// TODO: alterar path
		if(!tarefa.isConcluido()) {
			tarefa.concluir(imagePath);
			
			entityManager.getTransaction().begin();
			entityManager.persist(tarefa);
			entityManager.getTransaction().commit();
		}
		
		return tarefa;
	}
	
	/**
	 * Atualizar uma tarefa especifica
	 */
	public Tarefa update(int tarefaId, String titulo, String descricao) throws NotFoundException {
		
		boolean updated=false;		
		Tarefa tarefa = entityManager.find(Tarefa.class, tarefaId);
		
		if(null == tarefa) throw new NotFoundException("Nenhuma tarefa encontrada!");
		
		if(!StringUtils.isBlank(titulo)) { 
			tarefa.setTitulo(titulo);
			updated=true;
		}
		
		if(!StringUtils.isBlank(descricao)) { 
			tarefa.setTitulo(descricao);
			updated=true;
		}
		
		if(updated) {
			entityManager.getTransaction().begin();
			entityManager.persist(tarefa);
			entityManager.getTransaction().commit();
		}
		
		return tarefa;
	}
	
	/**
	 * Deletar uma tarefa especifica
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
