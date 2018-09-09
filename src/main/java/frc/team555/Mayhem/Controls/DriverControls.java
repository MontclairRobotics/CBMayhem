package frc.team555.Mayhem.Controls;

import frc.team555.Mayhem.RobotCB;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBButton;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;

public class DriverControls {

    //Cyborg Robot Class
    Cyborg cyborg;

    // Cyborg Hardware Adapter
    CBHardwareAdapter hardwareAdapter;

    // Joystick USB Port
    private final int STICK_ID = 0;

    // Declare Joystick axis and buttons
    private CBDeviceID xAxis;
    private CBDeviceID yAxis;
    private CBDeviceID gyroLockButton;

    public DriverControls(Cyborg cyborg, CBHardwareAdapter hardwareAdapter){
        this.cyborg = cyborg;
        this.hardwareAdapter = hardwareAdapter;

    }

    public boolean setup() {

        //Initialize Axis
        xAxis = hardwareAdapter.add(
                new CBAxis(STICK_ID, 1)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        yAxis = hardwareAdapter.add(
                new CBAxis(STICK_ID, 0)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        //Initialize Buttons
        gyroLockButton = hardwareAdapter.add(
                new CBButton(STICK_ID, 1)
        );

        // Setup Drive Mapper
        cyborg.addTeleOpMapper(
                new CBArcadeDriveMapper(cyborg)
                        .setAxes(xAxis, null, yAxis)
                        // TODO: the following line commented because the mode above was changed to Power (from speed)
                        // TODO: Tune Axis Scales
                        //.setAxisScales(0, 40, 90) // no strafe, 40 inches/second, 90 degrees/second
                        .setGyroLockButton(gyroLockButton)
        );

        return true;
    }

}
