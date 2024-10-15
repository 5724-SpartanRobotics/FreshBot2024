// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.VisionSubsystem;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems.DriveTrainSubsystem;
import frc.robot.Subsystems.Field;
import frc.robot.Subsystems.Intake;
import frc.robot.commands.AutoCommand;
import frc.robot.commands.IntakeControl;
import frc.robot.commands.TeleopSwerve;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.cameraserver.CameraServer;


import edu.wpi.first.cscore.UsbCamera;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private double selectionPhase;
  private double poseFinder;
  private boolean wasAutoFlag;
  private Command m_autoSelected;
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();
  //private DriveTrainSubsystem drive;
  private DriveTrainSubsystem drive;
  private VisionSubsystem vision;
  private Field field = new Field(drive);


  private double dif = 0;
  // Constants for angle setting of swerve
  private double m = -5;
  private double b = 3;
  private double maxturn = 0.5;
  private Command setPos;
  
  private Timer tagTime = new Timer();

  private XboxController drivestick = new XboxController (0);
  private XboxController operator = new XboxController(1);
  private double[] dummyArray = new double[1];
  private Intake intakeSubSystem = new Intake();

  private Command _AutoCommand;



  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  
  @Override
  public void robotInit() {
    tagTime.restart();
    drive = new DriveTrainSubsystem();
    poseFinder = 0;
    CameraServer.startAutomaticCapture();
    UsbCamera cam2 = CameraServer.startAutomaticCapture();
    //System.out.println(Filesystem.getDeployDirectory());
    //if using rick's subsystem uncoment these
    drive.setDefaultCommand(new TeleopSwerve(drive, drivestick));
    // TODO this is bad and should not be here fix it its for debugging etc etc etc
    
    intakeSubSystem.setDefaultCommand(new IntakeControl(operator, intakeSubSystem));


    SmartDashboard.putNumber("setpos", 0);
    //drive.flipGyro();
    //  drive = new DriveTrainSubsystem();
    //  drive.setDefaultCommand(new RunCommand(() -> {
      
    //  }, drive));
    selectionPhase = 0;
    _AutoCommand = new AutoCommand(drive);
  //  _AutoCommand.initialize();
    m_chooser.addOption("SimpleAuto", _AutoCommand);
    SmartDashboard.putData("Auto Choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override 
  public void robotPeriodic() {
    // System.out.println("dist: " + SmartDashboard.getNumberArray("aprilTag5", dummyArray)[0]);
    // System.out.println("ang: " + SmartDashboard.getNumberArray("aprilTag5", dummyArray)[1]);
      // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    wasAutoFlag = true;
    SmartDashboard.putNumber("xSpeed", 0);
    SmartDashboard.putNumber("ySpeed", 0);
    SmartDashboard.putNumber("rotationAuto", 0);
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
    if (m_autoSelected != null)
      m_autoSelected.schedule();
//    _AutoCommand.schedule();
  }

  /** This function is called periodically 
   *  autonomous. */
  @Override
  public void autonomousPeriodic() {
    // field.setTarget(0, 0, 0);
    // field.update();
    //_AutoCommand.execute();
//    if (m_autoSelected != null)
  //    m_autoSelected.execute();
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    if(wasAutoFlag) {
      wasAutoFlag = false;
    }
    if (_AutoCommand != null)
    _AutoCommand.cancel();
  }

  /** This function is called periodically during operator control. */
    // if(drivestick.getRawButtonPressed(2)) {
      
    // }
    // if(selectionPhase == 0 && drivestick.getRawButtonPressed(8)) {
    //   poseFinder = 1;
    //   selectionPhase = 1;
    //   // TODO red vs blue
    //   vision.setTag(1);
    // } else if(selectionPhase == 0 && drivestick.getRawButtonPressed(10)) {
    //   poseFinder = 1;
    //   selectionPhase = 1;
    //   vision.setTag(2);
    // } else if(selectionPhase == 0 && drivestick.getRawButtonPressed(12)) {
    //   poseFinder = 1;
    //   selectionPhase = 1;
    //   vision.setTag(3);
    // }

    // // If we see it, set the pose
    // if(setPos != null && !setPos.isScheduled() && poseFinder != 0 && vision.foundTag() && !drivestick.getRawButton(7)) {
    //   drive.ZeroDriveSensors(vision.getSetPose());
    // }
    // if(selectionPhase == 1 && drivestick.getRawButtonPressed(8)) {
    //   setPos = new GoToAPlace(drive, new Pose2d(new Translation2d(-0.4, 0.6), new Rotation2d(3.14)), true);
    //   //System.out.println(":)");
    //   poseFinder = 0;
    //   selectionPhase = 0;
    // } else if(selectionPhase == 1 && drivestick.getRawButtonPressed(10)) {
    //   setPos = new GoToAPlace(drive, new Pose2d(new Translation2d(-0.4, 0), new Rotation2d(3.14)), true);
    //   //System.out.println(":)");
    //   poseFinder = 0;
    //   selectionPhase = 0;
    // } else if(selectionPhase == 1 && drivestick.getRawButtonPressed(12)) {
    //   setPos = new GoToAPlace(drive, new Pose2d(new Translation2d(-0.4, -0.7), new Rotation2d(3.14)), true);
    //   //System.out.println(":)");
    //   poseFinder = 0;
    //   selectionPhase = 0;
    // }

    // if(drivestick.getRawButtonPressed(7) && setPos != null) {
    //   setPos.schedule();
    // }
    // // Cancel on button 
    // if(drivestick.getRawButtonPressed(9)) {
    //   if(setPos != null) {
    //     setPos.cancel();
    //   }
    //   poseFinder = 0;
    //   selectionPhase = 0;
    // }
  

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    drive.simulationInit();
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
// if push input A them move left test