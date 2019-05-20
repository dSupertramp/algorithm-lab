/******************************************************************************
* Salvatore Danilo Palumbo - 0642907
* Compilazione: javac GraphProperties.java
* Esecuzione: java GraphProperties filecongrafo.txt
*
* tinyG.txt --> Graph not connected
* mediumgG.txt --> Girth = 3
*
*
* Classe che trova alcune caratteristiche del grafo,
* come eccentricità di un vertice, diametro, raggio, centro e circonferenza
* di un grafo connesso. Se si immette un grafo non connesso, allora verrà
* lanciata un'eccezione
* Ho calcolato anche gli altri metodi (tranne il girth) usando il DFS
*
*
*
*
******************************************************************************/


/*
* La classe GraphProperties contiene un array chiamato exc dove saranno
* memorizzate le diverse eccentricità dei nodi: di default, è inizializzato
* con i nodi del grafo: tutti questi primi metodi utilizzano il BFS
*/

import java.lang.Math;

public  class GraphProperties{
    private int[] exc;
    private int[] excDFS;
    private static final int INFINITY = Integer.MAX_VALUE;
    private int girth = INFINITY;


    public GraphProperties(Graph G){
      exc = new int[(G.V())];
        for (int i = 0; i < G.V(); i++)
            exc[i] = eccentricity(G,i);
    }

    /**
     * Calcola la lunghezza del più corto cammino dal vertice s al
	 * vertice più lontano da v
	 * @param grafo G, vertice s
	 * @return l'eccentricità del vertice
    */
    public int eccentricity(Graph G, int s){
        BreadthFirstPaths bfs = new BreadthFirstPaths(G,s);
        int exc=0;
        for (int v=0;v<G.V();v++){
            if(bfs.hasPathTo(v)){
                if(bfs.distTo(v)>exc)
					exc=bfs.distTo(v);
            }
        }
        return exc;
    }



    /**
     * La massima tra le eccentricità dei vertici del grafo
	 * @return diametro del grafo
    */
    public int diameter(){
        int dia=exc[0];
        for (int i=1;i<exc.length;i++)
            if(exc[i]>dia)
				dia=exc[i];
        return dia;
    }

    /**
     * La minima tra le eccentricità dei vertici del grafo
	 * @return raggio del grafo
     */
    public int radius(){
        int rad=exc[0];
        for (int i=1;i<exc.length;i++)
            if(exc[i]<rad)
				rad=exc[i];
        return rad;
    }

    /**
     * Il vertice la cui eccentricità è il raggio
	 * @return centro del grafo
     */
    public int center(){
        int rad=radius();
        for (int i=0;i<exc.length;i++){
			if (rad==exc[i]){
				return i;
			}
		}
		return 0;
    }

////////////////////////////////////////////////////////////////////////////////


/*
* Metodi precedenti implementati utilizzando DFS
*/



/*
* Secondo costruttore per le operazioni tramite DFS
*/

    public GraphProperties(Graph G, boolean k){
        excDFS = new int[(G.V())];
        for (int i = 0; i < G.V(); i++)
            excDFS[i] = eccentricityDFS(G,i);
    }



/**
 * Calcola la lunghezza del più corto cammino dal vertice s al
* vertice più lontano da v
* @param grafo G, vertice s
* @return l'eccentricità del vertice
*/
  public int eccentricityDFS(Graph G, int s){
      DepthFirstPaths dfs = new DepthFirstPaths(G,s);
      int exc=0;
      for (int v=0;v<G.V();v++){
          if(dfs.hasPathTo(v)){
              if(dfs.distTo(v)>exc)
              exc=dfs.distTo(v);
            }
          }
          return exc;
        }


/**
 * La massima tra le eccentricità dei vertici del grafo
* @return diametro del grafo
*/
  public int diameterDFS(){
      int dia=excDFS[0];
      for (int i=1;i<excDFS.length;i++)
          if(excDFS[i]>dia)
          dia=excDFS[i];
          return dia;
        }

/**
 * La minima tra le eccentricità dei vertici del grafo
* @return raggio del grafo
 */
 public int radiusDFS(){
      int rad=excDFS[0];
      for (int i=1;i<excDFS.length;i++)
          if(excDFS[i]<rad)
          rad=excDFS[i];
          return rad;
        }

/**
 * Il vertice la cui eccentricità è il raggio
* @return centro del grafo
 */
 public int centerDFS(){
      int rad=radiusDFS();
      for (int i=0;i<excDFS.length;i++){
        if (rad==excDFS[i]){
          return i;
        }
      }
      return 0;
    }

////////////////////////////////////////////////////////////////////////////////
/**
* Bfs modificato per trovare il ciclo più corto
* @param il grafo G, sorgente s
* @return shortestCycle, il ciclo più corto del grafo
*/


/*
* edgeTo è un metodo che ho aggiunto alla classe BreadthFirstPaths simile a distTo:
* restituisce l'arco "precedente" ad un vertice
*/


