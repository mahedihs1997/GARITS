package home.controllers;

public class TaskTable {

    String TaskName, Duration, JobJobNumber, TaskMechanic; // String variables for Tasks in table

    public TaskTable(String taskName, String duration, String jobJobNumber, String taskMechanic) {
        //Set String variables to the parameters being passed in when called
        TaskName = taskName;
        Duration = duration;
        JobJobNumber = jobJobNumber;
        TaskMechanic = taskMechanic;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getJobJobNumber() {
        return JobJobNumber;
    }

    public void setJobJobNumber(String jobJobNumber) {
        JobJobNumber = jobJobNumber;
    }

    public String getTaskMechanic() {
        return TaskMechanic;
    }

    public void setTaskMechanic(String taskMechanic) {
        TaskMechanic = taskMechanic;
    }
}
