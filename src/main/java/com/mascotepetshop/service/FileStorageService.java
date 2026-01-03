package com.mascotepetshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads", e);
        }
    }

    public String salvarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        try {
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path destino = this.uploadPath.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo", e);
        }
    }

    public void excluirArquivo(String imagemUrl) {
        if (imagemUrl == null || imagemUrl.isEmpty()) {
            return;
        }

        try {
            String nomeArquivo = imagemUrl.replace("/uploads/", "");
            Path arquivo = this.uploadPath.resolve(nomeArquivo);
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            // Log do erro, mas não propaga
            System.err.println("Erro ao excluir arquivo: " + e.getMessage());
        }
    }
}
