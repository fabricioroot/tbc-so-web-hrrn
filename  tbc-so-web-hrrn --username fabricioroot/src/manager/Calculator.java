package manager;

import java.util.Vector;
import bean.Process;

/**
 *
 * @author Fabricio Reis
 */
public class Calculator {
    
    public Calculator() {
    }    

    /* This method calculates the waiting time and the turn around of the process that is going to be executed according
     * the algorithm HRRN (Highet Response Ratio Next).
     */
    public Process waitingTimeAndTurnAround (Vector<Process> processes, int position, int time) {
        Process out = new Process();
        out = processes.elementAt(position);

        // Waiting time = time (clock) - creation time
        out.setWaitingTime(time - out.getCreationTime());
        // Turn Around = waiting time + burst time
        out.setTurnAround(out.getWaitingTime() + out.getLifeTime());
        return out;
    }

    /* This method calculates the waiting times of the processes.
     */
    public Vector<Process> waitingTimes (Vector<Process> processes, int time) {
        Vector<Process> out = new Vector<Process>();
        out = processes;
        
        for(int i = 0; i <= (out.size() - 1); i++) {
            // Waiting time = time (clock) - creation time
            out.elementAt(i).setWaitingTime(time - out.elementAt(i).getCreationTime());
        }
        return out;
    }

    /* This method calculates the average of the waiting times.
     */
    public float averageWaitingTime (Vector<Process> processes) {
        float aux = 0;
        for (int i = 0; i <= (processes.size() - 1); i++) {
            aux += processes.elementAt(i).getWaitingTime();
        }
        aux = aux / processes.size();
        return aux;
    }
    
    /* This method calculates the average of the turns around.
     */
    public float averageTurnAround (Vector<Process> processes) {
        float aux = 0;
        for (int i = 0; i <= (processes.size() - 1); i++) {
            aux += processes.elementAt(i).getTurnAround();
        }
        aux = aux / processes.size();
        return aux;
    }
}