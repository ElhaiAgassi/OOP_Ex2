package main.java.Ex2_b;


public enum TaskType {

    COMPUTATIONAL(1) {
        @Override
        public String toString() {
            return "Computational Ex2_b.Task";
        }
    },
    IO(2) {
        @Override
        public String toString() {
            return "IO-Bound Ex2_b.Task";
        }
    },
    OTHER(3) {
        @Override
        public String toString() {
            return "Unknown Ex2_b.Task";
        }
    };

    private int Priority;

    TaskType(int priority) {
        if (validatePriority(priority)) Priority = priority;
        else
            throw new IllegalArgumentException("Priority is not an integer");
    }

    public void setPriority(int priority) {
        if (validatePriority(priority)) this.Priority = priority;
        else
            throw new IllegalArgumentException("Priority is not an integer");
    }

    public int getPriorityValue() {
        return Priority;
    }

    public TaskType getType() {
        return this;
    }

    /**
     * priority is represented by an integer value, ranging from 1 to 10
     *
     * @param priority
     * @return whether the priority is valid or not
     */
    private static boolean validatePriority(int priority) {
        return priority >= 1 && priority <= 10;
    }
}