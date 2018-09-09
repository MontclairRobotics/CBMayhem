package frc.team555.Mayhem;

import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team555.Mayhem.Behaviors.IntakeLiftBehavior;
import frc.team555.Mayhem.Behaviors.MainLiftBehavior;
import frc.team555.Mayhem.Controls.DriverControls;
import frc.team555.Mayhem.Controls.OperatorControls;
import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.Hardware.Drivetrain;
import frc.team555.Mayhem.Hardware.Intake;
import frc.team555.Mayhem.Hardware.IntakeLift;
import frc.team555.Mayhem.Hardware.MainLift;
import frc.team555.Mayhem.Mappers.SensorMapper;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.behaviors.CBStdDriveBehavior;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.devices.*;
import org.montclairrobotics.cyborg.mappers.CBMotorMonitorMapper;

public class RobotCB extends Cyborg {

    private CBDeviceID pdb, navx;

    DriverControls driverControls;
    OperatorControls operatorControls;
    Drivetrain drivetrain;
    Intake intake;
    IntakeLift intakeLift;
    MainLift mainLift;

    @Override
    public void cyborgInit() {

        // data init
        requestData = new RequestData();
        controlData = new ControlData();
        requestData.driveData = new CBStdDriveRequestData();
        controlData.driveData = new CBStdDriveControlData();


        // Configure Hardware Adapter and Devices
        CBHardwareAdapter ha = new CBHardwareAdapter(this);
        hardwareAdapter = ha;

        pdb = ha.add(new CBPDB());

        navx = ha.add(new CBNavX(SPI.Port.kMXP));

        // Instantiate and Initialize
        driverControls   = new DriverControls(this, ha);
        operatorControls = new OperatorControls(this, ha, requestData);
        drivetrain       = new Drivetrain(this, ha, pdb);
        intake           = new Intake(this, ha, pdb, controlData);
        intakeLift       = new IntakeLift(this, ha, pdb, controlData);
        mainLift         = new MainLift(this, ha, pdb, controlData);

        // Run Setups functions
        SmartDashboard.putBoolean("Driver Control Setup",driverControls.setup());
        SmartDashboard.putBoolean("Operator Control Setup",operatorControls.setup());
        SmartDashboard.putBoolean("Drivetrain Setup",drivetrain.setup());
        SmartDashboard.putBoolean("Intake Setup",intake.setup());
        SmartDashboard.putBoolean("Intake Lift Setup",intakeLift.setup());
        SmartDashboard.putBoolean("Main Lift Setup", mainLift.setup());


        // Sensor Mapper Setup
        this.addCustomMapper(
                new SensorMapper(this)
                        .setMainLiftLimits(mainLift.getEncoder(), mainLift.getLimitSwitch(), intakeLift.getEncoder())
        );

        // Add CB Monitor Mapper
        this.addCustomMapper(
                new CBMotorMonitorMapper(this)
                        .add(drivetrain.getFrontLeftMotor())
                        .add(drivetrain.getFrontRightMotor())
                        .add(drivetrain.getBackLeftMotor())
                        .add(drivetrain.getBackRightMotor())
                        .add(mainLift.getFrontMotor())
                        .add(mainLift.getBackMotor())
                        .add(intakeLift.getEncoder())
                        .add(intake.getLeftMotor())
                        .add(intake.getRightMotor())
        );

        // Setup Behaviors
        this.addBehavior(new CBStdDriveBehavior(this));
        this.addBehavior(new MainLiftBehavior(this));
        this.addBehavior(new IntakeLiftBehavior(this));

        // while this looks like a drivetrain, its an intake.
        this.addBehavior(
                new CBStdDriveBehavior(this)
                        .setRequestData(((RequestData) requestData).intake)
                        .setControlData(((ControlData) controlData).intake)
        );

    }

    @Override
    public void cyborgTeleopInit() {

    }

    @Override
    public void cyborgTestInit() {

    }

    @Override
    public void cyborgTestPeriodic() {

    }
}
