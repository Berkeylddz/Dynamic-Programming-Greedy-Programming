import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {


        ArrayList<Integer> demandSchedule = readIntegersFromFile(args[0]);

        ArrayList<Integer> esvInfo = readIntegersFromFile(args[1]);

        int maxNumESVs = esvInfo.get(0);
        int esvCapacity = esvInfo.get(1);
        ArrayList<Integer> maintenanceTasks = new ArrayList<>();
        for (int i = 2; i < esvInfo.size(); i++) {
            maintenanceTasks.add(esvInfo.get(i));
        }

        // Mission POWER GRID OPTIMIZATION
        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(demandSchedule);
        OptimalPowerGridSolution powerGridSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();
        int totalDemand = powerGridOptimization.getAmountOfEnergyDemandsArrivingPerHour().stream().mapToInt(Integer::intValue).sum();
        System.out.println("The total number of demanded gigawatts: " + totalDemand);
        System.out.println("Maximum number of satisfied gigawatts: " + powerGridSolution.getmaxNumberOfSatisfiedDemands());
        System.out.print("Hours at which the battery bank should be discharged: ");
        ArrayList<Integer> hoursToDischarge = powerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency();
        for (int i = 0; i < hoursToDischarge.size(); i++) {
            System.out.print(hoursToDischarge.get(i));
            if (i < hoursToDischarge.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("The number of unsatisfied gigawatts: " + (totalDemand - powerGridSolution.getmaxNumberOfSatisfiedDemands()));
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        // Mission ECO-MAINTENANCE
        System.out.println("##MISSION ECO-MAINTENANCE##");
        OptimalESVDeploymentGP esvDeployment = new OptimalESVDeploymentGP(maintenanceTasks);
        int minNumESVs = esvDeployment.getMinNumESVsToDeploy(maxNumESVs, esvCapacity);
        if (minNumESVs == -1) {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        } else {
            System.out.println("The minimum number of ESVs to deploy: " + minNumESVs);
            ArrayList<ArrayList<Integer>> assignedTasks = esvDeployment.getMaintenanceTasksAssignedToESVs();
            for (int i = 0; i < minNumESVs; i++) {
                System.out.println("ESV " + (i + 1) + " tasks: " + assignedTasks.get(i));
            }
        }
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }

    private static ArrayList<Integer> readIntegersFromFile(String filename) throws IOException {
        ArrayList<Integer> integers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                for (String part : parts) {
                    integers.add(Integer.parseInt(part));
                }
            }
        }
        return integers;
    }
}
