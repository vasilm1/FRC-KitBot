 package frc.robot.subsys;
 import com.revrobotics.CANSparkMax;
 import com.revrobotics.CANSparkMax.IdleMode;
 import com.revrobotics.CANSparkMaxLowLevel.MotorType;
 import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

 import frc.robot.Ports;

 public class Launcher {

     private static CANSparkMax launcher;
     private static CANSparkMax flicker;
     private static Launcher instance;
     private LauncherState launcherState = LauncherState.OFF;
     private FlickerState flickerState = FlickerState.OFF;



     public enum LauncherState
     {
         SHOOT(-1.0),
         OFF(0.0),
         INTAKE(0.4),
         AMP(-0.4);
         public final double power;

         private LauncherState(double power)
         {
             this.power = power;
         }
     }

     
     public enum FlickerState
     {
         SHOOT(-1.0),
         OFF(0.0),
         INTAKE(0.4),
         AMP(-0.2);

         public final double power;

         private FlickerState(double power)
         {
             this.power = power;
         }
     }

     public Launcher() {
         launcher = new CANSparkMax(Ports.LAUNCHER, MotorType.kBrushed);
         launcher.setInverted(false);
         launcher.setIdleMode(IdleMode.kBrake);
         launcher.burnFlash();

         flicker = new CANSparkMax(Ports.FLICKER, MotorType.kBrushed);
         flicker.setInverted(false);
         flicker.setIdleMode(IdleMode.kBrake);
         flicker.burnFlash();
     }

     public void setLaunchState(LauncherState state) {
         this.launcherState = state;
     }

    public void setFlickState(FlickerState state) {
         this.flickerState = state;
     }
         
    public void updateState() {
    

        launcher.set(launcherState.power);
        flicker.set(flickerState.power);

        SmartDashboard.putNumber("LAUNCHER VOLTS", launcher.getBusVoltage());
        SmartDashboard.putNumber("FLICKER VOLTS", flicker.getBusVoltage());
     }

     public static Launcher getInstance() {
         if (instance == null) {
             instance = new Launcher();
         }
         return instance;
     }
 }
