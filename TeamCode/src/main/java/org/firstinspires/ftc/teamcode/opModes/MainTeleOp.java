package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.subsystem.Drive.DRIVE;
import static org.firstinspires.ftc.teamcode.subsystem.Intake.INTAKE;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
                        INTAKE
                ),
                BindingsComponent.INSTANCE,
                BulkReadComponent.INSTANCE

        );
    }
    @Override
    public void onStartButtonPressed() {
        DRIVE.teleDrive.schedule();
        Gamepads.gamepad1().leftBumper()
                .whenBecomesTrue(INTAKE.open)
                .whenBecomesFalse(INTAKE.close)
        ;
        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(DRIVE.turnToAngle(180).then(DRIVE.teleDrive))
        ;
        Gamepads.gamepad1().circle()
                .whenBecomesTrue(DRIVE.driveInches(24).then(DRIVE.teleDrive))
        ;
        Gamepads.gamepad1().leftStickButton()
                .whenBecomesTrue(DRIVE.teleDrive)
        ;
    }
}
