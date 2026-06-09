package br.com.application.service;

import br.com.application.config.FileStorageConfig;
import br.com.application.exception.FileNotFoundException;
import br.com.application.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation; // define a váriavel, dizendo onde armazenar o arquivo no sistema de arquivos

    // construtor
    @Autowired // fazendo a injeção de dependências através do construtor
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()) // definindo o caminho do diretório de salvamento do arquivo
                .toAbsolutePath()
                .toAbsolutePath()
                .normalize(); // normalizando o arquivo, removendo caracteres inválidos

        this.fileStorageLocation = path;

        try {
            logger.info("Creating Directories"); // adicionando um log de informação, para mostrar no terminal que os arquivos estão sendo criados
            Files.createDirectories(this.fileStorageLocation); // tentando criar o diretório
        }
        catch (Exception e) {
            logger.error("Could not create the directory where files will be stored!"); // adicionando uma mensagem de erro pelo log
            throw new FileStorageException("Could not create the directory where files will be stored!", e); // lançando exceção caso o diretório não exista
        }
    }

    public String storageFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // limpa o nome do arquivo, remove algum caracter que não pode ser aceito

        try {
            if (fileName.contains("..")) {
                logger.error("Sorry! Filename Contains a Invalid path Sequence " + fileName);
                throw new FileStorageException("Sorry! Filename Contains a Invalid path Sequence " + fileName); // valida o nome do arquivo, se for um nome inválido lança uma exceção
            }

            logger.info("Saving file in Disk");

            Path targetLocation = this.fileStorageLocation.resolve(fileName); // determinar onde o arquivo vai ser salvo e o nome que o arquivo vai ter no diretório

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); // copiando os arquivos para o disco, caso o arquivo já exista vai sobrescrevê-lo
            return fileName; // retorna o nome do arquivo

        } catch (Exception e) {
            logger.error("Could not store file " + fileName + ". Please try again!");
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e); // lançando exceção caso não tenha espaço no HD/SSD para o arquivo
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName) // vai acessar onde o arquivo está salvo no projeto e determinar o nome que o arquivo vai ter na hora que fazer o download
                    .normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) { // se o resource existir
                return resource; // ele retorna o resource
            }
            else  {
                throw new FileNotFoundException("File Not Found " + fileName);
            }
        } catch (Exception e) {
            logger.error("File not found " + fileName); // retorna uma log de erro no terminal com uma mensagem sinalizando que o arquivo não foi encontrado
            throw new FileNotFoundException("File not found " + fileName, e); // lança uma exceção falando que o arquivo não foi encontrado
        }
    }
}