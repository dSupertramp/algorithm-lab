/********************************************************************************
* Salvatore Danilo Palumbo - 0642907
* Compilazione: javac BTree.java
* I parametri passati nel BTree sono stringhe: ho usato al posto di URL 
* e indirizzi IP dati di default coppie contenenti come Key una lettera
* mentre come Value un numero, solamente per semplicità ed ordine.
* I parametri "default" presenti nel main sono:
* E -> per i metodi floor, rank e get
* A e J -> (x e y nel main) per il metodo size
*
********************************************************************************/

import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;
import java.util.Iterator;

//Tutti i primi metodi sono quelli già presenti nel file

public class BTree<Key extends Comparable<Key>, Value>  {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static int M = 4;

    private Node root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree

    // helper B-tree node data type
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children

        // create a node with k children
        private Node(int k) {
            m = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
	static class Entry {
        private Comparable key;
        private Object val;
        public Node next;    
        public Entry(Comparable key, Object val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

	//Metodi di default già presenti in precedenza
    public BTree(){
        root = new Node(0);
    }
 

    public boolean isEmpty() {
        return size() == 0;
    }


    public int size() {
        return n;
    }

    public int height() {
        return height;
    }



    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (equaals(key, children[j].key)) return (Value) children[j].val;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }



    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        Node u = insert(root, key, val, height); 
        n++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, val, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--)
            h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }


    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j]; 
        return t;    
    }

 
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent + children[j].key + " " + children[j].val + "\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }


  
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean equaals(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }
	
	
	private boolean greater(Comparable k1, Comparable k2){
		return k1.compareTo(k2) > 0;
	}
	
		
	
	
	 
	 /*****************NUOVI METODI***************************************************************/
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
		/**Verifica se un nodo è una foglia controllando che il primo figlio
	abbia next settato a null.
	@return true se foglia, false altrimenti.
	@param nodo
	*/
    public Boolean isLeaf(Node x){
        if(x!=null){
            if(x.children[0]!=null && x.children[0].next==null)
				return true;
			}
            return false;
        }
	
	/**Restituisce la chiave minima presente all'interno del BTree: basato sul metodo search
	@return o la chiave minima o null
	@param nodo, altezza*/
	private Comparable minima(Node x, int ht){
		Entry[] children = x.children;
		Comparable minimum = children[0].key;
		if(ht == 0){
			for(int i=0; i<x.m;i++){
				if(greater(minimum, children[i+1].key))
					minimum = (Comparable) children[i+1].key; //cerco il minimo nel II° nodo
					return  (Comparable) minimum; 
			}
		}
		else{
			for(int i=0;i<x.m;i++){ // itero la ricerca del massimo sull' albero
				if(i+1 == x.m || greater(minimum, children[i+1].key))
					return minima (children[i-1].next, ht-1);
			}
		}
		return null;
	}
	
	/**Metodo getMin: contiene il metodo minima incapsulato 
	@return metodo minima su BTree dato in input*/
	public Comparable getMin(){
        return minima(root,height);
    }
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**Restituisce la chiave massima presente all'interno del BTree: basato sul metodo search
	@return o la chiave massima o null
	@param nodo, altezza*/
	private Comparable maxima(Node x, int ht){
		Entry[] children = x.children;
		Comparable maximum = children[0].key;
		if(ht == 0){
			for(int i=0;i<x.m;i++){
				if(less(children[i].key, children[i+1].key)){ //cerco il massimo nel I° nodo 
					maximum = (Comparable) children[i+1].key;
					return (Comparable) maximum;
				}
			}
		}else{
			for(int i=0;i<x.m;i++){ // itero la ricerca del massimo sull' albero
				if(i+1 == x.m || greater(maximum, children[i].key))
					return maxima(children[i].next,ht-1);
			}
		}
		return null;
	}
	
	
	/**Metodo getMax: contiene il metodo maxima incapsulato 
	@return metodo maxima su BTree dato in input*/
	public Comparable getMax(){
        return maxima(root,height);
    }
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	/**Conta il numero delle chiavi minori o uguali alla key passata a parametro: basato sul metodo search
	@return un counter, pari al numero di key contate
	@param nodo, chiave in input*/
	private int rank(Node x, Key key){
		int count =0;
		if(isLeaf(x)){
			for(int i=0;i<x.m;i++){
				Comparable control = x.children[i].key;
				if(control.compareTo(key)<=0){
					++count;
				}
				/**
				 * Se trovo un elemento maggiore della chiave, posso benissimo uscire dal ciclo for
				 * perché gli altri elementi saranno sicuramente maggiori.
				 */
				else
					break;
			}
		}
		else{
			for(int i=0;i<x.m;i++){
				/**
				 * anche in questo caso, la chiamata ricorsiva viene fatta sugli elementi minori o uguali alla chiave 
				 * passata a parametro. Ciò vale perché se troviamo una chiave maggiore di quella passata a parametro,
				 * sicuramente i suoi figli non potranno essere minori di essa.
				 */
				Comparable control = x.children[i].key;
				if(control.compareTo(key)<=0){
					count+=rank(x.children[i].next, key);
				}
				else
					break;
			}
		}
		return count;
	}
	
	/**Metodo getMax: contiene il metodo rank incapsulato 
	@return metodo rank su BTree dato in input 
	@param la chiave in input*/
	public int getRank(Key key){
		return rank(root, key);
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**Elimina la chiave massima presente nel BTree (di default la chiave eliminata è la prima, quindi A), basato sul metodo search
	@return l'albero con il nodo eliminato (albero da stampare nel caso si voglia visualizzare)
	@param nodo, altezza*/ 
	private void deleteMin (Node x, int ht){
		Entry[] children = x.children;
		Comparable minimum = children[0].key;
		if(ht == 0){
			for(int i=0; i<x.m;i++){
				if(greater(minimum, children[i+1].key))
					minimum = (Key) children[i+1].key;
				if(equaals(minimum,children[i].key)){
					children[i].key=null;
					children[i].val=null;
				}
			}
			
		}
		else{
			for(int i=0;i<x.m;i++){
				if(i+1 == x.m || greater(minimum, children[i+1].key))
					deleteMin(children[i-1].next, ht-1);
			}
		}
	}	
	
	/**Metodo getDeleteMin: contiene il metodo deleteMin incapsulato 
	@return void*/
	public void getDeleteMin(){
		deleteMin(root,height);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	/**Restituisce la chiave più grande minore della key data in input
	@param nodo, chiave input
	@return la key o null*/
	private Comparable floor(Node x, Key key){
		if(x != null){
			if(x.m > 0){
				Entry tmp = null;
				int i=0;
				while(i<x.m){
					Comparable control = x.children[i].key;
					if(control.compareTo(key)<=0){
						tmp = x.children[i];
						i++;
					}
					else{
						break;
					}
				}
				if(tmp!=null){
					if(tmp.key==key)
						return (Comparable) tmp.key;
					if(tmp.next!=null)
						return floor(tmp.next,key);
				}
				else
					return (Comparable) tmp.key;
			}
		}
		return null;
	}
	
	
	/**Metodo getMax: contiene il metodo floor incapsulato 
	@return metodo getFloor su BTree dato in input
	@param chiave in input*/
	public Comparable getFloor(Key key){
		return floor(root,key);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	/**Restituisce il numero di chiavi in un range che va da x a y (passati a param)
	@return il numero di chiavi comprese tra x e y
	@param, nodo, chiave q e w (range)*/
        public int sizeRanged(Node x, Comparable q, Comparable w){
			int count = 0;
			Entry [] children = x.children;
			if(isLeaf(x)){
				for(int i=0;i<x.m;i++){
					Comparable control = x.children[i].key;
					if(control.compareTo(q)>=0 && control.compareTo(w)<=0){
						++count;
					}
				}
			}else{
				for(int i=0;i<x.m;i++){
					Comparable control = x.children[i].key;
					if(control.compareTo(q)>=0 && control.compareTo(w)<=0){
						count += sizeRanged(x.children[i].next, q,w);
					}
				}
			}
			return count;
        }	
	
	/**Metodo getSizeRanged: contiene il metodo size incapsulato 
	@return metodo sizeRanged su BTree dato in input
	@param le chiavi in input*/
		public int getSizeRanged(Comparable x, Comparable y){
			return sizeRanged(root,x,y);
		}

	////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**Aggiunge le keys presenti nell' albero all'interno di un ArrayList
	@return void
	@param nodo, q ArrayList da riempire
	*/
	
	public void keys(Node x, ArrayList<Comparable> q){
		if(isLeaf(x)){
			for(int i=0;i<x.m;i++){
				q.add(x.children[i].key);
			}
		}
		else{
			for(int i=0;i<x.m;i++){
				keys(x.children[i].next, q);
			}
		}
	}
	
	/**Crea l'ArrayList da riempire
	@return ArrayList riempito
	@param nodo
	*/
	
	
	private ArrayList<Comparable> keys(Node x){
        if(x!=null){
            if(x!=null){
                ArrayList<Comparable> q = new ArrayList<Comparable>();
                keys(x,q);
                return q;
            }
        }
        return null;
    }
	
	
	/**Metodo getKeys: contiene il metodo keys incapsulato 
	@return metodo keys su BTree dato in input*/
	public ArrayList<Comparable> getKeys(){
		return keys(root);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        BTree<String, String> st = new BTree<String, String>();
		Comparable x = "A";
		Comparable y = "J";
		///////////////////////////////////////////////////////////////////////
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
		///////////////////////////////////////////////////////////////////////
		StdOut.println("\n");
		StdOut.println(st);
		StdOut.println("\n");
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
        StdOut.println("Size -> " + st.size());
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
        StdOut.println("Height -> " + st.height());
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("Get -> " + st.get("Y"));
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("Min -> " + st.getMin());
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("Max -> " + st.getMax());
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("Floor -> " + st.getFloor("Y"));
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("Rank -> " + st.getRank("Y"));
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("Size Ranged -> " + st.getSizeRanged(x,y));
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("\n");
		StdOut.println("\n");
		StdOut.println(st);
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("\t///In ArrayList///");
		StdOut.println("\n");
		ArrayList<Comparable> keys = st.getKeys();
        Iterator<Comparable> it = keys.iterator();
        while(it.hasNext()){
            StdOut.println(it.next());
        }
		st.getDeleteMin();
		StdOut.println("---------------------------------------------------");
		StdOut.println("---------------------------------------------------");
		StdOut.println("\t///DeleteMin///");
		StdOut.println("\n");
		StdOut.println(st);
		
		
	}
}
