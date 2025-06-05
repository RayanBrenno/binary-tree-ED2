package br.com.gomide.grafos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public class GrafoTest {

    @Test
    public void testAdicionarVerticeEAresta() {
        Grafo grafo = new Grafo();
        grafo.adicionarAresta("A", "B");
        assertEquals(1, grafo.grau("A"));
        assertEquals(1, grafo.grau("B"));
    }

    @Test
    public void testAdicionarPeso() {
        Grafo grafo = new Grafo();

        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("B", "C");
        grafo.adicionarAresta("C", "D");
        grafo.adicionarAresta("A", "A");

        Map<String, Integer> grafoPonderado;

        grafoPonderado = grafo.adicionarPeso("A--B", 2);
        System.out.println(grafoPonderado.toString());
        assertEquals(grafo.retornarPeso("A--B"), 2);

    }

    @Test
    public void testContarLacos() {
        Grafo grafo = new Grafo();
        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("A", "C");
        grafo.adicionarAresta("B", "C");
        grafo.adicionarAresta("B", "D");
        grafo.adicionarAresta("A", "D");
        grafo.adicionarAresta("D", "C");
        grafo.adicionarAresta("A", "A"); // laço
        assertEquals(1, grafo.contarLacos());
        grafo.adicionarAresta("B", "B"); // laço
        assertEquals(2, grafo.contarLacos());
    }

    @Test
    public void testverificarCompleto() {
        Grafo grafo = new Grafo();
        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("A", "C");
        grafo.adicionarAresta("B", "C");
        grafo.adicionarAresta("B", "D");
        assertFalse(grafo.verificarCompleto());

        grafo.adicionarAresta("A", "D");
        grafo.adicionarAresta("D", "C");
        assertTrue(grafo.verificarCompleto());

    }

    @Test
    public void testGrau() {
        Grafo grafo = new Grafo();
        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("A", "C");
        assertEquals(2, grafo.grau("A"));
    }

    @Test
    public void testCaminho() {
        Grafo grafo = new Grafo();

        // Criando vértices e arestas
        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("B", "C");
        grafo.adicionarAresta("C", "D");
        grafo.adicionarAresta("D", "E");
        grafo.adicionarAresta("E", "F");
        grafo.adicionarAresta("F", "G");
        grafo.adicionarAresta("G", "H");
        grafo.adicionarAresta("H", "I");
        grafo.adicionarAresta("I", "J");

        // Atalhos
        grafo.adicionarAresta("A", "H");
        grafo.adicionarAresta("A", "I");
        grafo.adicionarAresta("A", "E");
        grafo.adicionarAresta("E", "J");
        grafo.adicionarVertice("Z");

        List<String> caminho = grafo.caminho("A", "J");
        // O caminho mais curto deve ser: A -> E -> J
        List<String> esperado = Arrays.asList("A", "E", "J");
        assertEquals(esperado, caminho);

        caminho = grafo.caminho("A", "Z");
        // O caminho não existe, pois Z não está conectado a A, nem ao sistema de grafos
        assertNull(caminho);
    }

    @Test
    public void testCaminhoPonderado(){
        Grafo grafo = new Grafo();

        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("B", "C");
        grafo.adicionarAresta("C", "D");
        grafo.adicionarAresta("D", "E");
        grafo.adicionarAresta("A", "E");
        // ↑ peso 1 por padrão

        grafo.adicionarPeso("A--E", 5);
        assertNull(grafo.adicionarPeso("A--D", 2));

        List<String> caminho = grafo.caminhoPonderado("A", "E");
        // O caminho mais curto deve ser: A → B → C → D → E
        List<String> esperado = Arrays.asList("A", "B", "C", "D", "E");
        assertEquals(esperado, caminho);

        grafo.adicionarVertice("Z");
        caminho = grafo.caminhoPonderado("A", "Z");
        assertNull(caminho);

        grafo.adicionarPeso("C--D", 3);
        caminho = grafo.caminhoPonderado("A", "E");
        // O caminho mais curto deve ser: A → E
        esperado = Arrays.asList("A", "E");

        grafo.adicionarAresta("A", "D");
        caminho = grafo.caminhoPonderado("A", "E");
        esperado = Arrays.asList("A", "D", "E");
        assertEquals(esperado, caminho);

    }

    @Test
    public void testToDOT() {
        Grafo grafo = new Grafo();
        grafo.adicionarAresta("A", "B");
        grafo.adicionarAresta("A", "C");
        grafo.adicionarVertice("J");
        grafo.adicionarAresta("E", "B");
        grafo.adicionarVertice("D"); 

        String esperado = "graph { A--B A--C B--E D J }";
        String gerado = grafo.toDOT();

        assertEquals(esperado, gerado);

        // Ponderado

        grafo.adicionarPeso("A--C", 2);

        esperado = "graph { D [label: 0] J [label: 0] A--C [label: 2] B--E [label: 1] A--B [label: 1] }";
        gerado = grafo.toDOTPonderado();
        assertEquals(esperado, gerado);

    }

}