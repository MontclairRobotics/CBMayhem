package frc.team555.Mayhem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.CounterBase;
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
import frc.team555.Mayhem.Mappers.OperatorMapper;
import frc.team555.Mayhem.Mappers.SensorMapper;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBDriveModule;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.behaviors.CBStdDriveBehavior;
import org.montclairrobotics.cyborg.controllers.CBDifferentialDriveController;
import org.montclairrobotics.cyborg.controllers.CBLiftController;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.devices.*;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;
import org.montclairrobotics.cyborg.mappers.CBMotorMonitorMapper;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBEnums;
import org.montclairrobotics.cyborg.utils.CBPIDErrorCorrection;

public class RobotCB extends Cyborg {

    private CBDeviceID pdb,


    //navx
    navx;

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
        driverControls   = new DriverControls(ha);
        operatorControls = new OperatorControls(ha);
        drivetrain       = new Drivetrain(ha, pdb);
        intake           = new Intake(ha, pdb);
        intakeLift       = new IntakeLift(ha, pdb);
        mainLift         = new MainLift(ha, pdb);

        // Run Setups functions
        SmartDashboard.putBoolean("Driver Control Setup",driverControls.setup());
        SmartDashboard.putBoolean("Operator Control Setup",operatorControls.setup());
        SmartDashboard.putBoolean("Drivetrain Setup",drivetrain.setup());
        SmartDashboard.putBoolean("Intake Setup",intake.setup());
        SmartDashboard.putBoolean("Intake Lift Setup",intakeLift.setup());
        SmartDashboard.putBoolean("Main Lift Setup", mainLift.setup());

        // Here is a hack:
        // create a second "drivetrain" to operate the intake
        // because they work the same way...
        this.addTeleOpMapper(
                new CBArcadeDriveMapper(this)
                        .setAxes(operYAxis, null, operXAxis)
                        .setRequestData(((RequestData) requestData).intake)
        );

        this.addTeleOpMapper(
                new OperatorMapper(this)
                        .setIntakeLiftDownButton(intakeLiftDownButton)
                        .setIntakeLiftUpButton(intakeLiftUpButton)
                        .setMainLiftDownButton(mainLiftDownButton)
                        .setMainLiftUpButton(mainLiftUpButton)
                        .setShootCubeButton(shootCubeButton)
        );

        // setup sensor mapper(s)
        this.addCustomMapper(
                new SensorMapper(this)
                        .setMainLiftLimits(mainLiftEncoder, mainLiftLimit, intakeLiftEncoder)
        );

        this.addCustomMapper(
                new CBMotorMonitorMapper(this)
                        .add(drivetrain.getFrontLeftMotor())
                        .add(drivetrain.getFrontRightMotor())
                        .add(drivetrain.getBackLeftMotor())
                        .add(drivetrain.getBackRightMotor())
                        .add(mainLiftMotorBack)
                        .add(mainLiftMotorFront)
                        .add(intakeLiftEncoder)
                        .add(intakeLeftMotor)
                        .add(intakeRightMotor)
        );

        // setup robot controllers
        this.addRobotController(
                new CBDifferentialDriveController(this)
                        .addLeftDriveModule(
                                new CBDriveModule(
                                        new CB2DVector(-1, 0), 0)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(dtFrontLeftMotor)
                                                        .addSpeedController(dtBackLeftMotor)
                                                        .setEncoder(dtLeftEncoder)
                                                        .setErrorCorrection(
                                                                new CBPIDErrorCorrection()
                                                                        .setConstants(new double[]{1.5, 0, 0.0015}
                                                                        )
                                                        )
                                        )
                        )
                        .addRightDriveModule(
                                new CBDriveModule(new CB2DVector(1, 0), 180)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(dtFrontRightMotor)
                                                        .addSpeedController(dtBackRightMotor)
                                                        .setEncoder(dtRightEncoder)
                                                        .setErrorCorrection(
                                                                new CBPIDErrorCorrection()
                                                                        .setConstants(new double[]{1.5, 0, 0.0015}
                                                                        )
                                                        )
                                        )
                        )
        );

        // yup again with the second drivetrain for the intake
        this.addRobotController(
                new CBDifferentialDriveController(this)
                        .addLeftDriveModule(
                                new CBDriveModule(new CB2DVector(-6, 0), 0)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(intakeLeftMotor)
                                        )
                        )
                        .addRightDriveModule(
                                new CBDriveModule(new CB2DVector(6, 0), -180)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(intakeRightMotor)
                                        )
                        )
                        .setControlData(((ControlData) controlData).intake)
        );

        // main lift controller definition
        this.addRobotController(
                // hardware configurations are done here.
                // there are other "soft" configurations done in the mapper
                // that include margins (which trigger slow motion)
                // and in this case a encoder based top limit
                new CBLiftController(this)
                        // setData allows you to pick a CBLinearControllerData variable
                        // in controlData to use for this lift. There might be several
                        // lift controllers and each one would be controlled by a different
                        // CBLinearControllerData object in controlData.
                        .setControlData(((ControlData) controlData).mainLift)
                        // set a lower limit switch this is a hard limit
                        .setBottomLimit(mainLiftLimit)
                        // set the encoder for the lift
                        .setEncoder(mainLiftEncoder)
                        // attach a speed controller array to drive the lift
                        .setSpeedControllerArray(new CBVictorArrayController()
                                .addSpeedController(mainLiftMotorFront)
                                .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );

        // intake lift controller definition
        this.addRobotController(
                // hardware configurations are done here.
                // there are other "soft" configurations done in the mapper
                // that include margins (which trigger slow motion)
                // and in this case a encoder based top limit
                new CBLiftController(this)
                        // setData allows you to pick a CBLinearControllerData variable
                        // in controlData to use for this lift. There might be several
                        // lift controllers and each one would be controlled by a different
                        // CBLinearControllerData object in controlData.
                        .setControlData(((ControlData) controlData).intakeLift)
                        // set a lower limit switch this is a hard limit
                        //.setBottomLimit(mainLiftLimit)
                        // set the encoder for the lift
                        .setEncoder(intakeLiftEncoder)
                        // attach a speed controller array to drive the lift
                        .setSpeedControllerArray(new CBVictorArrayController()
                                .addSpeedController(intakeLiftMotor)
                                .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );


        // setup behaviors
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