  private int bfsToGetShortestCycle(Graph G, int s) {
    int shortestCycle = INFINITY;
    int[] distTo = new int[G.V()];
    int[] edgeTo = new int[G.V()];
    distTo[s] = 0;

    BreadthFirstPaths bfs = new BreadthFirstPaths(G,s);

    //Calcola i valori di distTo ed edgeTo
    for(int w=0;w<G.V();w++) {
        if (w == s) {
            continue;
        }
        distTo[w] = bfs.distTo(w);
        edgeTo[w] = bfs.edgeTo(w);
    }

    Queue<Integer> queue = new Queue<>();
    boolean[] visited = new boolean[G.V()];
    visited[s] = true;
    queue.enqueue(s);
    while (!queue.isEmpty()) {
        int current = queue.dequeue();


/*
* neighbor è usato per gli adiacenti: nel ciclo, vengono visitati tutti gli adiacenti:
* edgeTo[current] rappresenta l'ultimo arco necessario per arrivare nuovamente allo stesso
* vertice (origine del ciclo) : infatti se si prova ad inserire un grafo con un solo vertice,
* girth = INFINITY, dato che l'arco per "tornare indietro" al vertice origine del ciclo è lo stesso
*/

        for(int neighbor : G.adj(current)) {
            if (!visited[neighbor]){
                visited[neighbor] = true;
                queue.enqueue(neighbor);
            }else if (neighbor!=edgeTo[current]) {
                //Ciclo trovato
                int cycleLength = distTo[current] + distTo[neighbor] + 1;
                shortestCycle = Math.min(shortestCycle, cycleLength);
            }
        }
    }
    return shortestCycle;
}



/**
* Calcola il ciclo minimo del grafo: return INFINITY se il grafo è aciclico
* @param il grafo G
* @return void
*/

  private void computeGirth(Graph G) {
    for(int v= 0; v<G.V(); v++) {
      int shortestCycle = bfsToGetShortestCycle(G,v);
      girth = Math.min(girth, shortestCycle);
      }
    }



/**
* Restituisce il girth del grafo
* @param il grafo G
* @return il girth del grafo
*/


  public int girth(Graph G) {
   computeGirth(G);
    return girth;
  }



////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
		GraphProperties GP = null;
    GraphProperties GPDFS = null;
		Graph G = null;
		CC cc = null;

			G = new Graph(new In(args[0]));
			GP = new GraphProperties(G);
      GPDFS = new GraphProperties(G,true);
			cc = new CC(G);

//Il grafo deve essere costituito da una sola componente connessa

			if(cc.count()!=1)
        throw new RuntimeException("Graph must be connected");
		StdOut.println(args[0] + "\n" + G.toString());

////////////////////////////////////////////////////////////////////////////////

		for (int v = 0; v < G.V(); v++)
			StdOut.println("Eccentricity of " + v + " = "
					+ GP.eccentricity(G,v));
		StdOut.println("Diameter -> " + GP.diameter());
		StdOut.println("Radius -> " + GP.radius());
		StdOut.println("Center -> " + GP.center());
    StdOut.println("Girth -> " + GP.girth(G));
    StdOut.println("\n");


////////////////////////////////////////////////////////////////////////////////
/*
    StdOut.println("//////////DFS//////////");
    for (int v = 0; v < G.V(); v++)
      StdOut.println("Eccentricity [DFS] of " + v + " = "
          + GPDFS.eccentricityDFS(G,v));
    StdOut.println("Diameter [DFS] -> " + GPDFS.diameterDFS());
    StdOut.println("Radius [DFS] -> " + GPDFS.radiusDFS());
    StdOut.println("Center [DFS] -> " + GPDFS.centerDFS());
*/
////////////////////////////////////////////////////////////////////////////////
    }
}
