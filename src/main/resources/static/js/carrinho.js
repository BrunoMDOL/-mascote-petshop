// Mascote Pet Shop - Carrinho de Compras (JavaScript Vanilla)

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    console.log('Carrinho.js carregado com sucesso!');
    atualizarCarrinhoUI();
});

// Obter carrinho do localStorage
function getCarrinho() {
    const carrinho = localStorage.getItem('mascote_carrinho');
    return carrinho ? JSON.parse(carrinho) : [];
}

// Salvar carrinho no localStorage
function saveCarrinho(carrinho) {
    localStorage.setItem('mascote_carrinho', JSON.stringify(carrinho));
}

// Adicionar produto ao carrinho
function adicionarAoCarrinho(button) {
    console.log('Adicionando ao carrinho:', button);
    const id = button.dataset.id;
    const nome = button.dataset.nome;
    const preco = parseFloat(button.dataset.preco);
    const imagem = button.dataset.imagem || '';
    console.log('Produto:', { id, nome, preco, imagem });

    const carrinho = getCarrinho();
    const itemExistente = carrinho.find(item => item.id === id);

    if (itemExistente) {
        itemExistente.quantidade++;
    } else {
        carrinho.push({
            id: id,
            nome: nome,
            preco: preco,
            imagem: imagem,
            quantidade: 1
        });
    }

    saveCarrinho(carrinho);
    atualizarCarrinhoUI();
    mostrarNotificacao('Produto adicionado ao carrinho!');

    // Animação no botão do carrinho
    const btnCarrinho = document.getElementById('btn-carrinho');
    btnCarrinho.classList.add('cart-animate');
    setTimeout(() => btnCarrinho.classList.remove('cart-animate'), 300);
}

// Alterar quantidade
function alterarQuantidade(id, delta) {
    const carrinho = getCarrinho();
    const item = carrinho.find(item => item.id === id);

    if (item) {
        item.quantidade += delta;
        if (item.quantidade <= 0) {
            removerItem(id);
            return;
        }
        saveCarrinho(carrinho);
        atualizarCarrinhoUI();
    }
}

// Remover item
function removerItem(id) {
    let carrinho = getCarrinho();
    carrinho = carrinho.filter(item => item.id !== id);
    saveCarrinho(carrinho);
    atualizarCarrinhoUI();
}

// Limpar carrinho
function limparCarrinho() {
    localStorage.removeItem('mascote_carrinho');
    atualizarCarrinhoUI();
}

// Atualizar interface do carrinho
function atualizarCarrinhoUI() {
    const carrinho = getCarrinho();
    const countElement = document.getElementById('carrinho-count');
    const itensContainer = document.getElementById('carrinho-itens');
    const totalElement = document.getElementById('carrinho-total');
    const btnFinalizar = document.getElementById('btn-finalizar');

    // Atualizar contador
    const totalItens = carrinho.reduce((sum, item) => sum + item.quantidade, 0);
    countElement.textContent = totalItens;
    countElement.style.display = totalItens > 0 ? 'inline' : 'none';

    // Atualizar lista de itens
    if (carrinho.length === 0) {
        itensContainer.innerHTML = '<p class="text-center text-muted py-4">Seu carrinho está vazio</p>';
        btnFinalizar.disabled = true;
    } else {
        let html = '';
        carrinho.forEach(item => {
            const subtotal = item.preco * item.quantidade;
            html += `
                <div class="carrinho-item">
                    ${item.imagem ?
                        `<img src="${item.imagem}" alt="${item.nome}" class="carrinho-item-img">` :
                        `<div class="carrinho-item-img-placeholder"><i class="bi bi-image"></i></div>`
                    }
                    <div class="carrinho-item-info">
                        <div class="carrinho-item-nome">${item.nome}</div>
                        <div class="carrinho-item-preco">R$ ${formatarPreco(item.preco)}</div>
                        <div class="carrinho-item-subtotal">Subtotal: R$ ${formatarPreco(subtotal)}</div>
                    </div>
                    <div>
                        <div class="carrinho-qtd-controls">
                            <button class="btn btn-outline-secondary btn-sm" onclick="alterarQuantidade('${item.id}', -1)">
                                <i class="bi bi-dash"></i>
                            </button>
                            <span>${item.quantidade}</span>
                            <button class="btn btn-outline-secondary btn-sm" onclick="alterarQuantidade('${item.id}', 1)">
                                <i class="bi bi-plus"></i>
                            </button>
                        </div>
                        <button class="btn btn-link text-danger btn-sm p-0 mt-1" onclick="removerItem('${item.id}')">
                            <i class="bi bi-trash"></i> Remover
                        </button>
                    </div>
                </div>
            `;
        });
        itensContainer.innerHTML = html;
        btnFinalizar.disabled = false;
    }

    // Atualizar total
    const total = carrinho.reduce((sum, item) => sum + (item.preco * item.quantidade), 0);
    totalElement.textContent = 'R$ ' + formatarPreco(total);
}

