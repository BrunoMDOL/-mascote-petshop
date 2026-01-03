package com.mascotepetshop.repository;

import com.mascotepetshop.model.Produto;
import com.mascotepetshop.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByAtivoOrderByNomeAsc(Boolean ativo);

    List<Produto> findByCategoriaAndAtivoOrderByNomeAsc(Categoria categoria, Boolean ativo);

    List<Produto> findByCategoriaIdAndAtivoOrderByNomeAsc(Long categoriaId, Boolean ativo);

    List<Produto> findAllByOrderByNomeAsc();

    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.categoria.ativa = true ORDER BY p.categoria.ordem, p.nome")
    List<Produto> findAllAtivosComCategoriaAtiva();

    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.categoria.id = :categoriaId AND p.categoria.ativa = true ORDER BY p.nome")
    List<Produto> findAtivosByCategoriaId(@Param("categoriaId") Long categoriaId);

    long countByCategoria(Categoria categoria);
}
