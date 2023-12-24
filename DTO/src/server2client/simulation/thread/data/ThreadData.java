package server2client.simulation.thread.data;

public class ThreadData {
    private final int queueCount;
    private final int runningCount;
    private final int completedCount;

    public ThreadData(int queueCount, int runningCount, int completedCount) {
        this.queueCount = queueCount;
        this.runningCount = runningCount;
        this.completedCount = completedCount;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }
}
