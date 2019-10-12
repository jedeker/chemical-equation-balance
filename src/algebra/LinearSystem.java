package algebra;

import java.util.*;

public class LinearSystem extends ArrayList<Equation> {

    public LinearSystem() {
        super();
    }

    public LinearSystem(Collection<Equation> c) {
        super(c);
    }

    public List<LinearSystem> consolidate() {
        List<LinearSystem> list = new ArrayList<>();
        List<Equation> copy = new ArrayList<>(this);
        while (copy.size() > 1) {
            LinearSystem s = new LinearSystem();
            s.add(copy.remove(0));
            int i = 0;
            while (i < s.size()) {
                Iterator<Equation> iterator = copy.iterator();
                while (iterator.hasNext()) {
                    Equation e = iterator.next();
                    for (int j : s.get(i).variables()) {
                        if (e.contains(j)) {
                            s.add(e);
                            iterator.remove();
                            break;
                        }
                    }
                }
                i++;
            }
            list.add(s);
        }
        if (copy.size() == 1)
            list.add(new LinearSystem(Collections.singletonList(copy.get(0))));
        return list;
    }
    
    public Map<Integer, Integer> solve() {
        List<Equation> current = new ArrayList<>(this);
        List<Equation> pending = new ArrayList<>();
        int common = -1;
        while(current.size() > 0) {
            int minVariables = -1, minIndex = 0;
            for (int i = 0; i < current.size(); i++) {
                int variables = current.get(i).variables().size();
                if (minVariables < 0 || variables < minVariables) {
                    minVariables = variables;
                    minIndex = i;
                }
            }
            Equation equation = current.remove(minIndex);
            int variable = equation.variables().get(0);
            Equation solved = equation.solveFor(variable);
            ListIterator<Equation> pendingIterator = pending.listIterator();
            while (pendingIterator.hasNext()) {
                Equation e = pendingIterator.next();
                if (e.contains(variable))
                    pendingIterator.set(e.substitute(solved));
            }
            ListIterator<Equation> currentIterator = current.listIterator();
            while (currentIterator.hasNext()) {
                Equation e = currentIterator.next();
                if (e.contains(variable))
                    currentIterator.set(e.substitute(solved));
            }
            if (current.size() == 1 && current.get(0).isEmpty())
                current.remove(0);
            if (current.size() > 0) {
                pending.add(solved);
            } else {
                common = solved.variables().get(1);
                pending.add(solved.solveFor(common));
            }
        }
        int lcm = pending.stream()
                .mapToInt(e -> e.getRightExpression().get(0).getCoefficient())
                .reduce(MathUtils::lcm)
                .getAsInt();
        Map<Integer, Integer> solutions = new HashMap<>();
        solutions.put(common, lcm);
        for (Equation e : pending)
            solutions.put(e.getRightExpression().get(0).getVariable(), lcm / e.getRightExpression().get(0).getCoefficient() * e.getLeftExpression().get(0).getCoefficient());
        return solutions;
    }
}
