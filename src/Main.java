public class Main {

    public static void main(String[] args) {
        String formula = "K4Fe(CN)6 + H2SO4 + H2O = K2SO4 + FeSO4 + (NH4)2SO4 + CO";
        System.out.println(formula);
        ChemicalEquation equation = new ChemicalEquation(formula);
        System.out.println(equation);
        equation.balance();
    }
}
