package com.mascotepetshop.config;

import com.mascotepetshop.model.Categoria;
import com.mascotepetshop.model.Produto;
import com.mascotepetshop.repository.CategoriaRepository;
import com.mascotepetshop.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) {
        // Só popula se o banco estiver vazio
        if (categoriaRepository.count() > 0) {
            return;
        }

        // Criar categorias
        Categoria racoes = criarCategoria("Rações", 1);
        Categoria acessorios = criarCategoria("Acessórios", 2);
        Categoria higiene = criarCategoria("Higiene", 3);
        Categoria brinquedos = criarCategoria("Brinquedos", 4);
        Categoria medicamentos = criarCategoria("Medicamentos", 5);
        Categoria banhoTosa = criarCategoria("Banho e Tosa", 6);

        // Criar produtos - Rações
        criarProduto("Ração Premium Cães Adultos 15kg",
                     "Ração premium para cães adultos de todas as raças. Rico em proteínas e vitaminas.",
                     new BigDecimal("189.90"), racoes);
        criarProduto("Ração Gatos Castrados 10kg",
                     "Ração especial para gatos castrados. Controle de peso e saúde urinária.",
                     new BigDecimal("159.90"), racoes);
        criarProduto("Ração Filhotes Cães 8kg",
                     "Ração para filhotes com DHA para desenvolvimento cerebral.",
                     new BigDecimal("129.90"), racoes);
        criarProduto("Ração Úmida Sachê Gatos",
                     "Sachê de ração úmida para gatos. Sabor frango. Pacote com 12 unidades.",
                     new BigDecimal("45.90"), racoes);

        // Criar produtos - Acessórios
        criarProduto("Coleira Ajustável Nylon",
                     "Coleira ajustável em nylon resistente. Diversos tamanhos e cores.",
                     new BigDecimal("29.90"), acessorios);
        criarProduto("Comedouro Inox Grande",
                     "Comedouro em aço inox de alta qualidade. Capacidade 1,5L.",
                     new BigDecimal("45.00"), acessorios);
        criarProduto("Cama Pet Luxo M",
                     "Cama confortável para pets de médio porte. Lavável.",
                     new BigDecimal("89.90"), acessorios);
        criarProduto("Guia Retrátil 5m",
                     "Guia retrátil para passeios. Suporta até 20kg.",
                     new BigDecimal("65.00"), acessorios);

        // Criar produtos - Higiene
        criarProduto("Shampoo Neutro 500ml",
                     "Shampoo neutro para cães e gatos. Fórmula suave.",
                     new BigDecimal("32.90"), higiene);
        criarProduto("Condicionador Pet 500ml",
                     "Condicionador que desembaraça e dá brilho aos pelos.",
                     new BigDecimal("35.90"), higiene);
        criarProduto("Tapete Higiênico 30un",
                     "Tapetes higiênicos absorventes para treinamento.",
                     new BigDecimal("49.90"), higiene);
        criarProduto("Escova Removedora de Pelos",
                     "Escova especial que remove pelos soltos e massageia.",
                     new BigDecimal("28.00"), higiene);

        // Criar produtos - Brinquedos
        criarProduto("Bolinha com Apito",
                     "Bolinha de borracha resistente com apito interno.",
                     new BigDecimal("15.90"), brinquedos);
        criarProduto("Mordedor Osso Nylon",
                     "Mordedor em formato de osso. Resistente e durável.",
                     new BigDecimal("24.90"), brinquedos);
        criarProduto("Ratinho com Catnip",
                     "Ratinho de pelúcia com catnip para gatos.",
                     new BigDecimal("19.90"), brinquedos);
        criarProduto("Corda para Puxar",
                     "Corda trançada resistente para brincadeiras interativas.",
                     new BigDecimal("22.00"), brinquedos);

        // Criar produtos - Medicamentos
        criarProduto("Antipulgas Comprimido",
                     "Antipulgas e carrapatos em comprimido. Ação rápida.",
                     new BigDecimal("89.90"), medicamentos);
        criarProduto("Vermífugo Universal",
                     "Vermífugo de amplo espectro para cães e gatos.",
                     new BigDecimal("35.00"), medicamentos);
        criarProduto("Suplemento Vitamínico",
                     "Suplemento completo de vitaminas e minerais.",
                     new BigDecimal("55.90"), medicamentos);
        criarProduto("Spray Cicatrizante",
                     "Spray para tratamento de feridas e cortes superficiais.",
                     new BigDecimal("42.00"), medicamentos);

        // Criar produtos - Banho e Tosa
        criarProduto("Banho Completo Cães Pequenos",
                     "Banho completo com shampoo, condicionador e secagem. Para cães até 10kg.",
                     new BigDecimal("45.00"), banhoTosa);
        criarProduto("Banho Completo Cães Médios",
                     "Banho completo com shampoo, condicionador e secagem. Para cães de 10 a 25kg.",
                     new BigDecimal("60.00"), banhoTosa);
        criarProduto("Banho Completo Cães Grandes",
                     "Banho completo com shampoo, condicionador e secagem. Para cães acima de 25kg.",
                     new BigDecimal("80.00"), banhoTosa);
        criarProduto("Tosa Higiênica",
                     "Tosa higiênica para manter a higiene do seu pet.",
                     new BigDecimal("35.00"), banhoTosa);
        criarProduto("Tosa Completa",
                     "Tosa completa na máquina ou tesoura conforme preferência.",
                     new BigDecimal("70.00"), banhoTosa);

        System.out.println("Dados iniciais carregados com sucesso!");
    }

    private Categoria criarCategoria(String nome, int ordem) {
        Categoria categoria = new Categoria(nome, ordem);
        return categoriaRepository.save(categoria);
    }

    private Produto criarProduto(String nome, String descricao, BigDecimal preco, Categoria categoria) {
        Produto produto = new Produto(nome, descricao, preco, categoria);
        return produtoRepository.save(produto);
    }
}
