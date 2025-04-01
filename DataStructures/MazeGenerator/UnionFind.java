public class UnionFind {
    public void makeset(Cell A){
        // Takes a cell as an input and returns a new set with only cell A
        
        LLAddOnly list = new LLAddOnly();
        list.add(A);
    }

    public LLAddOnly find(Cell A){
        // Returns A.head, which is a LLAddOnly object

        LLAddOnly list = A.head;
        return list;
    }

    public void union(Cell A, Cell B){
        // Join the sets that contain A and B together by adding 1 cell from B at a time to A
        
        Cell x = B.head.first;
        Cell temp = x;
        while (x != null){
            temp = x.next;
            A.head.add(x);
            x = temp;
        }
    }

    public int size(LLAddOnly list){
        // Linear traversal of a linked list to return the size
        Cell curr = list.first;
        int counter = 0;

        while(curr != null){
            counter+=1;
            curr = curr.next;
        }
        return counter;
    }
}
