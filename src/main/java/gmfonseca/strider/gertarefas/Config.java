package gmfonseca.strider.gertarefas;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gmfonseca.strider.gertarefas.api.TarefaRestService;
import gmfonseca.strider.gertarefas.controller.TarefaController;

@Configuration
public class Config {


    public static AnnotationConfigApplicationContext getContext() {
        return new AnnotationConfigApplicationContext(Config.class);
    }

    @Bean
    public EntityManager getEntityManager() {
        return Persistence.createEntityManagerFactory("gerenciador_tarefas").createEntityManager();
    }
    
	@Bean
	public TarefaRestService tarefaRestService() {
		return new TarefaRestService(tarefaController());
	}
	
	@Bean
	public TarefaController tarefaController() {
		return new TarefaController(getEntityManager());
	}
	
}
