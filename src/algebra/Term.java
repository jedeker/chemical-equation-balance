package algebra;

public class Term {

    private final int COEFFICIENT;
    private final int VARIABLE;

    public Term(int coefficient, int variable) {
        COEFFICIENT = coefficient;
        VARIABLE = variable;
    }

    public Term(Term t) {
        COEFFICIENT = t.COEFFICIENT;
        VARIABLE = t.VARIABLE;
    }

    public Term(Term t, int scalar) {
        COEFFICIENT = t.COEFFICIENT * scalar;
        VARIABLE = t.VARIABLE;
    }

    public int getCoefficient() {
        return COEFFICIENT;
    }

    public int getVariable() {
        return VARIABLE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Term) {
            Term t = (Term) obj;
            return VARIABLE == t.VARIABLE && COEFFICIENT == t.COEFFICIENT;
        } else {
            return this == obj;
        }
    }

    @Override
    public String toString() {
        if (COEFFICIENT == 0) return "0";
        StringBuilder builder = new StringBuilder();
        if (COEFFICIENT == -1)
            builder.append('-');
        else if (COEFFICIENT != 1 )
            builder.append(Integer.toString(COEFFICIENT));

        builder.append((char) ('a' + VARIABLE));
        return builder.toString();
    }
}
