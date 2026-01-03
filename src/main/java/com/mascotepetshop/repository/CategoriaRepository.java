package com.mascotepetshop.repository;

import com.mascotepetshop.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByAtivaOrderByOrdemAsc(Boolean ativa);

    List<Categoria> findAllByOrderByOrdemAsc();

    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);
}