// Formatar preço
function formatarPreco(valor) {
    return valor.toFixed(2).replace('.', ',');
}

// Toggle carrinho sidebar
function toggleCarrinho() {
    const sidebar = document.getElementById('carrinho-sidebar');
    const overlay = document.getElementById('carrinho-overlay');

    sidebar.classList.toggle('open');
    overlay.classList.toggle('open');

    // Prevenir scroll do body quando carrinho está aberto
    document.body.style.overflow = sidebar.classList.contains('open') ? 'hidden' : '';
}

// Finalizar pedido via WhatsApp
function finalizarPedido() {
    const carrinho = getCarrinho();
    if (carrinho.length === 0) {
        alert('Seu carrinho está vazio!');
        return;
    }

    const clienteNome = document.getElementById('cliente-nome').value.trim();
    if (!clienteNome) {
        alert('Por favor, informe seu nome.');
        document.getElementById('cliente-nome').focus();
        return;
    }

    const observacoes = document.getElementById('pedido-obs').value.trim() || 'Nenhuma';

    // Montar mensagem
    let mensagem = `*Novo Pedido - ${PETSHOP_NOME}*\n\n`;
    mensagem += `*Cliente:* ${clienteNome}\n\n`;
    mensagem += `*Itens:*\n`;

    let total = 0;
    carrinho.forEach(item => {
        const subtotal = item.preco * item.quantidade;
        total += subtotal;
        mensagem += `${item.quantidade}x ${item.nome} - R$ ${formatarPreco(subtotal)}\n`;
    });

    mensagem += `\n*Total: R$ ${formatarPreco(total)}*\n\n`;
    mensagem += `*Observações:* ${observacoes}`;

    // Codificar mensagem para URL
    const mensagemCodificada = encodeURIComponent(mensagem);
    const whatsappUrl = `https://wa.me/${WHATSAPP_NUMBER}?text=${mensagemCodificada}`;

    // Abrir WhatsApp
    window.open(whatsappUrl, '_blank');

    // Limpar carrinho após envio
    limparCarrinho();
    toggleCarrinho();
    mostrarNotificacao('Pedido enviado com sucesso!');
}

// Mostrar notificação toast
function mostrarNotificacao(mensagem) {
    // Remover toast existente
    const toastExistente = document.querySelector('.toast-container');
    if (toastExistente) {
        toastExistente.remove();
    }

    // Criar novo toast
    const toastHtml = `
        <div class="toast-container">
            <div class="toast-notification show">
                <i class="bi bi-check-circle me-2"></i>
                ${mensagem}
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', toastHtml);

    // Remover após 3 segundos
    setTimeout(() => {
        const toast = document.querySelector('.toast-container');
        if (toast) {
            toast.remove();
        }
    }, 3000);
}

// Fechar carrinho com tecla ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        const sidebar = document.getElementById('carrinho-sidebar');
        if (sidebar && sidebar.classList.contains('open')) {
            toggleCarrinho();
        }
    }
});
