/*******************************************************************************
* Salvatore Danilo Palumbo - 0642907
* Compilazione -> javac GeneratorAnalyzerDB.java
* Esecuzione -> java GeneratorAnalyzerDB
*
* Classe molto simile alla precedente che avevo creato per Kruskal,Prim
* e LazyPrim: crea un EdgeWeightedDigraph randomico ed effettua inoltre
* il calcolo dei tempi di esecuzione di Dijkstra E BellmanFord, partendo
* entrambi da una sorgente random.
* I grafi random saranno memorizzati in RNG2.txt, mentre i tempi di esecuzione
* di Dijkstra e di BellmanFord saranno memorizzati rispettivamente in
* TEMPI_DIJKSTRAV2.txt, TEMPI_BFV2.txt, TEMPI_DIJKSTRAVLOG.txt e TEMPI_BFVLOG.txt.
*
* Nell'esecuzione dei due algoritmi, il peso di un self-loop sarà 0.00.
* Nel caso in cui nel file si crea una coppia uguale, verrà presa in considerazione
* quella con peso minore
* Es: se nel file si viene a creare una situazione del tipo:
* - 2 0 0.94
* - 2 0 0.59
* Nel calcolo dello shortest path, allora si utilizzerà 2 0 0.59.
*
* Se il numero degli archi non è ne |V|^2-|V| ne |V|log|V|, allora i tempi di
* esecuzione dei due algoritmi saranno memorizzati in TEMPI_RANDOMD.txt e
* TEMPI_RANDOMBF.txt.
* Ho deciso di memorizzare tutti i tempi in file diversi in modo d'averne
* traccia più facilmente e può essere visto anche come una sorta di incapsulamento
* dei dati.
* I file contengono già dei tempi calcolati su grafi random.
*
*
*******************************************************************************/

import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.concurrent.TimeUnit;


public class GeneratorAnalyzerDB{


  // Viene utilizzato come flag
  //per scrivere nei rispettivi files
  public static int check = 0;

/**
* Crea un EWD randomicamente e lo scrive sul file RNG.txt
* @return void
*/


  public void generator() throws Exception{

    //Prendo in input il numero di vertici
    StdOut.println("\n");
    StdOut.println("-----G R A P H   C R E A T I O N-----");
    StdOut.println("Insert the number of vertices: ");
    Scanner inV = new Scanner(System.in);
    Integer vertices = inV.nextInt();

    /*Prendo in input il numero degli archi;
    * Se si immette -1 -> n° degli archi = |V|^2-|V|
    * Se si immette -2 -> n° degli archi = (round) |V|log|V|
    * In quest'ultimo caso, verrà presa solo la parte intera del risultato
    */

    StdOut.println("Insert the number of edges: \nIf you type -1 -> # of edges = |V|^2-|V|\nIf you type -2 -> # of edges = (round) |V|log|V|");
    Scanner inE = new Scanner(System.in);
    Integer edges = inE.nextInt();

    // Setto il valore di check qua in modo tale  da utilizzarlo
    // nel metodo analyzer

    check = edges;
    if(edges.equals(-1)){
      double v = vertices.doubleValue();
      edges = (int) (Math.pow(vertices, 2) - vertices);
    }else if(edges.equals(-2)){
      double v = vertices.doubleValue();
      edges = (int) Math.round(v*Math.log(v));
    }

    //Creo il file e lo formatto
    PrintWriter creator = new PrintWriter(new FileWriter("RNG2.txt"));
    creator.write(Integer.toString(vertices));
    creator.write("\n");
    creator.write(Integer.toString(edges));
    creator.write("\n");

    //Questo for permette di generare terne (primo vertice, secondo vertice, peso dell'arco)
    //fino ad un massimo pari al numero di archi
    for(int i=0;i<edges;i++){
      Random randV1 = new Random();
      Integer v1 = randV1.nextInt(vertices);
      Random randV2 = new Random();
      Integer v2 = randV2.nextInt(vertices);

      //Approssimazione del peso degli archi
      double random = new Random().nextDouble();
      Double roundOff = 0 + (random * (1 - 0));
      Double weight = Math.round(roundOff * 100.0) / 100.0;

      creator.write(Integer.toString(v1));
      creator.write(" ");
      creator.write(Integer.toString(v2));
      creator.write(" ");
      creator.write(Double.toString(weight));
      creator.write("\n");
    }
    creator.close();
    StdOut.printf("EdgeWeightedDigraph created:\n|V| = %d - |E| = %d",vertices,edges);
    StdOut.println("\n");
  }
  /**
  * Calcola i tempi di esecuzione degli algoritmi
  * @return void
  */


  public void analyzer() throws Exception{
    StdOut.println("\n");
    StdOut.println("-----C O M P U T I N G / S A V I N G  T I M E S-----");
    PrintWriter out = new PrintWriter(new FileWriter("TEMPI_DIJKSTRAV2.txt",true));
    PrintWriter out1 = new PrintWriter(new FileWriter("TEMPI_BFV2.txt",true));
    PrintWriter out2 = new PrintWriter(new FileWriter("TEMPI_DIJKSTRAVLOG.txt",true));
    PrintWriter out3 = new PrintWriter(new FileWriter("TEMPI_BFVLOG.txt",true));
    PrintWriter out4 = new PrintWriter(new FileWriter("TEMPI_RANDOMD.txt",true));
    PrintWriter out5 = new PrintWriter(new FileWriter("TEMPI_RANDOMBF.txt",true));

    In in = new In("RNG2.txt");
    EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

    //Serve per generare un sorgente casuale
    Random rand = new Random();
    int randomSource = rand.nextInt(G.V());

    //Tempi Per DijkstraSP
    Long dStart = System.nanoTime();
    DijkstraSP D = new DijkstraSP(G,randomSource);
    Long dEnd = (System.nanoTime() - dStart)/1000000;
    String dTime = Long.toString(dEnd);
    if(check == (-1)){
      out.write(dTime);
      out.write("\n");
      out.close();
    }else if(check == (-2)){
      out2.write(dTime);
      out2.write("\n");
      out2.close();
    }
    else{
      out4.write(dTime);
      out4.write("\n");
      out4.close();
    }



    //Tempo per Bellman-Ford
    Long bfStart = System.nanoTime();
    BellmanFordSP bf = new BellmanFordSP(G,randomSource);
    Long bfEnd = (System.nanoTime() - bfStart)/1000000;
    String bfTime = Long.toString(bfEnd);
    if(check == (-1)){
      out1.write(bfTime);
      out1.write("\n");
      out1.close();
    }else if(check == (-2)){
      out3.write(bfTime);
      out3.write("\n");
      out3.close();
    }
    else{
      out5.write(bfTime);
      out5.write("\n");
      out5.close();
    }
    StdOut.println("Times computed correctly. Check the files.");

   }



    public static void main(String[] args) throws Exception{
      GeneratorAnalyzerDB G = new GeneratorAnalyzerDB();
      G.generator();
      G.analyzer();
    }
  }
