package com.mascotepetshop.service;

import com.mascotepetshop.model.Categoria;
import com.mascotepetshop.repository.CategoriaRepository;
import com.mascotepetshop.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAllByOrderByOrdemAsc();
    }

    public List<Categoria> listarAtivas() {
        return categoriaRepository.findByAtivaOrderByOrdemAsc(true);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.findByNome(nome);
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        categoriaRepository.findById(id).ifPresent(categoria -> {
            long produtosCount = produtoRepository.countByCategoria(categoria);
            if (produtosCount > 0) {
                throw new RuntimeException("Não é possível excluir categoria com produtos vinculados");
            }
            categoriaRepository.delete(categoria);
        });
    }

    @Transactional
    public Categoria alternarStatus(Long id) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setAtiva(!categoria.getAtiva());
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public boolean existePorNome(String nome) {
        return categoriaRepository.existsByNome(nome);
    }
}
