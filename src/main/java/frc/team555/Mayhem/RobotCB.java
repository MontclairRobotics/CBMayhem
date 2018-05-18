package frc.team555.Mayhem;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SPI;
import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.UserControls.DriverControls;
import frc.team555.Mayhem.UserControls.OperatorControls;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBDriveModule;
import org.montclairrobotics.cyborg.assemblies.CBSrxArrayController;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.behaviors.CBStdDriveBehavior;
import org.montclairrobotics.cyborg.controllers.CBDifferentialDriveController;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.devices.*;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBEnums;
import org.montclairrobotics.cyborg.utils.CBErrorCorrection;
import org.montclairrobotics.cyborg.utils.CBPIDErrorCorrection;

public class RobotCB extends Cyborg {

    private CBDeviceID

        //navx
        navx,

        // dt Motors
        dtFrontLeftMotor,
        dtFrontRightMotor,
        dtBackLeftMotor,
        dtBackRightMotor,

        // dt Encoders
        dtLeftEncoder,
        dtRightEncoder,

        //lift Motors
        mainLiftMotorFront,
        mainLiftMotorBack,
        intakeLiftMotor,

        // lift encoders
        mainLiftEncoder,
        intakeLiftEncoder,

        // intake motors
        intakeLeftMotor,
        intakeRightMotor;

    @Override
    public void cyborgInit() {

        // Configure Hardware Adapter
        Cyborg.hardwareAdapter = new CBHardwareAdapter(this).setJoystickCount(3);
        CBHardwareAdapter hardwareAdapter = Cyborg.hardwareAdapter;

        // data init
        // TODO: the following two lines added.
        requestData = new RequestData();
        controlData = new ControlData();
        requestData.driveData = new CBStdDriveRequestData();
        controlData.driveData = new CBStdDriveControlData();

        // setup navx
        navx = hardwareAdapter.add(new CBNavX(SPI.Port.kMXP));

        // setup dt Motors
        dtFrontLeftMotor  = hardwareAdapter.add(new CBTalonSRX(1));
        dtFrontRightMotor = hardwareAdapter.add(new CBTalonSRX(7));
        dtBackLeftMotor   = hardwareAdapter.add(new CBTalonSRX(3));
        dtBackRightMotor  = hardwareAdapter.add(new CBTalonSRX(8));

        // setup dt encoders
        final double inchesPerTick = 96/4499;
        dtLeftEncoder  = hardwareAdapter.add(new CBEncoder(1, 0, CounterBase.EncodingType.k4X, false, inchesPerTick));
        dtRightEncoder = hardwareAdapter.add(new CBEncoder(3, 2, CounterBase.EncodingType.k4X, false, inchesPerTick));

        // setup lift motors
        mainLiftMotorFront = hardwareAdapter.add(new CBTalonSRX(4));
        mainLiftMotorBack  = hardwareAdapter.add(new CBTalonSRX(2));
        intakeLiftMotor    = hardwareAdapter.add(new CBTalonSRX(9));

        // setup lift encoders
        mainLiftEncoder = hardwareAdapter.add(new CBEncoder(4, 5, CounterBase.EncodingType.k4X, false, 0)); //TODO: Get Distance Per Pulse
//        intakeLiftEncoder = hardwareAdapter.add(new CBEncoder(, , , , )) //TODO: FIX TALON ENCODER

        // setup intake motors
        intakeLeftMotor = hardwareAdapter.add(new CBTalonSRX(10));
        intakeRightMotor = hardwareAdapter.add(new CBTalonSRX(5));

        // setup PIDs
        CBErrorCorrection dtLeftPID = new CBPIDErrorCorrection().setConstants(new double[]{1.5, 0, 0.0015});
        CBErrorCorrection dtRightPID = new CBPIDErrorCorrection().setConstants(new double[]{1.5, 0, 0.0015});

        // setup drive modules //TODO: Test Vector and Orientation
        CBDriveModule dtLeftModule  = new CBDriveModule(new CB2DVector(-1, 0), 0)
                // TODO: CBSrxArrayController has no implementation yet so change to
                // CBVictorArrayController and change mode to Power for simplicity
                .addSpeedControllerArray(new CBVictorArrayController()
                        .setDriveMode(CBEnums.CBDriveMode.Power)
                        .addSpeedController(dtFrontLeftMotor)
                        .addSpeedController(dtBackLeftMotor)
                        .setEncoder(dtLeftEncoder)
                        .setErrorCorrection(dtLeftPID));

        // TODO: change orientation since natural "forward" on the right side goes 180 away from the front of the robot
        CBDriveModule dtRightModule  = new CBDriveModule(new CB2DVector(1, 0), 180)
                // TODO: CBSrxArrayController has no implementation yet so change to
                // CBVictorArrayController and change mode to Power for simplicity
                .addSpeedControllerArray(new CBVictorArrayController()
                        .setDriveMode(CBEnums.CBDriveMode.Power)
                        .addSpeedController(dtFrontRightMotor)
                        .addSpeedController(dtBackRightMotor)
                        .setEncoder(dtRightEncoder)
                        .setErrorCorrection(dtRightPID));

        // setup dt controller
        CBDifferentialDriveController dtController =
                new CBDifferentialDriveController(this)
                    .addLeftDriveModule(dtLeftModule)
                    // TODO: changed line below...
                    //.addDriveModule(dtRightModule);
                    .addRightDriveModule(dtRightModule)
                ;

        // setup drive controller
        DriverControls driveControls = new DriverControls(hardwareAdapter);
        driveControls.setup();

        // setup operator controller
        OperatorControls operatorControls = new OperatorControls(hardwareAdapter);
        // TODO: fix line below
        //driveControls.setup();

        // setup teleop mapper //TODO: Tune Axis Scales
        this.addTeleOpMapper(new CBArcadeDriveMapper(this)
                .setAxes(driveControls.getForwardAxis(), null, driveControls.getRotationalAxis())
                // TODO: the following line commented because the mode above was changed to Power (from speed)
                //.setAxisScales(0, 40, 90) // no strafe, 40 inches/second, 90 degrees/second //TODO: Tune Scales
                .setGyroLockButton(driveControls.getGyroLockButton())
        );

        // setup robot controller
        this.addRobotController(dtController);

        // TODO: add behavior(s) they connect mappers and controllers
        // setup behaviors
        this.addBehavior(new CBStdDriveBehavior(this));

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
