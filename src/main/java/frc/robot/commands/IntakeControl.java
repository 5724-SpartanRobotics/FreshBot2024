package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.Constant.IntakeConstants;

public class IntakeControl extends Command {
    XboxController operatorController;
    Intake intakeSubsystem;

    public IntakeControl(XboxController operator, Intake inSubsystem){
        operatorController = operator;
        intakeSubsystem = inSubsystem;
        addRequirements(inSubsystem);
    }

    @Override
    public void execute(){
        double deadband = 0.2;
        double intakespeed = operatorController.getLeftY();
        if (intakespeed < deadband && intakespeed > 0)
            intakespeed = 0;
        else if (intakespeed > -deadband && intakespeed < 0)
            intakespeed = 0;
        if (intakespeed > 0)
            intakeSubsystem.RunIntake(IntakeConstants.TopIntakePercentSpeed, IntakeConstants.BotIntakePercentSpeed);
        else if (intakespeed < 0)
            intakeSubsystem.RunIntake(-IntakeConstants.TopIntakePercentSpeed, -IntakeConstants.BotIntakePercentSpeed);
        else
            intakeSubsystem.RunIntake(0, 0);
    }
}
