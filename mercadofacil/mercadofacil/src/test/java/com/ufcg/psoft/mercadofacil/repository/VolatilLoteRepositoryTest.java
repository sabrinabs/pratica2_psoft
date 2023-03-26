package com.ufcg.psoft.mercadofacil.repository;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VolatilLoteRepositoryTest {

    @Autowired
    VolatilLoteRepository driver = new VolatilLoteRepository();

    Lote lote;
    Lote resultado;
    Produto produto;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .id(1L)
                .nome("Produto Base")
                .codigoBarra("123456789")
                .fabricante("Fabricante Base")
                .preco(125.36)
                .build();

        lote = Lote.builder()
                .id(1L)
                .numeroDeItens(100)
                .produto(produto)
                .build();
    }

    @AfterEach
    void tearDown() {
        produto = null;
        driver.deleteAll();
    }

    @Test
    @DisplayName("Adicionar o primeiro Lote no repositorio de dados")
    void salvarPrimeiroLote() {
        resultado = driver.save(lote);

        assertEquals(driver.findAll().size(), 1);
        assertEquals(resultado.getId().longValue(), lote.getId().longValue());
        assertEquals(resultado.getProduto(), produto);
    }

    @Test
    @DisplayName("Adicionar o segundo Lote (ou posterior) no repositorio de dados")
    void salvarSegundoLoteOuPosterior() {
        Produto produtoExtra = Produto.builder()
                .id(2L)
                .nome("Produto Extra")
                .codigoBarra("987654321")
                .fabricante("Fabricante Extra")
                .preco(125.36)
                .build();
        Lote loteExtra = Lote.builder()
                .id(2L)
                .numeroDeItens(100)
                .produto(produtoExtra)
                .build();
        driver.save(lote);

        resultado = driver.save(loteExtra);

        assertEquals(driver.findAll().size(), 2);
        System.out.println(resultado.getId());
        System.out.println(loteExtra.getId());
        assertEquals(resultado.getId().longValue(), loteExtra.getId().longValue());
        assertEquals(resultado.getProduto(), produtoExtra);

        // motivo do erro: o metodo save retorna sempre o primeiro lote adicionado e não
        // o último a ser adicionado

    }

    @Test
    @DisplayName("Adicionar o segundo Lote no repositorio de dados usando o método update e receber uma lista de tamanho 1")
    void updateTest() {
        Produto produtoExtra = Produto.builder()
                .id(2L)
                .nome("Produto Extra")
                .codigoBarra("987654321")
                .fabricante("Fabricante Extra")
                .preco(125.36)
                .build();
        Lote loteExtra = Lote.builder()
                .id(2L)
                .numeroDeItens(100)
                .produto(produtoExtra)
                .build();
        driver.save(lote);

        resultado = driver.update(loteExtra);

        assertEquals(driver.findAll().size(), 1);
        assertEquals(resultado.getId().longValue(), loteExtra.getId().longValue());
        assertEquals(resultado.getProduto(), produtoExtra);
    }

    @Test
    @DisplayName("Adicionar um Lote no repositorio de dados e recuperá-lo através do seu ID")
    void findTest() {
        driver.save(lote);

        System.out.println(driver.findAll());

        assertEquals(driver.findAll().size(), 1);
        resultado = driver.find(1L);
        // motivo do erro: o metodo find na verdade retorna o lote que ta no valor de
        // index passado e n o lote que tem o id passado

        assertEquals(resultado.getId().longValue(), lote.getId().longValue());
        assertEquals(resultado.getProduto(), produto);
    }

    @Test
    @DisplayName("Adicionar dois Lotes no repositorio de dados e recuperá-los")
    void findAllTest() {
        Produto produtoExtra = Produto.builder()
                .id(2L)
                .nome("Produto Extra")
                .codigoBarra("987654321")
                .fabricante("Fabricante Extra")
                .preco(125.36)
                .build();
        Lote loteExtra = Lote.builder()
                .id(2L)
                .numeroDeItens(100)
                .produto(produtoExtra)
                .build();
        driver.save(lote);
        resultado = driver.save(loteExtra);

        List<Lote> saidaEsperada = new ArrayList<>();
        saidaEsperada.add(lote);
        saidaEsperada.add(loteExtra);

        assertEquals(saidaEsperada, driver.findAll());

    }

    @Test
    @DisplayName("Adicionar o segundo Lote no repositorio de dados e depois deletá-lo")
    void deleteTest() {
        Produto produtoExtra = Produto.builder()
                .id(2L)
                .nome("Produto Extra")
                .codigoBarra("987654321")
                .fabricante("Fabricante Extra")
                .preco(125.36)
                .build();
        Lote loteExtra = Lote.builder()
                .id(2L)
                .numeroDeItens(100)
                .produto(produtoExtra)
                .build();
        driver.save(lote);

        resultado = driver.save(loteExtra);
        driver.delete(loteExtra);

        assertEquals(driver.findAll().size(), 1);
        // motivo do erro: o metodo delete na verdade deleta todos os Lotes do
        // repositório e não apenas o lote passado
    }

    @Test
    @DisplayName("Adicionar o segundo Lote no repositorio de dados e depois deletar todos os lotes")
    void deleteAllTest() {
        Produto produtoExtra = Produto.builder()
                .id(2L)
                .nome("Produto Extra")
                .codigoBarra("987654321")
                .fabricante("Fabricante Extra")
                .preco(125.36)
                .build();
        Lote loteExtra = Lote.builder()
                .id(2L)
                .numeroDeItens(100)
                .produto(produtoExtra)
                .build();
        driver.save(lote);

        resultado = driver.save(loteExtra);
        driver.deleteAll();

        assertEquals(driver.findAll().size(), 0);
    }

}
