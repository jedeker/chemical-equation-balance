import algebra.Equation;
import algebra.LinearSystem;
import algebra.Term;

import java.util.*;

public class ChemicalEquation {

    private static <E> List<E> union(List<E> a, List<E> b) {
        List<E> list = new ArrayList<>(a);
        list.addAll(b);
        return list;
    }

    private static class Expression {

        List<Molecule> molecules;

        Expression(String x) {
            molecules = parse(x);
        }

        private static List<Molecule> parse(String expression) {
            List<Molecule> molecules = new ArrayList<>();
            for (String s : expression.split("\\+"))
                molecules.add(new Molecule(s));
            return molecules;
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(" + ");
            for (Molecule m : molecules)
                joiner.add(m.toString());
            return joiner.toString();
        }
    }

    private static class Molecule extends LinkedHashMap<String, Integer> {

        Molecule(String x) {
            super(parse(x));
        }

        private static Map<String, Integer> parse(String formula) {
            Map<String, Integer> atoms = new LinkedHashMap<>();
            List<String> list = splitMolecule(formula);
            for (int i = 0; i < list.size(); i += 2) {
                String x = list.get(i), y = list.get(i + 1);
                if ("([{".indexOf(x.charAt(0)) > -1) {
                    for (Map.Entry<String, Integer> e : parse(x.substring(1, x.length() - 1)).entrySet())
                        atoms.merge(e.getKey(), e.getValue() * Integer.parseInt(y), Integer::sum);
                } else {
                    atoms.merge(x, Integer.parseInt(y), Integer::sum);
                }
            }
            return atoms;
        }

        private static List<String> splitMolecule(String formula) {
            List<String> list = new ArrayList<>();
            Deque<Character> deque = new ArrayDeque<>();
            StringBuilder builder = new StringBuilder();
            int previous = 0;
            for (char c : formula.toCharArray()) {
                int i = "([{)]}".indexOf(c);
                if (i > -1) {
                    if (i < 3) {
                        if (previous != 3 && builder.length() > 0) {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                            if (previous != 2) list.add("1");
                        }
                        deque.add(")]}".charAt(i));
                        previous = 3;
                    } else if (deque.isEmpty() || deque.pollLast() != ")]}".charAt(i - 3)) {
                        throw new IllegalArgumentException();
                    } else if (deque.isEmpty()) previous = 0;
                } else if (deque.isEmpty()) {
                    if (Character.isUpperCase(c) && builder.length() > 0) {
                        list.add(builder.toString());
                        builder = new StringBuilder();
                        if (previous != 2) list.add("1");
                        previous = 0;
                    } else if (Character.isLowerCase(c)) {
                        if (previous == 1) throw new IllegalArgumentException();
                        previous = 1;
                    } else if (Character.isDigit(c)) {
                        if (previous != 2) {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                        }
                        previous = 2;
                    }
                }
                builder.append(c);
            }
            if (!deque.isEmpty()) throw new IllegalArgumentException();
            if (builder.length() > 0) {
                list.add(builder.toString());
                if (previous != 2) list.add("1");
            }
            return list;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Integer> e : entrySet()) {
                builder.append(e.getKey());
                if (e.getValue() > 1) builder.append(e.getValue().toString());
            }
            return builder.toString();
        }
    }

    private Expression left, right;

    public ChemicalEquation(String equation) {
        equation = equation.replaceAll("\\s+", "");
        int i = equation.indexOf('=');
        if (i < 0) throw new IllegalArgumentException();
        left = new Expression(equation.substring(0, i));
        right = new Expression(equation.substring(i + 1, equation.length()));
    }

    public void balance() {
        LinearSystem system = new LinearSystem();
        Set<String> set = new LinkedHashSet<>();
        for (Molecule m : left.molecules) set.addAll(m.keySet());
        for (Molecule m : right.molecules) set.addAll(m.keySet());
        for (String s : set) {
            List<Term> leftTerms = new ArrayList<>(), rightTerms = new ArrayList<>();
            for (int i = 0; i < left.molecules.size(); i++) {
                Molecule m = left.molecules.get(i);
                if (m.containsKey(s)) leftTerms.add(new Term(m.get(s), i));
            }
            for (int i = 0; i < right.molecules.size(); i++) {
                Molecule m = right.molecules.get(i);
                if (m.containsKey(s)) rightTerms.add(new Term(m.get(s), i + left.molecules.size()));
            }
            system.add(new Equation(leftTerms, rightTerms));
        }
        List<Molecule> molecules = union(left.molecules, right.molecules);
        for (LinearSystem s : system.consolidate()) {
            for (Map.Entry<Integer, Integer> e : s.solve().entrySet()) {
                System.out.println(e.getValue() + " x " + molecules.get(e.getKey()));
            }
        }
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }
}
