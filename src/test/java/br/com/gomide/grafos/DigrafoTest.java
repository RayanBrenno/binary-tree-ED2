package br.com.gomide.grafos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DigrafoTest {

    @Test
    public void testAdicionarVerticeEAresta() {
        Digrafo digrafo = new Digrafo();
        digrafo.adicionarAresta("A", "B");
        assertEquals(1, digrafo.grau("A"));
        assertEquals(0, digrafo.grau("B"));
    }

    @Test
    public void testAdicionarPeso() {
        Digrafo digrafo = new Digrafo();

        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("C", "D");
        digrafo.adicionarAresta("A", "A");

        Map<String, Integer> grafoPonderado;

        grafoPonderado = digrafo.adicionarPeso("A->B", 2);
        System.out.println(grafoPonderado.toString());
        assertEquals(digrafo.retornarPeso("A->B"), 2);

    }

    @Test
    public void testContarLacos() {
        Digrafo digrafo = new Digrafo();
        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("A", "C");
        digrafo.adicionarAresta("B", "C");
        digrafo.adicionarAresta("B", "D");
        digrafo.adicionarAresta("A", "D");
        digrafo.adicionarAresta("D", "C");

        digrafo.adicionarAresta("A", "A"); // laço
        assertEquals(1, digrafo.contarLacos());

        digrafo.adicionarAresta("B", "B"); // laço
        assertEquals(2, digrafo.contarLacos());
    }

    @Test
    public void testverificarCompleto() {
        Digrafo digrafo = new Digrafo();
        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("B", "A");
        digrafo.adicionarAresta("A", "C");
        digrafo.adicionarAresta("C", "A");
        digrafo.adicionarAresta("A", "D");
        digrafo.adicionarAresta("D", "A");

        digrafo.adicionarAresta("B", "C");
        digrafo.adicionarAresta("C", "B");
        digrafo.adicionarAresta("B", "D");
        digrafo.adicionarAresta("D", "B");
        assertFalse(digrafo.verificarCompleto());

        digrafo.adicionarAresta("C", "D");
        digrafo.adicionarAresta("D", "C");
        assertTrue(digrafo.verificarCompleto());

        digrafo.adicionarAresta("A", "A");
        assertFalse(digrafo.verificarCompleto());
    }

    @Test
    public void testGrau() {
        Digrafo digrafo = new Digrafo();
        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("A", "C");
        assertEquals(2, digrafo.grau("A"));
    }

    @Test
    public void testCaminho() {
        Digrafo digrafo = new Digrafo();

        // Criando vértices e arestas
        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("C", "B");
        digrafo.adicionarAresta("A", "E");
        digrafo.adicionarAresta("E", "D");
        digrafo.adicionarAresta("D", "C");

        digrafo.adicionarVertice("Z");

        List<String> caminho = digrafo.caminho("A", "C");
        // O caminho mais curto deve ser: A → E → D → C
        List<String> esperado = Arrays.asList("A", "E", "D", "C");
        assertEquals(esperado, caminho);


        caminho = digrafo.caminho("A", "Z");
        // O caminho não existe, pois Z não está conectado a A, nem ao sistema de grafos
        assertNull(caminho);
    }

    @Test
    public void testCaminhoPonderado(){
        Digrafo digrafo = new Digrafo();

        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("A", "E");
        digrafo.adicionarAresta("E", "D");
        digrafo.adicionarAresta("D", "C");
        digrafo.adicionarAresta("C", "B");
        // ↑ peso 1 por padrão

        digrafo.adicionarPeso("A->B", 5);
        assertNull(digrafo.adicionarPeso("B->A", 5));

        List<String> caminho = digrafo.caminhoPonderado("A", "B");
        // O caminho mais curto deve ser: A → E → D → C → B
        List<String> esperado = Arrays.asList("A", "E", "D", "C", "B");
        assertEquals(esperado, caminho);

        digrafo.adicionarVertice("Z");
        caminho = digrafo.caminhoPonderado("A", "Z");
        assertNull(caminho);

        digrafo.adicionarPeso("D->C", 3);
        caminho = digrafo.caminhoPonderado("A", "B");
        // O caminho mais curto deve ser: A → B
        esperado = Arrays.asList("A", "B");
        assertEquals(esperado, caminho);

        digrafo.adicionarAresta("A", "C");
        caminho = digrafo.caminhoPonderado("A", "B");
        esperado = Arrays.asList("A", "C", "B");
        assertEquals(esperado, caminho);

    }

    @Test
    public void testToDOT() {
        Digrafo digrafo = new Digrafo();
        digrafo.adicionarAresta("A", "B");
        digrafo.adicionarAresta("B", "A");
        digrafo.adicionarAresta("B", "B");
        digrafo.adicionarAresta("A", "C");
        digrafo.adicionarVertice("J");
        digrafo.adicionarAresta("E", "B");
        digrafo.adicionarVertice("D");

        System.out.println(digrafo.toDOT());

        String esperado = "digraph { A->B A->C B->A B->B E->B C D J }";
        String gerado = digrafo.toDOT();

        assertEquals(esperado, gerado);

        // Ponderado

        digrafo.adicionarPeso("A->C", 2);

        esperado = "digraph { B->A [label: 1] B->B [label: 1] C [label: 0] D [label: 0] J [label: 0] E->B [label: 1] A->B [label: 1] A->C [label: 2] }";
        gerado = digrafo.toDOTPonderado();
        assertEquals(esperado, gerado);

    }

}