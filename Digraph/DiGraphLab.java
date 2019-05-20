/******************************************************************************
* Salvatore Danilo Palumbo - 0642907
* Compilazione:  javac DiGraphLab.java
* Esecuzione:    java DiGraphLab tinyDAG.txt
* Esecuzione con: java DiGraphLab tinyDAG.txt
* Ho messo 2 e 4 come valori prova per testare il numero dei cammini presenti
*
* //Stampa digraph
*  hasHamiltonianPath -> false
* ---------------------------------------
* Source vertices -> 2 8
* ---------------------------------------
* Sink vertices -> 1 4 10 12
* ---------------------------------------
* Esercizio D :
* 2 8 0 3 7 1 5 6 9 4 11 10 12
*
* ----------------------------------------
* Number of paths between 2 and 4 -> [3]
* ----------------------------------------
* No vertex core
* Core vertices -> null
* Core vertices (KS) -> null
* ----------------------------------------
*******************************************************************************/

import java.util.NoSuchElementException;
import java.util.*;
import java.lang.*;


public class DiGraphLab{

/**
* Controlla se il Digraph (sdeve essere aciclico) ha un cammino hamiltoniano
* @param il digraph G
* @return true se esiste un cammino, false altrimenti
*/
public static boolean hasHamiltonianPath(Digraph G) {
    DirectedCycle DAG = new DirectedCycle(G);
    if (DAG.hasCycle()) {
        StdOut.println("Digraph is not a DAG");
        System.exit(1);
    }

    DepthFirstOrder dfo = new DepthFirstOrder(G);
    ArrayList<Integer> topologicalSort = new ArrayList<Integer>();

    for(int w : dfo.reversePost()){
      topologicalSort.add(w);
    }
    int[] topologicalOrder = new int[G.V()];
    int arrayIndex = 0;

    for(int v : topologicalSort) {
        topologicalOrder[arrayIndex++] = v;
    }

    for(int i=0;i<topologicalOrder.length-1;i++) {
        boolean hasEdgeTo = false;

        for(int n : G.adj(topologicalOrder[i])) {
            if (n == topologicalOrder[i + 1]) {
                hasEdgeTo = true;
                break;
            }
        }
        if (!hasEdgeTo) {
            return false;
        }
    }
    return true;
}



/**
* Restituisce come iterable tutti i vertici che non hanno archi entranti
* @param il digraph G
* @return la lista di nodi "source"
*/

  public static Iterable<Integer> sources(Digraph G){
    Queue<Integer> s = new Queue<Integer>();
    for(int v=0;v<G.V;v++){
      if(G.indegree(v) == 0){
        s.enqueue(v);
      }
    }
    return s;
  }


/**
* Restituisce come iterable tutti i vertici che non hanno archi uscenti
* @param il digraph G
* @return la lista di nodi "sinks"
*/

  public static Iterable<Integer> sinks(Digraph G){
    Queue<Integer> s = new Queue<Integer>();
    for(int v=0;v<G.V();v++){
      if(G.outdegree(v) == 0){
        s.enqueue(v);
      }
    }
    return s;
  }


/**
* Restituisce una serie di nodi come Iterable
* @param il digraph G
* @return la lista di nodi richiesta
* L'Esercizio D crea un ordinamento topologico: è differente rispetto quello di "default":
* infatti, prima vengono inseriti in coda i vertici sorgente (che sono in questo caso 2 e 8),
* che hanno comunque solo archi uscenti (indegree = 0): successivamente saranno inseriti anche
* gli altri vertici con indegree = 0. Diciamo che cambia solamente l'ordine di inserimento dei vertici
* nella coda; quindi anche l'output dell'esercizio D è un ordinamento topologico
*
*/

