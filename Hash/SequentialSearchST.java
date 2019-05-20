/********************************************************************************************************
* Salvatore Danilo Palumbo - 0642907 																	
*																													
* Compilazione: javac SequentialSearchST.java 	
* Esecuzione: java SequentialSearchST: nel main è presente un test routine															
* Come nel BTree, ho usato al posto di URL e indirizzi IP coppie Lettera-Cifra,							
* per avere un main "più chiaro".																																																																																												
*//*****************************************************************************************************/



import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Queue;

//Questi primi metodi sono già presenti nel file
 
public class SequentialSearchST<Key, Value> {

    private int n;           // number of key-value pairs
    private Node first;      // the linked list of key-value pairs

    // a helper linked list data type
    private class Node {
        private Key key;
        private Value value;
        private Node next;

        public Node(Key key, Value value, Node next)  {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
    }

    // return number of key-value pairs
    public int size() {
        return n;
    }

    // does this symbol table contain the given key?
    public boolean contains(Key key) {
        for (Node x = first; x != null; x = x.next)
            if (key.equals(x.key)) return true;
        return false;
    }

    // add a key-value pair, replacing old key-value pair if key is already present
    public void put(Key key, Value value) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.value = value;
                return;
            }
        }
        first = new Node(key, value, first);
        n++;
    }

    // remove key-value pair with given key, and return associated value
    // return null if no such key
    public Value remove(Key key) {
        // special cases
        if (first == null) return null;

        if (key.equals(first.key)) {
            Value val = first.value;
            first = first.next;
            n--;
            return val;
        }

        // general case
        for (Node x = first; x.next != null; x = x.next)
            if (key.equals(x.next.key)) {
                Value val = x.next.value;
                x.next = x.next.next;
                n--;
                return val;
            }
        return null;
    }

    // return the value associated with the key, or null if the key is not present
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) return x.value;
        }
        return null;     // not found
    }
	

	
	
	//////////////////////////////////////////////////////////////////////////////////


    public Iterable<Key> keys() {
        PriorityQueue<Key> queue = new PriorityQueue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.add(x.key);
        return queue;
    }


   /***************************************************************************
    * Test routine.
    ***************************************************************************/
    public static void main(String[] args) {
        SequentialSearchST<String, String> st = new SequentialSearchST<String, String>();
		//Anche qua ho modificato il put di prova con coppie LETTERA-CIFRA, sempre per una questione di "semplicità"
        // insert some key-value pairs
		st.put("H", "1");
		st.put("B", "5");
		st.put("C", "3");
		st.put("D", "10");
		st.put("J", "4");
		st.put("I", "7");
		st.put("G", "9");
		st.put("A", "6");
		st.put("F", "1");
		st.put("E", "2");
		
		StdOut.println("\n");
        StdOut.println("Size -> " + st.size());
        StdOut.println("get (Value) of D -> " + st.get("D"));
        StdOut.println("get (Value) of A -> " + st.get("A"));
        StdOut.println("get (Value) of B -> " + st.get("B"));
        StdOut.println();
		StdOut.println("\n");

        // test out the iterator
		StdOut.println("Iterator w/ ArrayList");
        for (String s : st.keys()) 
            StdOut.println(s);
		
		StdOut.println("\n");
		StdOut.println("All key-value pairs");
        // print out all key-value pairs
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
        StdOut.println();
		
		StdOut.println("\n");
		
        StdOut.println("Deleting A two times");
        StdOut.println(st.remove("A"));
        StdOut.println(st.remove("A"));
    }

}