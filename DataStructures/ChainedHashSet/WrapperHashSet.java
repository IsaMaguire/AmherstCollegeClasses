import java.util.HashSet;

public class WrapperHashSet<E> implements AmhHashSet<E> {

    private HashSet<E> storage;
    
    public WrapperHashSet (int capacity) {

	storage = new HashSet<E>(capacity);

    } // WrapperHashSet ()

    public boolean insert (E key) {

	return storage.add(key);
	
    } // insert ()

    public boolean lookup (E key) {

	return storage.contains(key);
	
    } // lookup ()

    public boolean remove (E key) {

	return storage.remove(key);
	
    }

    public int size () {

	return storage.size();

    }

    public int getNumberCollisions () {

	return 0;

    }

} // class WrapperHashSet
