class Process {
    String name;
    String state;

    int remainingTime;
    int ioRequestPoint; //time of request
    int executedTime = 0;

    int ioStartTime = -1; //no interupt unless set
    boolean ioDone = false;

    public Process(String name, int burstTime, int ioRequestPoint) {
        this.name = name;
        this.remainingTime = burstTime;
        this.ioRequestPoint = ioRequestPoint;
        this.state = "NEW";
    }
}