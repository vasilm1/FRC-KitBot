// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsys.Drivetrain;
import frc.robot.subsys.Drivetrain.DriveSpeed;

import frc.robot.subsys.Launcher.FlickerState;
import frc.robot.subsys.Launcher.LauncherState;
import frc.robot.subsys.Roof.RoofState;
import frc.robot.subsys.Launcher;

import frc.robot.subsys.Roof;

import frc.robot.auton.sequences.*;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

private PS4Controller driver;
private PS4Controller operator;

private Drivetrain drivetrain;
private Launcher launcher;
//private Roof roof;

private static final String kDefaultAuto = "BAMP";

private BAMP bamp;

private String m_autoSelected;
private final SendableChooser<String> m_chooser = new SendableChooser<>();

Thread m_visionThread;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    drivetrain = Drivetrain.getInstance();
    launcher = Launcher.getInstance();
    //roof = Roof.getInstance();

    m_chooser.setDefaultOption("BAMP", kDefaultAuto);
    SmartDashboard.putData("AUTON SEQUENCES", m_chooser);

    m_visionThread =
    new Thread(
        () -> {
          // Get the UsbCamera from CameraServer
          UsbCamera camera = CameraServer.startAutomaticCapture("cam1", 0);

          // Set the resolution
          camera.setResolution(640, 480);

          // Get a CvSink. This will capture Mats from the camera
          CvSink cvSink = CameraServer.getVideo();
          
          // Setup a CvSource. This will send images back to the Dashboard
          CvSource outputStream = CameraServer.putVideo("Rectangle", 640, 480);

          // Mats are very memory expensive. Lets reuse this Mat.
          Mat mat = new Mat();

          // This cannot be 'true'. The program will never exit if it is. This
          // lets the robot stop this thread when restarting robot code or
          // deploying.
          while (!Thread.interrupted()) {
            // Tell the CvSink to grab a frame from the camera and put it
            // in the source mat.  If there is an error notify the output.
            if (cvSink.grabFrame(mat) == 0) {
              // Send the output the error.
              outputStream.notifyError(cvSink.getError());
              // skip the rest of the current iteration
              continue;
            }
            // Put a rectangle on the image
               Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 2);
            // Give the output stream a new image to display
            outputStream.putFrame(mat);
          }
        });
    m_visionThread.setDaemon(true);
    m_visionThread.start();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {

    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    bamp = new BAMP();

    bamp.initialize();
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kDefaultAuto:
      default:
          bamp.execute();
          break;
  }
  }

  @Override
  public void teleopInit() {
    driver = new PS4Controller(0);
    operator = new PS4Controller(1);
  }

  @Override
  public void teleopPeriodic() {

    //drive controls

    if (driver.getRawButton(Controller.PS_L1)) 
       drivetrain.setDriveSpeed(DriveSpeed.SLOW);
       else
       drivetrain.setDriveSpeed(DriveSpeed.FAST);
       

  double forward = driver.getRawAxis(Controller.PS_AXIS_LEFT_Y);
  double turn = driver.getRawAxis(Controller.PS_AXIS_LEFT_X);


  drivetrain.drive(forward, turn);

//launcher controls
                                  /*
△ - INTAKE                       
□ - Speed up Launcher            
〇 - Flick into Launcher          
✕ - Shoot into AMP
R1 & L1 - Lower and Raise the Amp roof               
                                  */

  if (operator.getRawButton(Controller.PS_SQUARE)){
    launcher.setFlickState(FlickerState.SHOOT);
  } else {
    launcher.setFlickState(FlickerState.OFF);
  }
  if (operator.getRawButton(Controller.PS_CIRCLE)){
    launcher.setLaunchState(LauncherState.SHOOT);
  } else if (operator.getRawButton(Controller.PS_TRIANGLE)){
    launcher.setFlickState(FlickerState.INTAKE);
    launcher.setLaunchState(LauncherState.INTAKE);
  }
  else if(operator.getRawButton(Controller.PS_CROSS)){
    launcher.setFlickState(FlickerState.AMP);
    launcher.setLaunchState(LauncherState.AMP);
  }
  else {
    launcher.setLaunchState(LauncherState.OFF);
  }

  launcher.updateState();

 if (operator.getRawButton(Controller.PS_R1)) {
  //roof.setState(RoofState.HIGH);
} else if (operator.getRawButton(Controller.PS_L1)) {
  //roof.setState(RoofState.LOW);
}

  //roof.update();

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
