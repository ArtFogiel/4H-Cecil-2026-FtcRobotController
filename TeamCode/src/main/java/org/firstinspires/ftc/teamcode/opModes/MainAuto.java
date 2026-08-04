package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.subsystem.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.subsystem.Intake.INTAKE;
import static org.firstinspires.ftc.teamcode.subsystem.Outtake.OUTTAKE;

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
                        INTAKE,
                        OUTTAKE
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
        OUTTAKE.preLoad.schedule();
        new SequentialGroup(
                DRIVE.driveInches(30),
                DRIVE.turnToAngle(mirrorAngle(180)),
                DRIVE.driveInches(39).and(INTAKE.open),
                INTAKE.close.and(OUTTAKE.drop),
                DRIVE.driveInches(-23).afterTime(1),
                DRIVE.turnToAngle(mirrorAngle(90)),
                DRIVE.driveInches(43.5),
                DRIVE.turnToAngle(mirrorAngle(180)),
                DRIVE.driveInches(18).and(INTAKE.open),
                INTAKE.close.and(OUTTAKE.drop),
                DRIVE.driveInches(-22).afterTime(1),
                DRIVE.turnToAngle(mirrorAngle(90)),
                DRIVE.driveInches(-44)
        ).schedule();
    }
    private double mirrorAngle(double angle) {
        if (Settings.get("alliance", SettingsSetter.alliance.RED) == SettingsSetter.alliance.BLUE) {
            return 180 - angle;
        }
        return angle;
    }
}