  private static Iterable <Integer> EsercizioD(Digraph G){
      DirectedCycle DAG = new DirectedCycle(G);

      if (DAG.hasCycle()) {
        StdOut.println("Digraph is not a DAG");
        System.exit(1);
      }

      int[] entries = new int[G.V()];
      Queue<Integer> S = new Queue<>();
      int[] indegree = new int[G.V()];
      int[] list = new int [G.V()];
      int listIndex = 0;

      for(int v=0;v<G.V();v++) {
          for(int n: G.adj(v)) {
              entries[n]++;
          }
      }

      for(int v=0;v<G.V();v++) {
          if (entries[v] == 0) {
              S.enqueue(v);
          }
      }

      while (!S.isEmpty()) {
          int p = S.dequeue();

          indegree[p] = listIndex;
          list[listIndex++] = p;

          for(int n : G.adj(p)){
              entries[n]--;
              if (entries[n] == 0) {
                  S.enqueue(n);
              }
          }
      }
      Queue<Integer> l = new Queue<Integer>();
      for(int v : list){
        l.enqueue(v);
      }
      return l;
  }



/**
* Metodo che permette di calcolare il numero dei cammini compresi tra s e t
* @param il digraph G, il vertice "begin" s, il vertice "end" t, array di boolean
* visited, il numero dei cammini contati pathCount
* @return il numero dei cammini contati
*/



public static int pathCounter(Digraph G, int s, int t, boolean visited[]){
    visited[s] = true;
    int pathCount = 0;
    if (s == t){
        return 1;
    }else{
      Bag<Integer> bag = G.adj[s];
      ArrayList<Integer> al  = new ArrayList<Integer>();
      for(int w : bag){
        al.add(w);
      }
      Iterator<Integer> i = al.iterator();
      while (i.hasNext()){
            int n = i.next();
            if (!visited[n]){
                pathCount = pathCounter(G,n,t,visited,pathCount);
            }
        }
    }
    visited[s] = false;
    return pathCount;
}



/**
* Conta i cammini compresi tra s e t
* @param il digraph G, il vertice "begin" s, il vertice "end" t
* @return il numero dei cammini compresi tra s e t in G
*/


public static int countPaths(Digraph G, int s, int t){
    boolean visited[] = new boolean[G.V()];
    int pathCount = 0;
    pathCount = pathCounter(G,s,t,visited,pathCount);
    return pathCount;
}





/*Discutendo con alcuni colleghi, ho notato che molti hanno utilizzato l'alg. di
Kosaraju-Sharir per calcolare le componenti fort.connesse. Ho implementato
entrambi i tipi di metodo. Probabilmente l'approccio che ho usato per il metodo
core non rispetta il "vincolo" del tempo lineare, ma l'ho implementato ugualmente
* core -> non usa KosarajuSharir
* coreNode -> usa KosarajuSharir
*/


/**
* Restituisce i vertici appartenenti al nucleo del digrafo come Iterable
* @param il digraph G
* @return Iterable con i vertici appartenenti al nucleo
*/


public static Iterable<Integer> core (Digraph G){
  boolean visited[] = new boolean[G.V()];
  Queue<Integer> core = new Queue<Integer>();
    for (int i = 0; i < G.V(); i++){
      visited[i] = false;
    }

       // L'ultimo vertice dovrebbe appartenere al nucleo del Digraph
    int v = -1;
      for (int i = 0; i < G.V(); i++){
        if (visited[i] == false){
          dfsUntil(G,i, visited);
            v = i;
        }
      }

       // L'ultimo nodo è il vertice appartenente al nucleo
      for (int i = 0; i < G.V(); i++){
        visited[i] = false;
      }
      core.enqueue(v);
       //Trovato l'ultimo nodo bisogna avere "conferma" che appartenga al nucleo
      dfsUntil(G,v,visited);
      for(int i = 0; i < G.V(); i++){
        if(visited[i] == false){
          StdOut.println("No vertex core");
            return null;
        }
       }
      return core;
   }


public static Iterable<Integer> coreVertices(Digraph G) {
  ArrayList<Integer> s = new ArrayList<Integer>();
  for(int w : sources(G)){
    s.add(w);
  }
  if(s.size()>1)
    return null;


    // Calcolo le comp. connesse
  KosarajuSharirSCC KS = new KosarajuSharirSCC(G);
  int n = KS.count();
  Queue<Integer>[] components = (Queue<Integer>[]) new Queue[n];
  for (int i=0;i<n;i++) {
    components[i] = new Queue<Integer>();
  }
  for (int v=0;v<G.V();v++) {
    components[KS.id(v)].enqueue(v);
  }
  return components[n-1];
}


/**
* Permette di ricercare il potenziale vertice appartenente al nucleo e effettua
* una DFS a partire da quest'ultimo
* @param il digraph G, il vertice del nucleo v, un array visited di boolean
* @return void
*/

public static void dfsUntil(Digraph G, int v,boolean visited[]){
       // Marchia il nodo corrente e lo visita
       visited[v] = true;

       // Ricorsione per gli adiacenti del nodo visitato
       Bag<Integer> bag = G.adj[v];
       ArrayList<Integer> al  = new ArrayList<Integer>();
       for(int w : bag){
         al.add(w);
       }
       Iterator<Integer> i = al.iterator();
       while (i.hasNext())
       {
           int n = i.next();
           if (!visited[n])
               dfsUntil(G,n,visited);
       }
   }




////////////////////////////////////////////////////////////////////////////////
public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    StdOut.println(G);


    StdOut.println("hasHamiltonianPath -> " + hasHamiltonianPath(G));
    StdOut.println("---------------------------------------");
    StdOut.println("Source vertices -> " + sources(G));
    StdOut.println("---------------------------------------");
    StdOut.println("Sink vertices -> " +  sinks(G));
    StdOut.println("---------------------------------------");
    StdOut.println("Esercizio D :");
    for(int v : EsercizioD(G)){
      StdOut.print(v + " ");
    }
    StdOut.println("\n");
    StdOut.println("---------------------------------------");
    StdOut.printf("Number of paths between %d and %d -> [%d]\n" , 2, 4,countPaths(G,2,4));
    StdOut.println("---------------------------------------");
    StdOut.println("Core vertices -> " + core(G));
    StdOut.println("Core vertices (KS) -> " + coreVertices(G));
    StdOut.println("---------------------------------------");
  }
}
