package frc.robot.auton.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auton.commands.*;

public class BAMP extends SequentialCommandGroup {
    
    public BAMP()
    {
        addCommands(
        new AutoDrive(.5, 0, 1.2),
        new AutoDrive(0, 1, .56),
        new AutoDrive(.25, -.09, 1.2),
        new AutoAMP()
        );
    }
}
