/********************************************************************************************************
* Salvatore Danilo Palumbo - 0642907 																
*																												
* Compilazione: javac LinearProbingHashST.java 		
* Esecuzione: java LinearProbingHashST < tinyST.txt
* All' interno del file tinyST.txt è presente la stringa richiesta nell' esercizio	
*//*****************************************************************************************************/

import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Queue;
import java.util.ArrayList;
import java.lang.*;
 
 
 // Questi primi metodi sono già presenti nel file

public class LinearProbingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int n;           // number of key-value pairs in the symbol table
    private int m;           // size of linear probing table
    private Key[] keys;      // the keys
    private Value[] vals;    // the values


    /**
     * Initializes an empty symbol table.
     */
    public LinearProbingHashST() {
        this(INIT_CAPACITY);
    }

    /**
     * Initializes an empty symbol table with the specified initial capacity.
     *
     * @param capacity the initial capacity
     */
    public LinearProbingHashST(int capacity) {
        m = capacity;
        n = 0;
        keys = (Key[])   new Object[m];
        vals = (Value[]) new Object[m];
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    // hash function for keys - returns value between 0 and M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    // resizes the hash table to the given capacity by re-hashing all of the keys
    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>(capacity);
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], vals[i]);
            }
        }
        keys = temp.keys;
        vals = temp.vals;
        m    = temp.m;
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

        // double table size if 50% full
        if (n >= m/2) resize(2*m);

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
        }
        keys[i] = key;
        vals[i] = val;
        n++;
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with {@code key};
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
            if (keys[i].equals(key))
                return vals[i];
        return null;
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
        if (!contains(key)) return;

        // find position i of key
        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % m;
        }

        // delete key and associated value
        keys[i] = null;
        vals[i] = null;

        // rehash all keys in same cluster
        i = (i + 1) % m;
        while (keys[i] != null) {
            // delete keys[i] an vals[i] and reinsert
            Key   keyToRehash = keys[i];
            Value valToRehash = vals[i];
            keys[i] = null;
            vals[i] = null;
            n--;
            put(keyToRehash, valToRehash);
            i = (i + 1) % m;
        }

        n--;

        // halves size of array if it's 12.5% full or less
        if (n > 0 && n <= m/8) resize(m/2);

        assert check();
    }

	
/////////////////////////////////////////////////////////////////////////////////
    /**
     * Returns all keys in this symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in this symbol table
     */
	 

    public Iterable<Key> keys() {
        PriorityQueue<Key> queue = new PriorityQueue<Key>();
        for (int i = 0; i < m; i++)
            if (keys[i] != null) queue.add(keys[i]);
        return queue;
    }

	
    // integrity check - don't check after each put() because
    // integrity not maintained during a delete()
    private boolean check() {

        // check that hash table is at most 50% full
        if (m < 2*n) {
            System.err.println("Hash table size m = " + m + "; array size n = " + n);
            return false;
        }

        // check that each key in table can be found by get()
        for (int i = 0; i < m; i++) {
            if (keys[i] == null) continue;
            else if (get(keys[i]) != vals[i]) {
                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
                return false;
            }
        }
        return true;
    }


	///////////////////////////////////NUOVI METODI//////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	
	
	
	
	/**
	* Nuovo metodo hash dell' esercizio
	* @param key la chiave, m la dimensione della hash table
	* @return hashcode
	*/
	
	public int newHash(Key key, int m){
		 m = 11;
		return(key.hashCode() & 0x7fffffff) % (m-1);
	}
	
	
	/**
	* Nuovo metodo put: usa la funzione newHash
	* @param key la chiave da inserire, val il valore da inserire
	* @return il valore k dipendente dalla chiave
	*/
	
	
	
   public int newPut(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");

        if (val == null) {
            delete(key);
            return -1;
        }

        // double table size if 50% full
        if (n >= m/2) resize(2*m);

        int i;
		int k = newHash(key,m);
        for (i = hash(key); keys[i] != null; i = (i + k) % m) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return k;
            }
        }
        keys[i] = key;
        vals[i] = val;
        n++;
		return k;
    }
	
	
	
	
	/**
	* Nuovo metodo get: usa la funzione newHash
	* @param key la chiave associata al valore
	* @return val il valore associato alla chiave
	*/
	
	
	public Value newGet(Key key) {
		int k = newHash(key, m);
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (int i = hash(key); keys[i] != null; i = (i + k) % m)
            if (keys[i].equals(key))
                return vals[i];
        return null;
    }
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) { 
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<String, Integer>();
		
			
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
		* Inserimento con hash modificato dell'esercizio
		*/
		
	
		for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.newPut(key, i);
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
		
		
		
		
		long startTime = System.nanoTime();   
		st.newGet(rng);
 		long estimatedTime = System.nanoTime() - startTime;
		StdOut.println("----------------------------------------------");
		StdOut.println("----------------------------------------------");
		StdOut.println("Search random value: " + "[" + rng + "]" +  " w/ newHash: Value : ["+st.newGet(rng)+"] -> " + estimatedTime + " nanosec ");
		StdOut.println("----------------------------------------------");
		
    }
}