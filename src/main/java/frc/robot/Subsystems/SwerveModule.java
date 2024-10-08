package frc.robot.Subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Subsystems.Constant.DebugLevel;
import frc.robot.Subsystems.Constant.DebugSetting;
import frc.robot.Subsystems.Constant.DriveConstants;
import frc.robot.Util.CTREModuleState;
import frc.robot.Util.Conversions;

public class SwerveModule {

    // Offset
    private double offset = 0 ;

    // Decleration of swerve components
    private CANSparkMax turn;
    private CANSparkMax drive;
    private SparkPIDController turn_pid;
    private CANcoder canCoder;
    private CANcoderConfiguration config;
    //simulation objects
    /*private CANCoderSimCollection canCoderSim;
    private TalonFXSimCollection turnSim;
    private TalonFXSimCollection driveSim;*/

    private DriveTrainInterface driveTrainParent;

    private SwerveModuleState state;

    // Constants for angle setting of swerve
    private double m = -5;
    private double b = 4;
    private double maxturn = 0.5;
    private double dif = 0;

    // Desired speed and angle values for swerve
    private double driveSpeed = 0;//1 = max speed.
    private double driveAngle = 0;

    // Limit for maximum drive speed
    private double maxdrive = 0.3;

    // isInverted is used when the wheel is facing the 
    // opposite direction and should run backwards instead
    private double isInverted = 1;

    // This value is used for whether or not the optimal path
    // to the desired angle loops around, or goes through 0
    private boolean loopAround = false;

    // Requested angle from main
    private double requestedAngle = 0;

    private double canCoderModified = 0;

    private PIDController turnPID;
    private int turnID = 0;
    public String Name;
    private String canCoderName;

    // Takes ID's of swerve components when called.
    public SwerveModule(int turnMotor, int driveMotor, int canCoderID, double off, String name, DriveTrainInterface driveTr) {
        Name = name;
        // Set the offset
        offset = off;
        turnID = turnMotor;
        driveTrainParent = driveTr;

        // IDs for cancoder and falcons.
        turn = new CANSparkMax(turnMotor, MotorType.kBrushless);

        drive = new CANSparkMax(driveMotor, MotorType.kBrushless);
        turn.setInverted(false);
        drive.setInverted(false);

        turn_pid = turn.getPIDController();
        canCoder = new CANcoder(canCoderID);
        canCoderName = name + canCoderID;
        
        drive.setIdleMode(IdleMode.kBrake);
        turn.restoreFactoryDefaults();
        turn.setIdleMode(IdleMode.kBrake);
        //turn.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        turn_pid.setP(.08);
        turn_pid.setI(0);
        turn_pid.setD(0);
        //turn.config_kP(0, 0.08);
        //turn.config_kI(0, 0);
        //turn.config_kD(0, 0);
        turn.enableSoftLimit(SoftLimitDirection.kForward, false);
        turn.enableSoftLimit(SoftLimitDirection.kReverse, false);
        drive.enableVoltageCompensation(DriveConstants.kNominalVoltage);
        turn.enableVoltageCompensation(DriveConstants.kNominalVoltage);
        drive.setSmartCurrentLimit(DriveConstants.kDriveCurrentLimit);
        turn.setSmartCurrentLimit(DriveConstants.kSteerCurrentLimit);
        resetTurnToAbsolute();
        // cancoder settings.
        // config = new CANcoderConfiguration();
        // config.sensorCoefficient = 2 * 3.14 / 4096.0;
        // config.unitString = "rad";
        // config.sensorTimeBase = SensorTimeBase.PerSecond;
        // canCoder.configAllSettings(config);

    }

    // Run this in periodic for all swerves. It just keeps stuff up to speed.
    public void update()  {
        // // Check for loop around
        // loopAround = (Math.abs(canCoder.getAbsolutePosition() - requestedAngle) > (3 * Math.PI / 2)); 

        // // Cancoder variable to help with loop around.
        // canCoderModified = canCoder.getAbsolutePosition();
        // canCoderModified -= offset;
        // if(canCoderModified < 0) {
        //     canCoderModified += 2 * Math.PI;
        // }
        // if(loopAround) {
        //     if(canCoderModified > Math.PI) {
        //         canCoderModified -= 2 * Math.PI;
        //     }
        //     if(requestedAngle > Math.PI) {
        //         requestedAngle -= 2 * Math.PI;
        //     }
        // }
        

        // If the wheel is facing the other direction, invert speed
        // if(Math.abs(canCoderModified - requestedAngle) > Math.PI / 2) {
        //     isInverted = -1;
        // } else {
        //     isInverted = 1;
        // }

        // Set the speed
        drive.set(maxdrive * isInverted * driveSpeed);

        // // Calculate dif
        // dif = canCoderModified - requestedAngle;

        // // Protect against 0/0
        // if(dif == 0) {
        //     dif = 0.0001;
        // }
        // turn.set(ControlMode.Position, (requestedAngle) / (Math.PI * 4) * 4096 * 150 / 7);
        double dashboardTurnSetpoint = SmartDashboard.getNumber("turn", 0);
        turn_pid.setReference(dashboardTurnSetpoint, ControlType.kPosition);
        SmartDashboard.putNumber("turn", dashboardTurnSetpoint);
              // Set the turn
        // turn.set(maxturn * dif / Math.abs(dif) / (1 + Math.pow(Math.E, (m * Math.abs(dif) + b))));
       // System.out.println(turnID);
        //System.out.println((requestedAngle) / (Math.PI * 4) * 4096 * 150 / 7);
        
    }

