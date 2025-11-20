package br.icev.vendas;

import br.icev.vendas.excecoes.QuantidadeInvalidaException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Carrinho {
    private final Map<String, Produto> produtos = new HashMap<>();
    private final Map<String, Integer> quantidades = new HashMap<>();

    public void adicionar(Produto produto, int quantidade) throws QuantidadeInvalidaException {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException("Quantidade deve ser  maior que zero");
        }

        String codigo = produto.getCodigo();
        produtos.put(codigo, produto);

        int quantidadeAtual = quantidades.getOrDefault(codigo, 0);
        quantidades.put(codigo, quantidadeAtual + quantidade);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (String codigo : quantidades.keySet()) {

            Produto produto = produtos.get(codigo);
            int quantidade = quantidades.get(codigo);
            BigDecimal precoItem = produto.getPrecoUnitario().multiply(new BigDecimal(quantidade));

            subtotal = subtotal.add(precoItem);
        }

        return UtilDinheiro.arredondar2(subtotal);
    }

    public BigDecimal getTotalCom(PoliticaDesconto politica) {
        BigDecimal subtotal = getSubtotal();
        BigDecimal totalComDesconto = politica.aplicar(subtotal);
        
        if (totalComDesconto.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        
        return UtilDinheiro.arredondar2(totalComDesconto);
    }

    public int getTotalItens() {
        int total = 0;
        for (int quantidade : quantidades.values()) {
            total += quantidade;
        }
        return total;
    }
}
