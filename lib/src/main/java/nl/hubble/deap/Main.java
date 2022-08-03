package nl.hubble.deap;

public class Main {
    public static void main(String[] args) {
        Deap<Integer> deap = new Deap<>();
        int space = 15;
        for (int i = 0; i < space; i++) {
            deap.add((int) Math.floor(Math.random() * 100) + 1);
        }

        deap.prettyPrint();

        int min = Integer.MAX_VALUE;
        int max = 0;
        for (Integer i : deap.toArray()) {
            if (i != null) {
                min = Math.min(i, min);
                max = Math.max(i, max);
            }
        }

        System.out.println("expected: " + min + " " + max);
        System.out.println("actual: " + deap.getLow() + " " + deap.getHigh());
    }
}
