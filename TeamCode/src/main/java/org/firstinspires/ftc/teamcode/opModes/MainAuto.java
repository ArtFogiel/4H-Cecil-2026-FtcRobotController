package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.subsystem.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.subsystem.Intake.INTAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.skeletonarmy.marrow.settings.Settings;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import org.firstinspires.ftc.teamcode.marrow.SettingsSetter;

@Autonomous(name="MainAuto", group = "OpMode")
public class MainAuto extends NextFTCOpMode {
    public MainAuto() {
        addComponents(
                new SubsystemComponent(
                        DRIVE,
                        INTAKE
                ),
                BulkReadComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        DRIVE.setHeading(90);
    }

    @Override
    public void onStartButtonPressed() {
        new SequentialGroup(
                DRIVE.driveInches(24),
                DRIVE.turnToAngle(mirrorAngle(180))
        ).schedule();
    }
    private double mirrorAngle(double angle) {
        if (Settings.get("alliance", SettingsSetter.alliance.RED) == SettingsSetter.alliance.BLUE) {
            return 180 - angle;
        }
        return angle;
    }
}
