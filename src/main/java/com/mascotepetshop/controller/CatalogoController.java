package com.mascotepetshop.controller;

import com.mascotepetshop.model.Categoria;
import com.mascotepetshop.model.Produto;
import com.mascotepetshop.service.CategoriaService;
import com.mascotepetshop.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CatalogoController {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;

    @Value("${petshop.nome}")
    private String petshopNome;

    @Value("${petshop.endereco}")
    private String petshopEndereco;

    @Value("${petshop.whatsapp}")
    private String petshopWhatsapp;

    @Value("${petshop.telefone}")
    private String petshopTelefone;

    @Value("${petshop.email}")
    private String petshopEmail;

    @Value("${petshop.horario.semana}")
    private String horarioSemana;

    @Value("${petshop.horario.sabado}")
    private String horarioSabado;

    @Value("${petshop.horario.domingo}")
    private String horarioDomingo;

    @GetMapping("/")
    public String index(Model model, @RequestParam(required = false) Long categoria) {
        List<Categoria> categorias = categoriaService.listarAtivas();
        List<Produto> produtos;

        if (categoria != null) {
            produtos = produtoService.listarAtivosPorCategoria(categoria);
        } else {
            produtos = produtoService.listarAtivos();
        }

        // Agrupar produtos por categoria
        Map<Categoria, List<Produto>> produtosPorCategoria = new HashMap<>();
        for (Categoria cat : categorias) {
            List<Produto> produtosDaCategoria = produtoService.listarAtivosPorCategoria(cat.getId());
            if (!produtosDaCategoria.isEmpty()) {
                produtosPorCategoria.put(cat, produtosDaCategoria);
            }
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("produtos", produtos);
        model.addAttribute("produtosPorCategoria", produtosPorCategoria);
        model.addAttribute("categoriaAtiva", categoria);

        addPetshopInfo(model);

        return "index";
    }

    @GetMapping("/categoria/{id}")
    public String porCategoria(@PathVariable Long id, Model model) {
        List<Categoria> categorias = categoriaService.listarAtivas();
        List<Produto> produtos = produtoService.listarAtivosPorCategoria(id);

        model.addAttribute("categorias", categorias);
        model.addAttribute("produtos", produtos);
        model.addAttribute("categoriaAtiva", id);

        addPetshopInfo(model);

        return "index";
    }

    private void addPetshopInfo(Model model) {
        model.addAttribute("petshopNome", petshopNome);
        model.addAttribute("petshopEndereco", petshopEndereco);
        model.addAttribute("petshopWhatsapp", petshopWhatsapp);
        model.addAttribute("petshopTelefone", petshopTelefone);
        model.addAttribute("petshopEmail", petshopEmail);
        model.addAttribute("horarioSemana", horarioSemana);
        model.addAttribute("horarioSabado", horarioSabado);
        model.addAttribute("horarioDomingo", horarioDomingo);
    }
}
