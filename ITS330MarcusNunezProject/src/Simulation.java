import java.util.*;

public class Simulation {

    static final int QUANTUM = 2;
    static final int IO_DURATION = 2;

    public static void main(String[] args) {

        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> waitingList = new ArrayList<>();
        List<Process> newList = new ArrayList<>();

        //create processes
        newList.add(new Process("P1", 5, 2));
        newList.add(new Process("P2", 4, 1));
        newList.add(new Process("P3", 3, -1)); //no I/O

        int time = 0;

        //NEW > READY
        for (Process p : newList) {
            p.state = "NEW";
            log(time, p.name + " created (NEW)");
        }

        for (Process p : newList) {
            p.state = "READY";
            readyQueue.add(p);
            log(time, p.name + " NEW > READY");
        }

        //I/O Interupt
        while (!readyQueue.isEmpty() || !waitingList.isEmpty()) {
            Iterator<Process> it = waitingList.iterator();
            while (it.hasNext()) {
                Process p = it.next();
                if (time - p.ioStartTime >= IO_DURATION) {
                    p.state = "READY";
                    readyQueue.add(p);
                    log(time, "[I/O INTERRUPT] " + p.name + " WAITING > READY");
                    it.remove();
                }
            }

            //idle
            if (readyQueue.isEmpty()) {
                log(time, "CPU IDLE");
                time++;
                continue;
            }

            //Start process
            Process current = readyQueue.poll();
            current.state = "RUNNING";
            log(time, current.name + " READY > RUNNING");

            int runTime = Math.min(QUANTUM, current.remainingTime);

            //IO request > waiting
            for (int t = 0; t < runTime; t++) {
                time++;
                current.remainingTime--;
                if (current.ioRequestPoint == current.executedTime && !current.ioDone) {
                    current.state = "WAITING";
                    current.ioStartTime = time;
                    current.ioDone = true;
                    waitingList.add(current);

                    log(time, current.name + " RUNNING > WAITING (I/O request)");
                    break;
                }

                current.executedTime++;

                //complete process
                if (current.remainingTime == 0) {
                    current.state = "TERMINATED";
                    log(time, current.name + " RUNNING > TERMINATED");
                    break;
                }
            }

            //timer interupt
            if (current.state.equals("RUNNING")) {
                current.state = "READY";
                readyQueue.add(current);
                log(time, "[TIMER INTERRUPT] " + current.name + " RUNNING > READY");
            }
        }

        System.out.println("\nSimulation complete.");
    }

    //log time of event
    public static void log(int time, String message) {
        System.out.printf("Time %2d: %s\n", time, message);
    }
}