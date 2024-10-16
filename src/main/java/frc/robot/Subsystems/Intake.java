package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Constant.IntakeConstants;

public class Intake extends SubsystemBase{
    private VictorSPX intakeTop = new VictorSPX(IntakeConstants.TopMotorCanId);
    private VictorSPX intakeBot = new VictorSPX(IntakeConstants.BottomBeltsMotorCanId);
    private VictorSPX topSecondary = new VictorSPX(IntakeConstants.TopSecondaryCanId);
    
    public Intake(){
        //restore all settings
        intakeTop.configAllSettings(new VictorSPXConfiguration());
        intakeBot.configAllSettings(new VictorSPXConfiguration());
        topSecondary.configAllSettings(new VictorSPXConfiguration());

        intakeTop.setNeutralMode(NeutralMode.Brake);
        intakeBot.setNeutralMode(NeutralMode.Brake);
        topSecondary.setNeutralMode(NeutralMode.Brake);

        intakeTop.setInverted(false);
        topSecondary.setInverted(false);
    }

    public void RunIntake(double topspeed, double bottomspeed)
    {
        intakeTop.set(VictorSPXControlMode.PercentOutput, topspeed);
        intakeBot.set(VictorSPXControlMode.PercentOutput, bottomspeed);
    }

    public void RunTopIntake(double speed)
    {
        topSecondary.set(VictorSPXControlMode.PercentOutput, speed);
    }
}
