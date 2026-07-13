package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.subsystem.Drive.DRIVE;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
@TeleOp(name="Test", group = "OpMode")
public class MainTeleOp extends NextFTCOpMode {
    public MainTeleOp() {
        addComponents(
                new SubsystemComponent(
                        DRIVE
                ),
                BindingsComponent.INSTANCE,
                BulkReadComponent.INSTANCE

        );
    }
    @Override
    public void onStartButtonPressed() {
        DRIVE.teleDrive.schedule();
    }
}
