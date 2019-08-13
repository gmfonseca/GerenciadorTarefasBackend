package gmfonseca.strider.gertarefas.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tarefa")
public class Tarefa implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String titulo;
	private String descricao;
	private String imagePath;
	
	private boolean concluido;
	
	public Tarefa() {
		
	}
	
	public Tarefa(String titulo, String descricao) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.imagePath = "none";
		this.concluido = false;
	}
	
	/**
	 * Metodo para concluir uma tarefa
	 */
	public boolean concluir(String imagePath) {
		boolean concluido = false;
		
		try {
			
			//TODO: tentar encontrar a imagem 
			this.imagePath = imagePath;

			concluido = true;
			this.concluido=true;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return concluido;
	}

	public int getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImagePath() {
		return imagePath;
	}

	public boolean isConcluido() {
		return concluido;
	}
	
}
