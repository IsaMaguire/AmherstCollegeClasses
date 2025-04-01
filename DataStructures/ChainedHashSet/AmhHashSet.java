public interface AmhHashSet<E> {

    public boolean insert (E key);
    public boolean lookup (E key);
    public boolean remove (E key);
    public int     size ();
    public int     getNumberCollisions ();
    
} // interface AmhHashSet
