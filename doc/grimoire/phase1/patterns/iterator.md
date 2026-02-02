# Iterator Pattern

> Traverse a collection without exposing its internal structure.

## Problem
Clients need to iterate over `MyArrayList` and `MyLinkedList` without knowing their internal representation (array vs nodes).

## Solution

```
<<interface>>
Iterable<T>
  + iterator(): Iterator<T>
        △
        │
   ┌────┴────┐
   │         │
MyArrayList  MyLinkedList
   │              │
   └──────┬───────┘
          │
     Inner class:
     ListIterator<T>
       + hasNext(): boolean
       + next(): T
       + remove(): void
```

## Implementation

```java
public class MyArrayList<T> implements Iterable<T> {
    private Object[] data;
    private int size;
    private int modCount;  // For fail-fast
    
    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }
    
    private class ArrayListIterator implements Iterator<T> {
        private int cursor = 0;
        private int expectedModCount = modCount;
        
        @Override
        public boolean hasNext() {
            return cursor < size;
        }
        
        @Override
        public T next() {
            checkForComodification();
            return (T) data[cursor++];
        }
        
        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
```

## Fail-Fast Behavior

Detects concurrent modification during iteration:

```java
MyList<String> list = new MyArrayList<>();
list.add("A");
list.add("B");

for (String s : list) {
    list.add("C");  // Throws ConcurrentModificationException!
}
```

## Benefits
- **Uniform traversal**: Same `for-each` syntax for all collections
- **Encapsulation**: Internal structure hidden from clients
- **Multiple iterators**: Each `iterator()` call returns fresh iterator

## Used In
- [MyArrayList](../../../../src/main/java/com/omni/core/list/MyArrayList.java)
- [MyLinkedList](../../../../src/main/java/com/omni/core/list/MyLinkedList.java)
