package frc.robot.subsys;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Ports;

public class Launcher {

    private static CANSparkMax launcher;
    private static Launcher instance;
    private LauncherState state;


    public enum LauncherState
    {
        SHOOT(1.0),
        OFF(0.0),
        INTAKE(-1.0);

        public final double power;

        private LauncherState(double power)
        {
            this.power = power;
        }
    }

    public Launcher()
    {
        launcher = new CANSparkMax(Ports.LAUNCHER, MotorType.kBrushed);
        launcher.setInverted(false);
        launcher.setOpenLoopRampRate(0);
        launcher.setClosedLoopRampRate(0);
        launcher.setIdleMode(IdleMode.kBrake);
        launcher.burnFlash();
    }

    public void setState(LauncherState state) {
        this.state = state;
    }

    public void updateState(boolean launchbutton, boolean intakebutton)
    {
        if(launchbutton && !intakebutton)
        {
            setState(LauncherState.SHOOT);
        }
        if(intakebutton && !launchbutton)
        {
            setState(LauncherState.INTAKE);
        }
        else setState(LauncherState.OFF);

        launcher.set(state.power);

        SmartDashboard.putNumber("LAUNCHER VOLTS", launcher.getBusVoltage());

    }

    public static Launcher getInstance() {
        if (instance == null) {
            instance = new Launcher();
        }
        return instance;

}
}
