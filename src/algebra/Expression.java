package algebra;

import java.util.*;
import java.util.stream.Collectors;

public class Expression extends ArrayList<Term> {

    public Expression() {
        super();
    }

    public Expression(Collection<Term> c) {
        super(simplify(c));
    }

    private static Collection<Term> simplify(Collection<Term> terms) {
        return terms.stream()
                .collect(Collectors.toMap(Term::getVariable, Term::getCoefficient, Integer::sum, LinkedHashMap::new))
                .entrySet()
                .stream()
                .map(entry -> new Term(entry.getValue(), entry.getKey()))
                .collect(Collectors.toSet());
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

    @Override
    public String toString() {
        return stream()
                .map(Term::toString)
                .collect(Collectors.joining(" + "));
    }
}
