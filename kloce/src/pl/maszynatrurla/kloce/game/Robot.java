package pl.maszynatrurla.kloce.game;

public interface Robot
{
    public void go() throws RobotOffCourseException, RobotObjectiveAccomplishedEvent;
    public void left();
    public void right();
    public short getInputValue();
    public void setOutputValue(short value);
}
