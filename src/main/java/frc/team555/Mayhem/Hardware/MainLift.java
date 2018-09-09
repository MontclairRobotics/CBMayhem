package frc.team555.Mayhem.Hardware;

import edu.wpi.first.wpilibj.CounterBase;
import frc.team555.Mayhem.Data.ControlData;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.controllers.CBLiftController;
import org.montclairrobotics.cyborg.data.CBControlData;
import org.montclairrobotics.cyborg.devices.*;
import org.montclairrobotics.cyborg.utils.CBEnums;

public class MainLift {

    // Declare Cyborg Robot Class
    private Cyborg cyborg;

    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Data for Mappers
    private CBControlData controlData;

    // Declare Motors
    private CBDeviceID frontMotor;
    private CBDeviceID backMotor;

    // Declare Encoders
    private CBDeviceID encoder;

    // Declare Limit Switch
    private CBDeviceID limitSwitch;

    public MainLift(Cyborg cyborg, CBHardwareAdapter hardwareAdapter, CBDeviceID pdb, CBControlData controlData){
        this.cyborg = cyborg;
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
        this.controlData = controlData;
    }

    public boolean setup(){

        frontMotor = hardwareAdapter.add(
                new CBTalonSRX(4)
                        .setDeviceName("MainLift", "Front")
                        .setPowerSource(pdb, 4)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );

        backMotor = hardwareAdapter.add(
                new CBTalonSRX(2)
                        .setDeviceName("MainLift", "Back")
                        .setPowerSource(pdb, 5)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );

        encoder = hardwareAdapter.add(
                new CBEncoder(4, 5, CounterBase.EncodingType.k4X, false, 1)
        );

        limitSwitch = hardwareAdapter.add(new CBDigitalInput(9));

        // main lift controller definition
        cyborg.addRobotController(
                // hardware configurations are done here.
                // there are other "soft" configurations done in the mapper
                // that include margins (which trigger slow motion)
                // and in this case a encoder based top limit
                new CBLiftController(cyborg)
                        // setData allows you to pick a CBLinearControllerData variable
                        // in controlData to use for this lift. There might be several
                        // lift controllers and each one would be controlled by a different
                        // CBLinearControllerData object in controlData.
                        .setControlData(((ControlData) controlData).mainLift)
                        // set a lower limit switch this is a hard limit
                        .setBottomLimit(limitSwitch)
                        // set the encoder for the lift
                        .setEncoder(encoder)
                        // attach a speed controller array to drive the lift
                        .setSpeedControllerArray(new CBVictorArrayController()
                                .addSpeedController(frontMotor)
                                .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );

        return true;
    }

    public CBDeviceID getFrontMotor() {
        return frontMotor;
    }

    public CBDeviceID getBackMotor() {
        return backMotor;
    }

    public CBDeviceID getEncoder() {
        return encoder;
    }

    public CBDeviceID getLimitSwitch() {
        return limitSwitch;
    }
}