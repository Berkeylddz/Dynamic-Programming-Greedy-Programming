import java.util.ArrayList;

public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }

    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){
        ArrayList<Integer> solution = new ArrayList<>();
        ArrayList<ArrayList<Integer>> HOURS = new ArrayList<>();

        // Initialize solution and HOURS for the base case
        solution.add(0);
        HOURS.add(new ArrayList<>());

        int energyPerHour = amountOfEnergyDemandsArrivingPerHour.size();
        for (int j = 1; j <= energyPerHour; j++) {
            int maxSollution = Integer.MIN_VALUE;
            int bestHour = -1;

            for (int i = 0; i < j; i++) {
                int potentialSolution = solution.get(i) + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), calculateEfficiency(j - i));

                if (potentialSolution > maxSollution) {
                    maxSollution = potentialSolution;
                    bestHour = i;
                }
            }

            solution.add(maxSollution);
            ArrayList<Integer> hours1 = new ArrayList<>(HOURS.get(bestHour));
            hours1.add(j);
            HOURS.add(hours1);
        }

        return new OptimalPowerGridSolution(solution.get(energyPerHour), HOURS.get(energyPerHour));
    }

    private int calculateEfficiency(int hours) {
        return hours * hours;
    }
}
