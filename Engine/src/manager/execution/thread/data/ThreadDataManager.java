package manager.execution.thread.data;

import server2client.simulation.thread.data.ThreadData;

public class ThreadDataManager {
    private int queueCount;
    private int runningCount;
    private int finishedCount;
    private int updateVersion;

    public ThreadDataManager() {
        queueCount = 0;
        runningCount = 0;
        finishedCount = 0;
        updateVersion = 0;
    }

    public void queueCountIncrement() {
        queueCount++;
        updateVersion++;
    }

    public void runningCountIncrement() {
        runningCount++;
        updateVersion++;
    }

    public void finishedCountIncrement() {
        finishedCount++;
        updateVersion++;
    }

    public void runningCountDecrement() {
        runningCount--;
        updateVersion++;
    }

    public void queueCountDecrement() {
        queueCount--;
        updateVersion++;
    }

    public int getUpdateVersion() {
        return updateVersion;
    }

    public ThreadData getThreadData() {
        return new ThreadData(queueCount, runningCount, finishedCount);
    }
}