    // Takes x, y vector on desired robot direction
    public void setCartesian(double x, double y) {

        // Calculate the requested angle
        requestedAngle = Math.atan2(y, x);

        // Set the speed
        driveSpeed = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        
    }

    public void setPolar(double ang, double spe) {

        // Calculate the requested angle
        requestedAngle = ang;

        // Set the speed
        driveSpeed = spe;

    }

    // public void ZeroDriveSensor() {
    //      // drive.setSelectedSensorPosition(0);
    // }

    private void resetTurnToAbsolute(){
        //get the absolute encoder position and subtract the starting offset, to be used to reset the encoder so it knows where we are
        double absPosition = Conversions.radiansToFalcon(canCoder.getPosition().getValue() * Conversions.twoPi - offset);
        if (DebugSetting.TraceLevel == DebugLevel.Swerve || DebugSetting.TraceLevel == DebugLevel.All){
            SmartDashboard.putNumber(Name + "Posn abs", absPosition);
        }
        //negate the position. The cancoder increases while the motor encoder decreases
        //turn.setSelectedSensorPosition(-absPosition);
        turn.getEncoder().setPosition(-absPosition);
    }

    public void simulateInit()
    {
        /*canCoderSim = new CANCoderSimCollection(canCoder);
        turnSim = new TalonFXSimCollection(turn);
        driveSim = new TalonFXSimCollection(drive);*/
    }

    public void simulatePeriodic()
    {
        /*double battryVoltage = RobotController.getBatteryVoltage();
        canCoderSim.setBusVoltage(battryVoltage);
        canCoderSim.setRawPosition((int)turn.getSelectedSensorPosition());

        turnSim.setBusVoltage(battryVoltage);
        turnSim.setIntegratedSensorRawPosition((int)Conversions.degreesToFalcon(driveAngle));
        driveSim.setBusVoltage(battryVoltage);
        driveSim.setIntegratedSensorVelocity((int)(driveSpeed * 2048 / 600));*/
    }

    //Gets the current state of the robot based on the specified gyro angle and the last speed setpoint
    public SwerveModuleState getState(){
        //double velocity = Conversions.falconToMPS(drive.getSelectedSensorVelocity());
        double velocity = drive.get();
        Rotation2d angle = Rotation2d.fromDegrees(Conversions.falconToDegrees(-turn.getEncoder().getPosition()));
        return new SwerveModuleState(velocity, angle);
    }

    // TODO this is dumb and bad and it needs some UPDATINE!!!
    public SwerveModulePosition getPosition(){
        double position = Conversions.falconToMeters(drive.getEncoder().getPosition());
        Rotation2d angle = Rotation2d.fromDegrees(Conversions.falconToDegrees(-turn.getEncoder().getPosition()));
        return new SwerveModulePosition(position, angle);
    }

    public void setDesiredState(SwerveModuleState desiredState){
        desiredState = CTREModuleState.optimize(desiredState, getState().angle);
        driveSpeed = desiredState.speedMetersPerSecond / DriveConstants.maxRobotSpeedmps;
        if (DebugSetting.TraceLevel == DebugLevel.Swerve || DebugSetting.TraceLevel == DebugLevel.All){
            SmartDashboard.putNumber(Name + " DriveRef", driveSpeed);
        }
        drive.set(driveSpeed);

        //if desired speed is less than 1 percent, keep the angle where it was to prevent jittering
        double angle = (Math.abs(desiredState.speedMetersPerSecond) <= (DriveConstants.maxRobotSpeedmps * 0.01)) ? driveAngle : desiredState.angle.getRadians();
        if (DebugSetting.TraceLevel == DebugLevel.Swerve || DebugSetting.TraceLevel == DebugLevel.All){
            SmartDashboard.putNumber(Name + " TurnRef", Units.radiansToDegrees(angle));
        }
        // turn.set(ControlMode.Position, -Conversions.radiansToFalcon(angle));
        turn_pid.setReference(-Conversions.radiansToFalcon(angle), ControlType.kPosition);
        driveAngle = angle;

    }

    public void periodic(){
        // if (DebugSetting.TraceLevel == DebugLevel.Swerve || DebugSetting.TraceLevel == DebugLevel.All){
        //     SmartDashboard.putNumber("Pos FB " + Name, Units.radiansToDegrees(Conversions.falconToRadians(turn.getEncoder().getPosition())));
        //     SmartDashboard.putNumber(canCoderName,  Units.radiansToDegrees(canCoder.getAbsolutePosition() - offset));
        //     SmartDashboard.putNumber("Drive FB " + Name, drive.getSelectedSensorPosition());
        // }
    }
  }
