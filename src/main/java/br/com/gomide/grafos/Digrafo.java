package br.com.gomide.grafos;

import java.sql.SQLOutput;
import java.util.*;

public class Digrafo {

    private final Map<String, Set<String>> digrafo = new HashMap<>();
    private final Map<String, Integer> digrafoPonderado = new HashMap<>();

    public void adicionarVertice(String v) {
        if (!digrafo.containsKey(v)) {
            digrafo.put(v, new HashSet<>());
        }
    }

    public void adicionarAresta(String v1, String v2) {
        adicionarVertice(v1);
        adicionarVertice(v2);
        digrafo.get(v1).add(v2);
        if(!digrafoPonderado.isEmpty()){
            adicionarPeso(v1+"->"+v2, 1);
        }
        System.out.println(digrafo);
    }

    public Map<String, Integer> adicionarPeso(String aresta, int peso){

        if(!toDOT().contains(aresta)) return null;

        String dentroDasChaves = toDOT().replaceAll(".*\\{\\s*|\\s*}.*", "");
        String[] arestas = dentroDasChaves.split("\\s+");

        if(digrafoPonderado.isEmpty()){
            // inicializando o grafo com peso
            for(String item : arestas){
                int p = 0;
                if(item.contains("->")){
                    p = 1;
                }
                digrafoPonderado.put(item, p);
            }
        }else if(!digrafoPonderado.containsKey(aresta)){
            digrafoPonderado.put(aresta, 1);
        }

        // adiciona o peso à aresta desejada
        if(digrafoPonderado.containsKey(aresta)){
            digrafoPonderado.replace(aresta, peso);
        }
        return digrafoPonderado;
    }

    public Integer retornarPeso(String aresta){
        return digrafoPonderado.get(aresta);
    }

    public int contarLacos() {
        int lacos = 0;
        for (String v : digrafo.keySet()) {
            if (digrafo.get(v).contains(v)) {
                lacos++;
            }
        }
        return lacos;
    }

    public boolean verificarCompleto() {
        int n = digrafo.size();
        for (Map.Entry<String, Set<String>> entry : digrafo.entrySet()) {
            String v = entry.getKey();
            Set<String> vizinhos = entry.getValue();
            if (vizinhos.size() != n - 1 || vizinhos.contains(v)) {
                return false;
            }
        }
        return true;
    }

    public int grau(String v) {
        Set<String> vizinhos = digrafo.get(v);
        return vizinhos == null ? 0 : vizinhos.size();
    }

    public List<String> caminho(String origem, String destino) {
        if (!digrafo.containsKey(origem) || !digrafo.containsKey(destino)) {
            return null;
        }

        Queue<List<String>> fila = new LinkedList<>();
        Set<String> visitados = new HashSet<>();

        List<String> inicial = new ArrayList<>();
        inicial.add(origem);
        fila.offer(inicial);
        visitados.add(origem);

        while (!fila.isEmpty()) {
            List<String> caminhoAtual = fila.poll();
            String ultimo = caminhoAtual.get(caminhoAtual.size() - 1);

            if (ultimo.equals(destino)) {
                return caminhoAtual;
            }

            for (String vizinho : digrafo.getOrDefault(ultimo, Collections.emptySet())) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    List<String> novoCaminho = new ArrayList<>(caminhoAtual);
                    novoCaminho.add(vizinho);
                    fila.offer(novoCaminho);
                }
            }
        }

        return null; // Caminho não encontrado
    }

    public List<String> caminhoPonderado(String origem, String destino) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> anterior = new HashMap<>();
        PriorityQueue<VerticeDistancia> fila = new PriorityQueue<>(Comparator.comparingInt(v -> v.distancia));

        // Inicializa todas as distâncias como infinito
        for (String vertice : digrafo.keySet()) {
            dist.put(vertice, Integer.MAX_VALUE);
        }
        dist.put(origem, 0);
        fila.add(new VerticeDistancia(origem, 0));

        while (!fila.isEmpty()) {
            VerticeDistancia atual = fila.poll();

            // Pula se já encontramos um caminho mais curto
            if (atual.distancia > dist.get(atual.nome)) continue;

            for (String vizinho : digrafo.getOrDefault(atual.nome, new HashSet<>())) {
                String aresta = atual.nome + "->" + vizinho;
                Integer peso = digrafoPonderado.get(aresta);

                if (peso == null) continue; // Aresta inexistente

                int novaDist = dist.get(atual.nome) + peso;
                if (novaDist < dist.get(vizinho)) {
                    dist.put(vizinho, novaDist);
                    anterior.put(vizinho, atual.nome);
                    fila.add(new VerticeDistancia(vizinho, novaDist));
                }
            }
        }

        // Reconstrói caminho
        List<String> caminho = new ArrayList<>();
        for (String at = destino; at != null; at = anterior.get(at)) {
            caminho.add(at);
        }
        Collections.reverse(caminho);

        if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
            return null; // Sem caminho
        }

        return caminho;
    }

    static class VerticeDistancia {
        String nome;
        int distancia;

        VerticeDistancia(String nome, int distancia) {
            this.nome = nome;
            this.distancia = distancia;
        }
    }

    public String toDOT() {
        StringBuilder sb = new StringBuilder("digraph { ");
        Set<String> arestasAdicionadas = new HashSet<>();

        for (String v : digrafo.keySet()) {
            for (String u : digrafo.get(v)) {
                String aresta = v + "->" + u;
                if (!arestasAdicionadas.contains(aresta)) {
                    sb.append(aresta).append(" ");
                    arestasAdicionadas.add(aresta);
                }
            }
        }
        for (String v : digrafo.keySet()) {
            if (digrafo.get(v).isEmpty()) {
                sb.append(v).append(" ");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    public String toDOTPonderado() {
        StringBuilder sb = new StringBuilder("digraph { ");

        for(String item : digrafoPonderado.keySet()){

            int peso = digrafoPonderado.get(item);
            sb.append(item + " [label: "+peso+"] ");

        }

        sb.append("}");
        return sb.toString();
    }

}