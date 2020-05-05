package svarog.interactions;

public class Task {

    public enum doState{
        kill,
        collect,
        find,
        talk
    }

    private int taskID;
    private static int globalTaskID;

    doState state;

    private int howMuchIsDone = 0;
    private int toDo;
    private int doItemID;

    private boolean isEnded = false;

    public Task() {
        setTaskID(-1);

        setToDo(0);
        setDoItemID(0);
        setState(doState.kill);
        globalTaskID++;
    }

    public Task(int taskID, int toDo, int doItemID, doState state) {
        this.setTaskID(taskID);
        this.toDo = toDo;
        this.doItemID = doItemID;
        this.state = state;
        globalTaskID++;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getGlobalTaskID() {
        return globalTaskID;
    }

    public int getToDo() {
        return toDo;
    }

    public void setToDo(int toDo) {
        this.toDo = toDo;
    }

    public int getDoItemID() {
        return doItemID;
    }

    public void setDoItemID(int doItemID) {
        this.doItemID = doItemID;
    }

    public doState getState() {
        return state;
    }

    public void setState(doState state) {
        this.state = state;
    }

    public boolean getIsEnded() {
        return this.isEnded;
    }

    public void increaseHowMuchIsDone() {
        if(howMuchIsDone>=toDo) {
            this.isEnded = true;
            return;
        }
        this.howMuchIsDone++;
    }
    
    public String progress(){
        if(state == doState.kill){
            return "Kill " + String.valueOf(doItemID) + " done: "+ String.valueOf(howMuchIsDone)+"/"+String.valueOf(toDo);
        }
        if(state == doState.collect){
            return "Collect " + String.valueOf(doItemID) + " done: "+ String.valueOf(howMuchIsDone)+"/"+String.valueOf(toDo);
        }
        if(state == doState.find){
            return "Find " + String.valueOf(doItemID) + " done: "+ String.valueOf(howMuchIsDone)+"/"+String.valueOf(toDo);
        }
        if(state == doState.talk) {
        	return "Talk to " + String.valueOf(doItemID) + " done: "+ String.valueOf(howMuchIsDone)+"/"+String.valueOf(toDo);
        }
        return "Sorry our programmers don't think about that yet";
    }
}

