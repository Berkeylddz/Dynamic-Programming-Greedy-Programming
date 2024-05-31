import java.util.ArrayList;
import java.util.Collections;

public class OptimalESVDeploymentGP {
    private ArrayList<Integer> maintenanceTaskEnergyDemands;
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs;

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
        this.maintenanceTasksAssignedToESVs = new ArrayList<>();
    }

    public int getMinNumESVsToDeploy(int maxNumAvailableESVs, int ESV_CAPACITY) {
        // Sort maintenance tasks by their energy requirements in decreasing order
        Collections.sort(maintenanceTaskEnergyDemands, Collections.reverseOrder());

        // Initialize remaining energy capacities in all available ESVs
        ArrayList<Integer> remainESV = new ArrayList<>();
        for (int i = 0; i < maxNumAvailableESVs; i++) {
            remainESV.add(ESV_CAPACITY);
        }

        // Iterate through all maintenance tasks
        for (int i1 : maintenanceTaskEnergyDemands) {
            boolean taskAssigned = false;

            // Find the first ESV that can accommodate the task
            for (int i = 0; i < maxNumAvailableESVs; i++) {
                if (remainESV.get(i) >= i1) {
                    // Assign task to this ESV
                    if (maintenanceTasksAssignedToESVs.size() <= i) {
                        maintenanceTasksAssignedToESVs.add(new ArrayList<>());
                    }
                    maintenanceTasksAssignedToESVs.get(i).add(i1);
                    remainESV.set(i, remainESV.get(i) - i1);
                    taskAssigned = true;
                    break;
                }
            }

            // Get a new ESV only if the task does not fit in any of the already used ESVs
            if (!taskAssigned) {
                if (remainESV.size() < maxNumAvailableESVs) {
                    remainESV.add(ESV_CAPACITY - i1);
                    ArrayList<Integer> newESVTasks = new ArrayList<>();
                    newESVTasks.add(i1);
                    maintenanceTasksAssignedToESVs.add(newESVTasks);
                    taskAssigned = true;
                }
            }

            // If task could not be assigned, return -1
            if (!taskAssigned) {
                return -1;
            }
        }

        // Return the minimum number of ESVs needed
        return maintenanceTasksAssignedToESVs.size();
    }

    public ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }
}
