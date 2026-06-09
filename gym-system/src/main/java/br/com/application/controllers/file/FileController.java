package br.com.application.controllers.file;

import br.com.application.controllers.docs.FileControllerDocs;
import br.com.application.data.dto.UploadFileDTO;
import br.com.application.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService service; // injetando o service de FileStorageService no Controller

    @PostMapping("/uploadFile")
    @Override
    public UploadFileDTO uploadFile(@RequestParam("file") MultipartFile file) { // recebendo o MultipartFile através da Request

        var fileName = service.storageFile(file); // o parâmetro passa pelo método de fazer a gravação em disco do service e assim tratando o nome do arquivo

        /*
        aqui constrói o caminho do arquivo, através do ServletUriComponentsBuilder
        e o fromCurrentContextPath é o BasePath (http://localhost:8080), isso pode mudar dependendo de onde a aplicação está implantada
        */
        // exemplo de como o link vai ficar : http://localhost:8080/api/file/v1/downloadFile/fileName.docx

        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/") // aqui temos o caminho padrão para todos os Endpoints e depois o nome do Endpoint desse método em específico
                .path(fileName) // adiciona no caminho o nome do arquivo
                .toUriString();

        /*
        aqui retorna o nome do arquivo, a URL de download,
        o content type para que o cliente consiga criar o arquivo de novo na hora do download,
        e o tamanho para que ele consiga reconstruir o arquivo e assim fazer o download
        */

        return new UploadFileDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    @Override
    public List<UploadFileDTO> uploadMultiplesFiles(@RequestParam("files") MultipartFile[] files) {
        // retornando um lambda com uma lista de upload de múltiplos arquivos
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(fileName); // lendo o arquivo em disco, a partir do nome, armazenando em uma váriavel com o nome "resource"
        String contentyType = null; // declarando uma váriavel null para conseguir puxa-lá no try-catch

        try {

            contentyType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()); // tentamos determinar o contenty type a partir da váriavel "resource"

        }
        catch (Exception e) {
            logger.error("Code not determine file type!"); // caso não consiga lançamos uma exceção com uma mensagem dizendo que não conseguiu determinar o tipo de arquivo
        }

        if (contentyType == null) { // caso o content type seja null
            contentyType = "application/octet-stream"; // retorna um content type default (genérico)
        }

        return ResponseEntity.ok() // por fim, retorna uma Reponse Entity
                .contentType(MediaType.parseMediaType(contentyType)) // contendo o tipo de contéudo (contenty type), convertido para parseMediaType
                .header(
                        HttpHeaders.CONTENT_DISPOSITION, //dizendo que na Header da Response será mandado um anexo
                        "attachment; filename=\"" + resource.getFilename() + "\"" // e esse anexo está definido aqui
                )
                .body(resource); // no corpo da Response, passamos o arquivo
    }
}