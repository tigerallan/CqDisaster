package com.nandi.cqdisaster.examine.bean;

/**
 * Created by ChenPeng on 2017/8/28.
 */

public class MyCompleteTask {
    private String taskName;
    private String taskId;

    public MyCompleteTask(String taskName, String taskId) {
        this.taskName = taskName;
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
