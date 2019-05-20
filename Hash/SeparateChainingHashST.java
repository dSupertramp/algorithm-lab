/********************************************************************************************************
* Salvatore Danilo Palumbo - 0642907 																	
*																												
* Compilazione: javac SeparateChainingHashST.java
* Esecuzione: java SeparateChainingHashST < tinyST.txt
* All' interno del file tinyST.txt è presente la stringa richiesta nell' esercizio	



*//*****************************************************************************************************/











import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Queue;
import java.util.ArrayList;
import java.lang.*;


// Questi primi metodi sono già presenti nel file

public class SeparateChainingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;   //Capacità iniziale 4 
    private int n;                                // number of key-value pairs
    private int m;                                // hash table size
    private SequentialSearchST<Key, Value>[] st;	// array of linked-list symbol tables
	

    public SeparateChainingHashST() {
        this(INIT_CAPACITY);
    } 

    /**
     * Initializes an empty symbol table with {@code m} chains.
     * @param m the initial number of chains
     */
    public SeparateChainingHashST(int m) {
        this.m = m;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
        for (int i = 0; i < m; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    } 

    // resize the hash table to have the given number of chains,
    // rehashing all of the keys
    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.m  = temp.m;
        this.n  = temp.n;
        this.st = temp.st;
    }

    // hash value between 0 and m-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    } 
	

    public int size() {
        return n;
    } 


    public boolean isEmpty() {
        return size() == 0;
    }


    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    } 

    /**
     * Returns the value associated with the specified key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with {@code key} in the symbol table;
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	 

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash(key);
        return st[i].get(key);
    } 

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (val == null) {
            delete(key);
            return;
        }

        // double table size if average length of list >= 10
        if (n >= 10*m) resize(2*m);

        int i = hash(key);
        if (!st[i].contains(key)) n++;
        st[i].put(key, val);
    } 

    /**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	 
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

        int i = hash(key);
        if (st[i].contains(key)) n--;
			delete(key);

        // halve table size if average length of list <= 2
        if (m > INIT_CAPACITY && n <= 2*m) resize(m/2);
    } 

	

    public Iterable<Key> keys() {
        PriorityQueue<Key> queue = new PriorityQueue<Key>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.add(key);
        }
        return queue;
    } 
	
	
	

	//PRIMO ESERCIZIO////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	* Conta il numero di collisioni e le elenca
	* @return void
	*/
	
	
	private void collisionsDistributions(){
		for(int i=0;i<m;i++){
			int counter = st[i].size()-1;
			StdOut.println("st["+i+"]: #: "+ counter +" - Distribution -> "+ st[i].keys());
			
		}
	}
	



	//SECONDO ESERCIZIO////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	* Per prendere la "più corta" delle due liste, ho aggiunto un secondo put e i metodi hash11 e hash17
	*/
	
	
	/**
	* Calcola una nuova funzione hash con 11 come moltiplicatore
	* @param key la chiave
	* @return hashcode
	*/
	
	private int hash11k(Key key){
		return 11 * (key.hashCode() & 0x7fffffff) % m;
	}

	
	
	/**
	* Calcola una nuova funzione hash con 17 come moltiplicatore
	* @param key la chiave
	* @return hashcode
	*/
	

	private int hash17k(Key key){
		return 17 * (key.hashCode() & 0x7fffffff) % m;
	}
	
	
	
	
	/**
	* Nuovo metodo put che confronta quale delle due liste è minore rispetto all'altra,
	* e di conseguenza effettua l'hash
	* @param key la chiave da aggiungere, val il valore da aggiungere
	* @return void
	*/
	


	public void put2(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        int i = hash11k(key);
		int j = hash17k(key);
        if (st[i].size() < st[j].size()){
			st[i].put(key, val);
		}
		else{
			st[j].put(key,val);
		}

    } 
	

	
	/**
	* Metodo che permette di restituire la chiave calcolata mediante hash con moltiplicatore 11
	* @return Value il valore associato alla chiave
	* @param key la chiave
	*/
	

	
	public Value get11k(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash11k(key);
        return st[i].get(key);
    }


	
	/**
	* Metodo che permette di restituire la chiave calcolata mediante hash con moltiplicatore 11
	* @return Value il valore associato alla chiave
	* @param key la chiave
	*/
	
	public Value get17k(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash17k(key);
        return st[i].get(key);
    } 
	
	

////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) { 
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
		/*
		* Inserimento con hash standard del file
		*/
		
	/*
		for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
	*/

	
		/*
		* Inserimento con hash dell'esercizio
		*/
		
		for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put2(key, i);
        }
	
	
	
		//Stampa hash table
		StdOut.println("\n");
		StdOut.println("Iterator: ");
		StdOut.println("----------");
		for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
	

		//Valore random
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'A');
		String rng = Character.toString(c);
		

	
		StdOut.println("\n");
		StdOut.println("----------------------------------------------");
		StdOut.println("----------------------------------------------");
		st.collisionsDistributions();
		StdOut.println("----------------------------------------------");
		StdOut.println("----------------------------------------------");
	
		

		
		
		//Nella ricerca, sarà restituito il valore preso dalla lista più piccola
		////Calcolo tempo con hash11 
		long startTime = System.nanoTime();    
		st.get11k(rng);
		long estimatedTime = System.nanoTime() - startTime;
		StdOut.println("Search random value: " + "[" + rng + "]" +  " w/ Hash11k: Value : ["+st.get11k(rng)+"] -> " + estimatedTime + " nanosec ");
		StdOut.println("----------------------------------------------");
		
		////Calcolo tempo hash17
		long sTime = System.nanoTime();  
		st.get17k(rng);
		long exTime = System.nanoTime() - sTime;
		StdOut.println("Search random value: " + "[" + rng + "]" +  " w/ Hash17k: Value : ["+st.get17k(rng)+"] -> " + estimatedTime + " nanosec ");
		StdOut.println("----------------------------------------------");
		
		
	
		
    }

}