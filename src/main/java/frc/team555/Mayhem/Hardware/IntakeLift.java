package frc.team555.Mayhem.Hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.team555.Mayhem.Data.ControlData;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.controllers.CBLiftController;
import org.montclairrobotics.cyborg.data.CBControlData;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBSpeedControllerFaultCriteria;
import org.montclairrobotics.cyborg.devices.CBTalonSRX;
import org.montclairrobotics.cyborg.utils.CBEnums;

public class IntakeLift {

    //Declare Cyborg Robot Class
    private Cyborg cyborg;

    // Declare Cyborg Hardware Adapter
    private CBHardwareAdapter hardwareAdapter;

    // Declare Power Distribution Board
    private CBDeviceID pdb;

    // Data for Mappers
    private CBControlData controlData;

    // Declare Motors
    private CBDeviceID motor;

    // Declare Encoders
    private CBDeviceID encoder;

    public IntakeLift(Cyborg cyborg, CBHardwareAdapter hardwareAdapter, CBDeviceID pdb, CBControlData controlData){
        this.cyborg = cyborg;
        this.hardwareAdapter = hardwareAdapter;
        this.pdb = pdb;
        this.controlData = controlData;
    }

    public boolean setup(){

        motor = hardwareAdapter.add(
                new CBTalonSRX(9)
                        .setDeviceName("Intake", "LiftMotor")
                        .setPowerSource(pdb, 6)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );

        encoder = hardwareAdapter.add(
                new CBEncoder(motor, FeedbackDevice.QuadEncoder, false, 1)
        );

        // intake lift controller definition
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
                        .setControlData(((ControlData) controlData).intakeLift)
                        // set a lower limit switch this is a hard limit
                        //.setBottomLimit(mainLiftLimit)
                        // set the encoder for the lift
                        .setEncoder(encoder)
                        // attach a speed controller array to drive the lift
                        .setSpeedControllerArray(new CBVictorArrayController()
                                .addSpeedController(motor)
                                .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );

        return true;
    }

    public CBDeviceID getMotor() {
        return motor;
    }

    public CBDeviceID getEncoder() {
        return encoder;
    }
}
