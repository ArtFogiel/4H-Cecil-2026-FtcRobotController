package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.subsystem.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.subsystem.Intake.INTAKE;
import static org.firstinspires.ftc.teamcode.subsystem.Outtake.OUTTAKE;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.skeletonarmy.marrow.settings.Settings;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
@TeleOp(name="MainTeleOp", group = "OpMode")
public class MainTeleOp extends NextFTCOpMode {
    public MainTeleOp() {
        addComponents(
                new SubsystemComponent(
                        DRIVE,
                        INTAKE,
                        OUTTAKE
                ),
                BindingsComponent.INSTANCE,
                BulkReadComponent.INSTANCE

        );
    }
    @Override
    public void onStartButtonPressed() {
        DRIVE.teleDrive.schedule();
        INTAKE.close.schedule();
        if (Settings.get("controller", 2) == 1) {
            Gamepads.gamepad1().leftBumper()
                    .whenBecomesTrue(INTAKE.open)
                    .whenBecomesFalse(INTAKE.close)
            ;
            Gamepads.gamepad1().rightBumper()
                    .whenBecomesTrue(OUTTAKE.load)
            ;
            Gamepads.gamepad1().rightTrigger().greaterThan(0.1)
                    .whenBecomesTrue(OUTTAKE.drop)
            ;
        } else {
        Gamepads.gamepad2().leftBumper()
                .whenBecomesTrue(INTAKE.open)
                .whenBecomesFalse(INTAKE.close)
        ;
        Gamepads.gamepad2().rightBumper()
                .whenBecomesTrue(OUTTAKE.load)
        ;
        Gamepads.gamepad2().rightTrigger().greaterThan(0.1)
                .whenBecomesTrue(OUTTAKE.drop)
        ;
        }
    }
}
