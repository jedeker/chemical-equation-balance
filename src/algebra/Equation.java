package algebra;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Equation {

    private Expression a, b;

    public Equation(Collection<Term> leftTerms, Collection<Term> rightTerms) {
        a = new Expression(leftTerms);
        b = new Expression(rightTerms);
        simplify();
    }

    private void combine() {
        ListIterator<Term> aIterator = a.listIterator();
        while (aIterator.hasNext()) {
            Term m = aIterator.next();
            ListIterator<Term> bIterator = b.listIterator();
            while (bIterator.hasNext()) {
                Term n = bIterator.next();
                if (m.getVariable() == n.getVariable()) {
                    if (m.getCoefficient() >= n.getCoefficient()) {
                        aIterator.set(new Term(m.getCoefficient() - n.getCoefficient(), m.getVariable()));
                        bIterator.remove();
                    } else {
                        bIterator.set(new Term(n.getCoefficient() - m.getCoefficient(), n.getVariable()));
                        aIterator.remove();
                    }
                }
            }
        }
    }

    private void reduce() {
        if (a.size() > 0 && b.size() > 0) {
            int d = Stream.concat(a.stream(), b.stream())
                    .mapToInt(Term::getCoefficient)
                    .reduce(MathUtils::gcd)
                    .getAsInt();
            if (d > 1) {
                for (int i = 0; i < a.size(); i++)
                    a.set(i, new Term(a.get(i).getCoefficient() / d, a.get(i).getVariable()));
                for (int i = 0; i < b.size(); i++)
                    b.set(i, new Term(b.get(i).getCoefficient() / d, b.get(i).getVariable()));
            }
        }
    }

    private void simplify() {
        combine();
        reduce();
    }

    Expression getLeftExpression() {
        return a;
    }

    Expression getRightExpression() {
        return b;
    }

    public Equation substitute(Equation equation) {
        if (equation.a.size() != 1) throw new IllegalArgumentException();
        if (equals(equation)) return new Equation(b, a);
        Term aTerm = equation.a.get(0);
        if (a.contains(aTerm.getVariable())) {
            Term bTerm = a.get(a.indexOf(aTerm.getVariable()));
            List<Term> newA = new ArrayList<>(), newB = new ArrayList<>();
            if (bTerm.getCoefficient() != aTerm.getCoefficient()) {
                int lcm = MathUtils.lcm(bTerm.getCoefficient(), aTerm.getCoefficient());
                int k = lcm / bTerm.getCoefficient(), l  = lcm / aTerm.getCoefficient();
                for (Term t : a) {
                    if (t.getVariable() == aTerm.getVariable())
                        newA.addAll(equation.b.stream().map(e -> new Term(e.getCoefficient() * l, e.getVariable())).collect(Collectors.toList()));
                    else
                        newA.add(new Term(t.getCoefficient() * k, t.getVariable()));
                }
                for (Term t : b)
                    newB.add(new Term(t.getCoefficient() * k, t.getVariable()));
                return new Equation(newA, newB);
            } else {
                for (Term t : a) {
                    if (t.getVariable() == aTerm.getVariable())
                        newA.addAll(equation.b);
                    else
                        newA.add(t);
                }
                return new Equation(newA, b);
            }
        } else if (b.contains(aTerm.getVariable())) {
            return new Equation(b, a).substitute(equation);
        } else {
            return null;
        }
    }

    public Equation solveFor(int variable) {
        Term term;
        Expression expression;
        if (a.contains(variable)) {
            expression = new Expression(b);

            int n = a.indexOf(variable);
            for (int i = 0; i < a.size(); i++)
                if (i != n) expression.add(new Term(-a.get(i).getCoefficient(), a.get(i).getVariable()));

            term = new Term(a.get(n).getCoefficient(), variable);
        } else if (b.contains(variable)) {
            return new Equation(b, a).solveFor(variable);
        } else {
            return null;
        }
        return new Equation(new Expression(Collections.singletonList(term)), expression);
    }

    public boolean contains(int variable) {
        for (Term t : a)
            if (t.getVariable() == variable) return true;
        for (Term t : b)
            if (t.getVariable() == variable) return true;
        return false;
    }

    public List<Integer> variables() {
        List<Integer> list = new ArrayList<>();
        for (Term t : a)
            list.add(t.getVariable());
        for (Term t : b)
            list.add(t.getVariable());
        return list;
    }

    public boolean isEmpty() {
        return a.isEmpty() && b.isEmpty();
    }

    private boolean sidesEquals(List<Term> a, List<Term> b) {
        if (a.size() != b.size()) return false;
        List<Term> aTerms = new ArrayList<>(getLeftExpression()), bTerms = new ArrayList<>(getRightExpression());
        aTerms.sort(Comparator.comparingInt(Term::getVariable));
        bTerms.sort(Comparator.comparing(Term::getVariable));
        for (int i = 0; i < a.size(); i++) {
            Term t1 = aTerms.get(i), t2 = bTerms.get(i);
            if (t1.getVariable() != t2.getVariable() || t1.getCoefficient() != t2.getCoefficient()) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equation) {
            Equation e = (Equation) obj;
            return a.equals(e.a) && b.equals(e.b);
        } else {
            return this == obj;
        }
    }


    @Override
    public String toString() {
        return (a.size() > 0 ? a.toString() : "0") + " = " + (b.size() > 0 ? b.toString() : "0");
    }
}
