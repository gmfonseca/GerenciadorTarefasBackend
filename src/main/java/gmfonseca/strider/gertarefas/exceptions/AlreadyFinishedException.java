package gmfonseca.strider.gertarefas.exceptions;

public class AlreadyFinishedException extends Exception {

	public AlreadyFinishedException() {
		super("Esta tarefa já foi finalizada.");
	}
	
}
