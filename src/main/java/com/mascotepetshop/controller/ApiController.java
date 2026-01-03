package com.mascotepetshop.controller;

import com.mascotepetshop.model.Produto;
import com.mascotepetshop.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ProdutoService produtoService;

    @GetMapping("/produtos/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable Long id) {
        Optional<Produto> produtoOpt = produtoService.buscarPorId(id);

        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", produto.getId());
            response.put("nome", produto.getNome());
            response.put("descricao", produto.getDescricao());
            response.put("preco", produto.getPreco());
            response.put("imagemUrl", produto.getImagemUrl());
            response.put("categoria", produto.getCategoria().getNome());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }
}
