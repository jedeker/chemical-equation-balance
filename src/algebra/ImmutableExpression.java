package algebra;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ImmutableExpression extends Expression {

    private final List<Term> l;

    public ImmutableExpression(Collection<Term> terms) {
        l = new ArrayList<>(terms);
    }

    boolean contains(int variable) {
        for (Term t : this)
            if (t.getVariable() == variable) return true;
        return false;
    }

    int indexOf(int variable) {
        for (int i = 0; i < size(); i++)
            if (get(i).getVariable() == variable) return i;
        return -1;
    }

    public int size()                 {return l.size();}
    public boolean isEmpty()          {return l.isEmpty();}
    public boolean contains(Object o) {return l.contains(o);}
    public Object[] toArray()         {return l.toArray();}
    public <T> T[] toArray(T[] a)     {return l.toArray(a);}

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" + ");
        for (Term t : this)
            joiner.add(t.toString());
        return joiner.toString();
    }

    @Override
    public Iterator<Term> iterator() {
        return new Iterator<Term>() {
            private final Iterator<Term> i = l.iterator();

            public boolean hasNext() {return i.hasNext();}
            public Term next()       {return i.next();}
            public void remove()     {throw new UnsupportedOperationException();}

            @Override
            public void forEachRemaining(Consumer<? super Term> action) {
                i.forEachRemaining(action);
            }
        };
    }

    public boolean add(Term term) {throw new UnsupportedOperationException();}
    public boolean remove(Object o) {throw new UnsupportedOperationException();}

    public boolean containsAll(Collection<?> c) {return l.containsAll(c);}
    public boolean addAll(Collection<? extends Term> c) {throw new UnsupportedOperationException(); }
    public boolean addAll(int index, Collection<? extends Term> c) {throw new UnsupportedOperationException();}
    public boolean removeAll(Collection<?> c) {throw new UnsupportedOperationException();}
    public boolean retainAll(Collection<?> c) {throw new UnsupportedOperationException();}
    public void clear() {throw new UnsupportedOperationException();}

    public Term get(int index) {return l.get(index);}
    public Term set(int index, Term element) {throw new UnsupportedOperationException();}

    public void add(int index, Term element) {throw new UnsupportedOperationException();}
    public Term remove(int index) {throw new UnsupportedOperationException();}

    public int indexOf(Object o) {return l.indexOf(o);}
    public int lastIndexOf(Object o) {return l.lastIndexOf(o);}

    public ListIterator<Term> listIterator() {return listIterator(0);}
    public ListIterator<Term> listIterator(int index) {
        return new ListIterator<Term>() {
            private final ListIterator<Term> i = l.listIterator(index);

            public boolean hasNext()     {return i.hasNext();}
            public Term next()           {return i.next();}
            public boolean hasPrevious() {return i.hasPrevious();}
            public Term previous()       {return i.previous();}
            public int nextIndex()       {return i.nextIndex();}
            public int previousIndex()   {return i.previousIndex();}
            public void remove()         {throw new UnsupportedOperationException();}
            public void set(Term term)   {throw new UnsupportedOperationException();}
            public void add(Term term)   {throw new UnsupportedOperationException();}

            @Override
            public void forEachRemaining(Consumer<? super Term> action) {
                i.forEachRemaining(action);
            }
        };
    }

    public List<Term> subList(int fromIndex, int toIndex) {return l.subList(fromIndex, toIndex);}

    // Override default methods from Collection
    @Override
    public void forEach(Consumer<? super Term> action) {l.forEach(action);}
    @Override
    public boolean removeIf(Predicate<? super Term> filter) {throw new UnsupportedOperationException();}
    @Override
    public Spliterator<Term> spliterator() {return l.spliterator();}
    @Override
    public Stream<Term> stream() {return l.stream();}
    @Override
    public Stream<Term> parallelStream() {return l.parallelStream();}
}
