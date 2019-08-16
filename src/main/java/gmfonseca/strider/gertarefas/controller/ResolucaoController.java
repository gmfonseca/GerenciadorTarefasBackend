package gmfonseca.strider.gertarefas.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import gmfonseca.strider.gertarefas.domain.Tarefa;

public class ResolucaoController {

	/**
	 * Armazenar uma imagem recebida
	 */
	protected static String uploadImage(Tarefa tarefa, MultipartFile image) {
		String path = "resolucoes";
		String fileName = tarefa.getId() + ".jpg";
		
		try {
			path = saveImage(path, fileName, image).getPath();
		}catch (Exception e) {
			path="notfound";
			e.printStackTrace();
		}
		
		return path;
	}
	
	/**
	 * Retornar imagem lida, caso exista, em bytes
	 */
	protected static byte[] downloadImage(Tarefa tarefa) {
		
		File imageFile = new File(tarefa.getImagePath());		
		byte[] bytes = new byte[0];
		
		if(imageFile != null) {
			try {
				bytes = FileUtils.readFileToByteArray(imageFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return bytes;
	}
	
	/**
	 * Salvar imagem em arquivo local
	 */
	private static File saveImage(String dir, String filename, MultipartFile image) {
		String rootPath = "C:/temp";
		
		Path dirPath = Paths.get(rootPath, dir);
		Path filePath = dirPath.resolve(filename);
		
	 	File file = new File(filePath.toUri());
		
		try {
		 	FileUtils.writeByteArrayToFile(file, image.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return file;
	}

}
