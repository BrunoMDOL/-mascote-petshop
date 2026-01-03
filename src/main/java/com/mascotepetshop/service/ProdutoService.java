package com.mascotepetshop.service;

import com.mascotepetshop.model.Produto;
import com.mascotepetshop.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FileStorageService fileStorageService;

    public List<Produto> listarTodos() {
        return produtoRepository.findAllByOrderByNomeAsc();
    }

    public List<Produto> listarAtivos() {
        return produtoRepository.findAllAtivosComCategoriaAtiva();
    }

    public List<Produto> listarAtivosPorCategoria(Long categoriaId) {
        return produtoRepository.findAtivosByCategoriaId(categoriaId);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        produtoRepository.findById(id).ifPresent(produto -> {
            if (produto.getImagemUrl() != null && !produto.getImagemUrl().isEmpty()) {
                fileStorageService.excluirArquivo(produto.getImagemUrl());
            }
            produtoRepository.delete(produto);
        });
    }

    @Transactional
    public Produto alternarStatus(Long id) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setAtivo(!produto.getAtivo());
                    return produtoRepository.save(produto);
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
}
