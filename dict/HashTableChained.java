/* HashTableChained.java */

package dict;


import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
	private int numBuckets;
	private int numEntries;
	private List[] table;
	private int numCollisions;
	private int hashPrime;
	private int mapCode;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   *  @param sizeEstimate, the number of entries, not the number of buckets
   **/
	
	
  public HashTableChained(int sizeEstimate) {
	  numEntries = 0;
	  numBuckets = nextPrime(sizeEstimate*4/3);
	  table = new DList[numBuckets];
	  for(int i = 0; i< numBuckets; i++){
		  table[i] = new DList();
	  }
	  hashPrime = nextPrime(1000 * numBuckets);
  }

  /**
   * 
   * @param size, the number of buckets
   * @param flag, no meaningful, just contrary to the the above function
   */
  public HashTableChained(int size, boolean flag) {
	  numEntries = 0;
	  numBuckets = nextPrime(size);
	  table = new DList[numBuckets];
	  for(int i = 0; i< numBuckets; i++){
		  table[i] = new DList();
	  }
	  hashPrime = nextPrime(1000 * numBuckets);
  }
  
  
  
  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
	  this(100);
  }
  
  /**
   * this method is to return the current load factor, n/N
   * entries per bucket
   * @return
   */
  public double loadFactor(){
	  return ((double)numEntries) / numBuckets;
  }
  /**
   * isPrime() to tell whether the integer n is prime or not
   * @param n, the input integer
   */
  public static boolean isPrime(int n){
	if(n <= 1){
		return false;
	}
	for(int i=2; i<Math.sqrt(n);i++){
		if(n%i== 0){
			return false;
		}
	}
	return true;
  }
  
  /**
   * nextPrime() return the prime next to n
   */
  public static int nextPrime(int n){
	  while(!isPrime(n)){
		  n++;
	  }
	  return n;
  }
 
  
  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    return ((5 * code + 8) % hashPrime + hashPrime) % numBuckets;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return numEntries;
  }
  
  /**
   * Returns the number of collisions happen in this hash table
   * @return
   */
  public int numberCollision() {
    return numCollisions;
  }
  /**
   * Returns the number of buckets in this hash table 
   * @return
   */
  public int numberBuckets(){
	  return numBuckets;
  }
  
  /**
   * Returns the number of entries in this hash table
   * @return
   */
  public int numberEntries(){
	  return numEntries;
  }
  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    return numEntries == 0;
  }

  
  public void resize(){
	  try{ 
		  HashTableChained hash = null;
		  if(loadFactor() >= 0.75){
			  hash = new HashTableChained(numBuckets*2,true); 
		  }else if(loadFactor() <= 0.25){
			  hash = new HashTableChained(numBuckets/2,true);
		  }
		  for(int i=0; i<numBuckets;i++){
			  ListNode curr = this.table[i].front();
			  while(curr.isValidNode()){
				  Entry entry = (Entry)curr.item();
				  hash.insert(entry.key,entry.value);
				  curr = curr.next();
			  }
		  }
		  this.table = hash.table;
		  this.numBuckets = hash.numBuckets;
		  this.numEntries = hash.numEntries;
		  this.numCollisions = hash.numCollisions;
		  this.hashPrime = hash.hashPrime;
		  this.mapCode = hash.mapCode;
	  } catch(InvalidNodeException e){
		  e.printStackTrace();
	  }
  }
  
  
  
  
  
  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
	if(loadFactor() >= 0.75){
	  resize();
	}
  	mapCode = compFunction(key.hashCode()); 
  	Entry entry = new Entry();
  	entry.key = key;
  	entry.value = value;
  	if(table[mapCode].length() > 0){
  		numCollisions++;
  	}
    table[mapCode].insertBack(entry);
    numEntries++;
    return entry;
  }

  
  
  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
	mapCode = compFunction(key.hashCode()); 
    if(table[mapCode] !=null){
      List lst = table[mapCode];
      ListNode curr = lst.front();
      try{
  		while(curr.isValidNode()){
  			if(((Entry)curr.item()).key.equals(key)){
  				return (Entry) curr.item();
  			}else{
  				curr = curr.next();
  			}
  		}
  		return null;
	   }catch(InvalidNodeException e){
			System.out.println(e);
	   }
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
	  if(loadFactor() <= 0.25){
		  resize();
	  }
	  mapCode = compFunction(key.hashCode()); 
	    if(table[mapCode] !=null){
	      List lst = table[mapCode];
	      ListNode curr = lst.front();
	      try{
	  		while(curr.isValidNode()){
	  			if(((Entry)curr.item()).key.equals(key)){
	  				Entry e = (Entry)curr.item();
	  				if(lst.length() > 1){
	  					numCollisions--;
	  				}
	  				curr.remove();
	  				numEntries--;
	  				return e;
	  			}else{
	  				curr = curr.next();
	  			}
	  		}
	  		return null;
		   }catch(InvalidNodeException e){
				System.out.println(e);
		   }
	    }
	    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
	  for(int i = 0; i< numBuckets; i++){
		  table[i] = new DList();
	  }
	  numEntries = 0;
	  numCollisions = 0;
  }
  
