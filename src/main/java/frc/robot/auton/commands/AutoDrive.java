package frc.robot.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsys.Drivetrain;

public class AutoDrive extends Command {
    private Drivetrain drivetrain;

    private double time;
    private double turn;
    private double forward;
    private double endTime;

    private boolean ended = false;

    public AutoDrive(double forward, double turn, double time) {
        endTime = time;
        this.turn = turn;
        this.forward = -forward;
    }

    @Override
    public void initialize() {
        drivetrain = Drivetrain.getInstance();
        time = Timer.getFPGATimestamp();
        endTime += time;
    }

    @Override
    public void execute() 
    {
        drivetrain.drive(forward, turn);
        time = Timer.getFPGATimestamp();

        if (time >= endTime) {
            ended = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return ended;
    }
}