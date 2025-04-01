import java.util.LinkedList;

public class ChainedHashSet<E> implements AmhHashSet<E> {

    private LinkedList<E>[] storage;
    private int n;
    private int cap;
    private int collisions;
    
    public ChainedHashSet (int capacity) {
        storage = new LinkedList[capacity];
        cap = capacity;

    } 

    public boolean insert (E key) {
        // Get the list for the specific hash
        LinkedList<E> insertHere = storage[hash(key)];

        // If there is no linked list, make one
        if(insertHere == null){
            insertHere = new LinkedList<E>();
            storage[hash(key)] = insertHere;
        }

        // If this element isn't in the chained hash set, add it
        if(lookup(key) == false){
            // Collisions happen when you add an element with the same hashed value, and since the LL isn't empty we 
            // have another collision
            if(insertHere.size() != 0){
                collisions++;
            }
            insertHere.add(key);
            storage[hash(key)] = insertHere;
            n++;
            return true;
        }

        return false;

    }

    public boolean lookup (E key) {
        // Search chained hash set to see if a key has already been added

        // Find which list the element would be in 
        LinkedList<E> thisList = storage[hash(key)];
        if(thisList == null){
            return false;
        }

        // Linear traversal of list
        for(int i = 0; i < thisList.size(); i++){
            if(thisList.get(i).equals(key)){
                return true;
            }
        }
        return false;

    } // lookup ()

    public boolean remove (E key) {
        // Remove key if it actually is in the chained hash set

        LinkedList<E> removeHere = storage[hash(key)];
        if(lookup(key) == true){
            removeHere.remove(key);
            storage[hash(key)] = removeHere;
            n--;
            return true;
        }
        return false;
    }

    public int size () {

	return n;

    }

    public int getNumberCollisions () {

	return collisions;

    }

    private int hash (E key) {

	return (key.hashCode() % cap);
	
    }

}
