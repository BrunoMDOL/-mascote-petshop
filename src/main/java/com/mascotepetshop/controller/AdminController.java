package com.mascotepetshop.controller;

import com.mascotepetshop.model.Categoria;
import com.mascotepetshop.model.Produto;
import com.mascotepetshop.service.CategoriaService;
import com.mascotepetshop.service.FileStorageService;
import com.mascotepetshop.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final FileStorageService fileStorageService;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    // ==================== LOGIN ====================

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminLogado"))) {
            return "redirect:/admin";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("adminLogado", true);
            return "redirect:/admin";
        }
        redirectAttributes.addFlashAttribute("erro", "Usuário ou senha inválidos");
        return "redirect:/admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // ==================== DASHBOARD ====================

    @GetMapping("")
    public String dashboard(Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/dashboard";
    }

    // ==================== PRODUTOS ====================

    @GetMapping("/produtos")
    public String listarProdutos(Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("produtos", produtoService.listarTodos());
        return "admin/produtos";
    }

    @GetMapping("/produtos/novo")
    public String novoProdutoForm(Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/produto-form";
    }

    @GetMapping("/produtos/editar/{id}")
    public String editarProdutoForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        produtoService.buscarPorId(id).ifPresent(produto -> {
            model.addAttribute("produto", produto);
        });
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/produto-form";
    }

    @PostMapping("/produtos/salvar")
    public String salvarProduto(@RequestParam String nome,
                                 @RequestParam(required = false) String descricao,
                                 @RequestParam BigDecimal preco,
                                 @RequestParam Long categoriaId,
                                 @RequestParam(required = false) Long id,
                                 @RequestParam(required = false) MultipartFile imagem,
                                 @RequestParam(required = false, defaultValue = "true") Boolean ativo,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            Produto produto;
            if (id != null) {
                produto = produtoService.buscarPorId(id)
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            } else {
                produto = new Produto();
            }

            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setAtivo(ativo);

            categoriaService.buscarPorId(categoriaId).ifPresent(produto::setCategoria);

            if (imagem != null && !imagem.isEmpty()) {
                // Excluir imagem antiga se existir
                if (produto.getImagemUrl() != null) {
                    fileStorageService.excluirArquivo(produto.getImagemUrl());
                }
                String imagemUrl = fileStorageService.salvarArquivo(imagem);
                produto.setImagemUrl(imagemUrl);
            }

            produtoService.salvar(produto);
            redirectAttributes.addFlashAttribute("sucesso", "Produto salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar produto: " + e.getMessage());
        }

        return "redirect:/admin/produtos";
    }

    @PostMapping("/produtos/excluir/{id}")
    public String excluirProduto(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            produtoService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Produto excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir produto: " + e.getMessage());
        }

        return "redirect:/admin/produtos";
    }

    @PostMapping("/produtos/alternar-status/{id}")
    public String alternarStatusProduto(@PathVariable Long id,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            Produto produto = produtoService.alternarStatus(id);
            String status = produto.getAtivo() ? "ativado" : "desativado";
            redirectAttributes.addFlashAttribute("sucesso", "Produto " + status + " com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao alterar status: " + e.getMessage());
        }

        return "redirect:/admin/produtos";
    }

    // ==================== CATEGORIAS ====================

    @GetMapping("/categorias")
    public String listarCategorias(Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/categorias";
    }

    @GetMapping("/categorias/nova")
    public String novaCategoriaForm(Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("categoria", new Categoria());
        return "admin/categoria-form";
    }

    @GetMapping("/categorias/editar/{id}")
    public String editarCategoriaForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }
        categoriaService.buscarPorId(id).ifPresent(categoria -> {
            model.addAttribute("categoria", categoria);
        });
        return "admin/categoria-form";
    }

    @PostMapping("/categorias/salvar")
    public String salvarCategoria(@RequestParam String nome,
                                   @RequestParam(required = false, defaultValue = "0") Integer ordem,
                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = false, defaultValue = "true") Boolean ativa,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            Categoria categoria;
            if (id != null) {
                categoria = categoriaService.buscarPorId(id)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            } else {
                categoria = new Categoria();
            }

            categoria.setNome(nome);
            categoria.setOrdem(ordem);
            categoria.setAtiva(ativa);

            categoriaService.salvar(categoria);
            redirectAttributes.addFlashAttribute("sucesso", "Categoria salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar categoria: " + e.getMessage());
        }

        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            categoriaService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Categoria excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir categoria: " + e.getMessage());
        }

        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/alternar-status/{id}")
    public String alternarStatusCategoria(@PathVariable Long id,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes) {
        if (!isLogado(session)) {
            return "redirect:/admin/login";
        }

        try {
            Categoria categoria = categoriaService.alternarStatus(id);
            String status = categoria.getAtiva() ? "ativada" : "desativada";
            redirectAttributes.addFlashAttribute("sucesso", "Categoria " + status + " com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao alterar status: " + e.getMessage());
        }

        return "redirect:/admin/categorias";
    }

    private boolean isLogado(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("adminLogado"));
    }
}
