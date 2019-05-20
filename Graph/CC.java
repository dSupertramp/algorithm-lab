/******************************************************************************
 *  Compilation:  javac CC.java
 *  Execution:    java CC filename.txt
 *  Dependencies: Graph.java StdOut.java Queue.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  Compute connected components using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java CC tinyG.txt
 *  3 components
 *  0 1 2 3 4 5 6
 *  7 8
 *  9 10 11 12
 *
 *  % java CC mediumG.txt
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  % java -Xss50m CC largeG.txt
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  Note: This implementation uses a recursive DFS. To avoid needing
 *        a potentially very large stack size, replace with a non-recurisve
 *        DFS ala NonrecursiveDFS.java.
 *
 ******************************************************************************/
/*Tutti i primi metodi sono già presenti nel file default
* I nuovi metodi si trovano alla fine del file
* Il calcolo del grado di separazione del grafo viene effettuato sia utilizzando
* il DFS che il BFS
*/

public class CC {
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    /**
     * Computes the connected components of the undirected graph {@code G}.
     *
     * @param G the undirected graph
     */
    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    /**
     * Computes the connected components of the edge-weighted graph {@code G}.
     *
     * @param G the edge-weighted graph
     */
    public CC(EdgeWeightedGraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // depth-first search for a Graph
    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // depth-first search for an EdgeWeightedGraph
    private void dfs(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }


    /**
     * Returns the component id of the connected component containing vertex {@code v}.
     *
     * @param  v the vertex
     * @return the component id of the connected component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int id(int v) {
        validateVertex(v);
        return id[v];
    }

    /**
     * Returns the number of vertices in the connected component containing vertex {@code v}.
     *
     * @param  v the vertex
     * @return the number of vertices in the connected component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int size(int v) {
        validateVertex(v);
        return size[id[v]];
    }

    /**
     * Returns the number of connected components in the graph {@code G}.
     *
     * @return the number of connected components in the graph {@code G}
     */
    public int count() {
        return count;
    }

    /**
     * Returns true if vertices {@code v} and {@code w} are in the same
     * connected component.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @return {@code true} if vertices {@code v} and {@code w} are in the same
     *         connected component; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
    public boolean connected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id(v) == id(w);
    }

    /**
     * Returns true if vertices {@code v} and {@code w} are in the same
     * connected component.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @return {@code true} if vertices {@code v} and {@code w} are in the same
     *         connected component; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     * @deprecated Replaced by {@link #connected(int, int)}.
     */
    @Deprecated
    public boolean areConnected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id(v) == id(w);
    }

    // throw an IllegalArgume ntException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	* Metodi per la ricerca del grado di separazione (con DFS) del grafo in tinyG.txt
	* Sono presenti altri 2 metodi: uno per calcolare il grado di separazione di ogni singolo
	* nodo in una componente connessa e l'altro per calcolare il grado di separazione dell'intera
	* componente connessa. Infine l'altro metodo calcola il grado di separazione dell'intero grafo,
	* ossia la media di passi che servono per collegare un nodo all'altro
	*/


	/**
	* Calcola il grado di separazione di un singolo vertice del grafo
	* @param il grafo G, il vertice s
	* @return il grado di separazione di s
	*/

	private float vertexDegreeDFS(Graph G, int s){
		DepthFirstPaths dfs = new DepthFirstPaths(G,s);
		int n=0;
		int c=0;
		float degree = 0;
		for(int v=0;v<G.V();v++){
			if(dfs.hasPathTo(v)){
				degree = dfs.distTo(v);
				if(degree != 0){
					n++;
					c += degree;
				}
			}
		}
		return (float) c/n;
	}


	/**
	* Calcola il grado di separazione di una componente connessa (che viene trasferita in una Queue)
	* @param il grafo G, la Queue utilizzata
	* @return il grado di separazione della componente connessa
	*/

	private float componentDegreeDFS(Graph G, Queue <Integer> component){
		DepthFirstPaths dfs;
		int sum=0;
		int n=0;
		int vertexDegree=0;
		for(int s: component){
			dfs = new DepthFirstPaths(G,s);
			for(int v=0;v<G.V();v++){
				if(dfs.hasPathTo(v)){
					vertexDegree = dfs.distTo(v);
					if(vertexDegree != 0){
						sum += vertexDegree;
						n++;
					}
				}
			}
		}
		return (float) sum/n;
	}



	/**
	* Calcola il grado di separazione dell'intero grafo
	* @param il grafo G
	* @return il grado di separazione dell'intero grafo
	*/
	public float graphDegreeDFS(Graph G){
		int m=this.count();
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
		int n=0;
		float sum = 0;
		for(int i=0;i<m;i++){
			components[i] = new Queue<Integer>();
		}
		for(int v=0;v<G.V();v++){
			components[this.id(v)].enqueue(v);
		}
		for(int i=0;i<m;i++){
			sum += componentDegreeDFS(G, components[i]);
			n++;
		}
		return (float) sum/n;
	}



//////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	* Questi altri metodi sono uguali ai precedenti con la differenza che utilizzano il BFS
	*/

	public float vertexDegreeBFS(Graph G, int s){
		BreadthFirstPaths bfs = new BreadthFirstPaths(G,s);
		int n=0;
		int c=0;
		float degree = 0;
		for(int v=0;v<G.V();v++){
			if(bfs.hasPathTo(v)){
				degree = bfs.distTo(v);
				if(degree != 0){
					n++;
					c += degree;
				}
			}
		}
		return (float) c/n;
	}




	private float componentDegreeBFS(Graph G, Queue <Integer> component){
		BreadthFirstPaths bfs;
		int sum=0;
		int n=0;
		int vertexDegree=0;
		for(int s: component){
			bfs = new BreadthFirstPaths(G,s);
			for(int v=0;v<G.V();v++){
				if(bfs.hasPathTo(v)){
					vertexDegree = bfs.distTo(v);
					if(vertexDegree != 0){
						sum += vertexDegree;
						n++;
					}
				}
			}
		}
		return (float) sum/n;
	}


	public float graphDegreeBFS(Graph G){
		int m=this.count();
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
		int n=0;
		float sum = 0;
		for(int i=0;i<m;i++){
			components[i] = new Queue<Integer>();
		}
		for(int v=0;v<G.V();v++){
			components[this.id(v)].enqueue(v);
		}
		for(int i=0;i<m;i++){
			sum += componentDegreeBFS(G, components[i]);
			n++;
		}
		return (float) sum/n;
	}

/////////////////////////////////////////////////////////////////////////////////




    /**
     * Unit tests the {@code CC} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        CC cc = new CC(G);

        // number of connected components
        int m = cc.count();
        StdOut.println(m + " connected components");
		int s = cc.size(1); // size vuole a parametro il nodo contenuto nella componente connessa
		StdOut.println(s + " vertex of component");
		StdOut.println("\n");



        // compute list of vertices in each connected component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
        for (int i = 0; i < m; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < G.V(); v++) {
            components[cc.id(v)].enqueue(v);
        }

        // print results
        for (int i = 0; i < m; i++) {
            for (int v : components[i]) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

		StdOut.println("\n");


/////////////////////////////////////////////////////////////////////////

	float graphDegreeDFS = cc.graphDegreeDFS(G);
	StdOut.printf("Graph Degree (DFS) -> [%.5f]", graphDegreeDFS);
	StdOut.println("\n");

	float graphDegreeBFS = cc.graphDegreeBFS(G);
	StdOut.printf("Graph Degree (BFS) -> [%.5f]", graphDegreeBFS);
	StdOut.println("\n");

/////////////////////////////////////////////////////////////////////////
    }
}
