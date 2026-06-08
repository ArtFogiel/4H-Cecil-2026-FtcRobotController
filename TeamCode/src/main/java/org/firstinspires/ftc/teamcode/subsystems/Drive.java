package org.firstinspires.ftc.teamcode.subsystems;

import java.util.function.Supplier;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.driving.DifferentialArcadeDriverControlled;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.impl.MotorEx;

public class Drive implements Subsystem {
    public static final Drive DRIVE = new Drive();
    private Drive() {}

    private final MotorEx leftDrive = new MotorEx("leftDrive").reversed();
    private final MotorEx rightDrive = new MotorEx("rightDrive");
    private final Supplier<Double> forward = ()-> (double) ActiveOpMode.gamepad1().left_stick_y;
    private final Supplier<Double> turn = ()-> (double) ActiveOpMode.gamepad1().right_stick_x;

    public DriverControlledCommand teleOpDrive = new DifferentialArcadeDriverControlled(
            leftDrive, rightDrive, forward, turn
    );
}
