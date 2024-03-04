package frc.robot.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsys.Launcher;
import frc.robot.subsys.Launcher.FlickerState;
import frc.robot.subsys.Launcher.LauncherState;

public class AutoSpeaker extends Command {
    private Launcher launcher;

    private double endTime = 6;
    private double time;

    private boolean ended = false;


    public AutoSpeaker(){
    }

    @Override
    public void initialize()
    {
        launcher = Launcher.getInstance();
        time = Timer.getFPGATimestamp();
        endTime+= time;
    }
    
    @Override
    public void execute()
    {
        
        launcher.setFlickState(FlickerState.SHOOT);
        launcher.updateState();
        time = Timer.getFPGATimestamp();
        if(endTime-2<Timer.getFPGATimestamp()){
            launcher.setLaunchState(LauncherState.SHOOT);
            launcher.updateState();
        }
        time = Timer.getFPGATimestamp();
        
        if (time >= endTime) {
            launcher.setFlickState(FlickerState.OFF);
            launcher.setLaunchState(LauncherState.OFF);
            launcher.updateState();
            ended = true;
        }
        
    }

    @Override
    public void end(boolean interrupted) {
        launcher.setFlickState(FlickerState.OFF);
        launcher.setLaunchState(LauncherState.OFF);
        launcher.updateState();
    }

    @Override
    public boolean isFinished() {
        return ended;
    }

}