//  public String toString(){
//	  String s = "{  ";
//	  try{
//		  for(List lst : table){
//			  ListNode curr = lst.front();
//			  while(curr.isValidNode()){
//				  s += ((Entry)curr.item()).key + " : " + ((Entry)curr.item()).value + "  ";
//				  curr = curr.next();
//			  }
//		  }
//		  s += "  }"; 
//	  }catch(InvalidNodeException e){
//		  System.out.println(e);
//	  }
//	return s;
//  }
  public String toString(){
	  String s = "";
	  int count = 0;
	  for(List lst : table){
		  s += "[  " + lst.length() + "  ]  ";
		  count ++;
		  if(count % 10 == 0){
			  s += "\n";
		  }
	  }
	  return s;
  }
  
  public static void main(String[] args) {
	    // prime numbers
//	    int i = 100;
//	    System.out.println("Testing nextPrime() of " + i + ". Get? " + HashTableChained.nextPrime(i));
//	    HashTableChained table1 = new HashTableChained(500);
//	    System.out.println("Table 1 has " + table1.numBuckets + " buckets");
//	    System.out.println("Table 1 should be 'empty', and isEmpty() is " + table1.isEmpty());
//	    HashTableChained table2 = new HashTableChained();
//	    System.out.println("Table 2 has " + table2.numBuckets + " buckets");
//	    // compFunction()
//	    System.out.println("1000's hashCode is " + table1.compFunction(1000) + " in table 1, and " + table2.compFunction(1000) + " in table 2");
//	    // insert() and find()
//	    table1.insert(10, "CS61B");
//	    table1.insert(20, "CS61A");
//	    System.out.println("Testing insert() entries, 'CS61A', 'CS61B'. The hashTable is " + table1);
//	    System.out.println("Testing find() 'CS61B' in table 1 with KEY 10. Get? " + table1.find(10).value);
//	    // remove()
//	    System.out.println("Testing remove() 'CS61B' in table 1 with KEY 10. Get? " + table1.remove(10).value + ". The current hashTable is " + table1);
//	    // make empty
//	    table1.makeEmpty();
//	    System.out.println("Testing makeEmpty(). table1 is " + table1);
//	    // 127 & 16908799
//	    System.out.println("Is 127 a prime? " + HashTableChained.isPrime(127));
//	    System.out.println("Is 16908799 a prime? " + HashTableChained.isPrime(16908799));
	    HashTableChained table3 = new HashTableChained(1);
	    System.out.println("Table 3 has " + table3.numBuckets + " buckets");
	    System.out.println("original table:" + table3);
//	    Integer inte = new Integer(1000);
	    table3.insert(1, "test1");
	    table3.insert(2, "test2");
	    table3.insert(3, "test3");
	    table3.insert(4, "test4");
	    table3.insert(5, "test5");
	    table3.insert(6, "test6");
	    table3.insert(7, "test6");
	    table3.insert(6, "test8");
	    table3.remove(3);
	    table3.remove(1);
	    table3.remove(2);
	    table3.remove(4);
	    table3.remove(7);
	    table3.remove(6);
	    System.out.println(table3);
	    System.out.println(table3.compFunction(1) + " " + table3.compFunction(2) + " " + 
	    					table3.compFunction(3) + " " + table3.compFunction(4) +  " " + table3.compFunction(5));
	    System.out.println("number of collisions: " + table3.numberCollision());
	    System.out.println("number of buckets: " + table3.numberBuckets());
	    System.out.println("number of entries: " + table3.numEntries);
	    table3.remove(3);
	    System.out.println("number of collisions: " + table3.numberCollision());
	    System.out.println("number of buckets: " + table3.numberBuckets());
	    System.out.println("number of entries: " + table3.numEntries);
	  }

}
