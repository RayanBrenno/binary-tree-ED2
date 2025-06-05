package br.com.gomide.grafos;

import java.util.*;

public class Grafo {
    private final Map<String, Set<String>> grafo = new HashMap<>();
    private final Map<String, Integer> grafoPonderado = new HashMap<>();

    public void adicionarVertice(String v) {
        if (!grafo.containsKey(v)) {
            grafo.put(v, new HashSet<>());
        }
    }

    public void adicionarAresta(String v1, String v2) {
        adicionarVertice(v1);
        adicionarVertice(v2);
        grafo.get(v1).add(v2);
        grafo.get(v2).add(v1);
        if(!grafoPonderado.isEmpty()){
            adicionarPeso(v1+"--"+v2, 1);
        }
        System.out.println(grafo);
    }

    public Map<String, Integer> adicionarPeso(String aresta, int peso){

        if(!toDOT().contains(aresta)) return null;

        String dentroDasChaves = toDOT().replaceAll(".*\\{\\s*|\\s*}.*", "");
        String[] arestas = dentroDasChaves.split("\\s+");

        if(grafoPonderado.isEmpty()){
            // inicializando o grafo com peso
            for(String item : arestas){
                int p = 0;
                if(item.contains("--")){
                    p = 1;
                }
                grafoPonderado.put(item, p);
            }
        }else if(!grafoPonderado.containsKey(aresta)){
            grafoPonderado.put(aresta, 1);
        }

        // adiciona o peso à aresta desejada
        if(grafoPonderado.containsKey(aresta)){
            grafoPonderado.replace(aresta, peso);
        }

        return grafoPonderado;
    }

    public Integer retornarPeso(String aresta){
        return grafoPonderado.get(aresta);
    }

    public int contarLacos() {
        int lacos = 0;
        for (String v : grafo.keySet()) {
            if (grafo.get(v).contains(v)) {
                lacos++;
            }
        }
        return lacos;
    }

    public boolean verificarCompleto() {
        int n = grafo.size();
        for (Map.Entry<String, Set<String>> entry : grafo.entrySet()) {
            String v = entry.getKey();
            Set<String> vizinhos = entry.getValue();
            if (vizinhos.size() != n - 1 || vizinhos.contains(v)) {
                return false;
            }
        }
        return true;
    }
    
    public int grau(String v) {
        Set<String> vizinhos = grafo.get(v);
        return vizinhos == null ? 0 : vizinhos.size();
    }
    
    public List<String> caminho(String origem, String destino) {
        if (!grafo.containsKey(origem) || !grafo.containsKey(destino)) {
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

            for (String vizinho : grafo.getOrDefault(ultimo, Collections.emptySet())) {
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

        for (String vertice : grafo.keySet()) {
            dist.put(vertice, Integer.MAX_VALUE);
        }
        dist.put(origem, 0);
        fila.add(new VerticeDistancia(origem, 0));

        while (!fila.isEmpty()) {
            VerticeDistancia atual = fila.poll();
            if (atual.distancia > dist.get(atual.nome)) continue;

            for (String vizinho : grafo.getOrDefault(atual.nome, new HashSet<>())) {
                String aresta1 = atual.nome + "--" + vizinho;
                String aresta2 = vizinho + "--" + atual.nome;
                int peso = grafoPonderado.getOrDefault(aresta1, grafoPonderado.getOrDefault(aresta2, Integer.MAX_VALUE));

                int novaDist = dist.get(atual.nome) + peso;
                if (novaDist < dist.get(vizinho)) {
                    dist.put(vizinho, novaDist);
                    anterior.put(vizinho, atual.nome);
                    fila.add(new VerticeDistancia(vizinho, novaDist));
                }
            }
        }

        // Reconstrução do caminho
        List<String> caminho = new ArrayList<>();
        for (String at = destino; at != null; at = anterior.get(at)) {
            caminho.add(at);
        }
        Collections.reverse(caminho);

        if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
            return null; // sem caminho
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
        StringBuilder sb = new StringBuilder("graph { ");
        Set<String> arestasAdicionadas = new HashSet<>();
    
        for (String v : grafo.keySet()) {
            for (String u : grafo.get(v)) {
                String aresta = v.compareTo(u) < 0 ? v + "--" + u : u + "--" + v;
                if (!arestasAdicionadas.contains(aresta)) {
                    sb.append(aresta).append(" ");
                    arestasAdicionadas.add(aresta);
                }
            }
        }
        for (String v : grafo.keySet()) {
            if (grafo.get(v).isEmpty()) {
                sb.append(v).append(" ");
            }
        }

        sb.append("}");
        return sb.toString();
    }
    public String toDOTPonderado() {
        StringBuilder sb = new StringBuilder("graph { ");

        for(String item : grafoPonderado.keySet()){

            int peso = grafoPonderado.get(item);
            sb.append(item + " [label: "+peso+"] ");

        }

        sb.append("}");
        return sb.toString();
    }

}