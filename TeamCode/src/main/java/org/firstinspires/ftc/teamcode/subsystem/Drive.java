package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DifferentialArcadeDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

public class Drive implements Subsystem {
    public static Drive DRIVE = new Drive();
    private Drive() {}

    private final MotorEx leftDrive = new MotorEx("leftDrive");
    private final MotorEx rightDrive = new MotorEx("rightDrive").reversed();

    public Command teleDrive = new Command() {
        final DifferentialArcadeDriverControlled opDrive = new DifferentialArcadeDriverControlled(leftDrive, rightDrive, Gamepads.gamepad1().leftStickY(), Gamepads.gamepad1().rightStickX());
        @Override
        public void start() {
            opDrive.start();
        }
        @Override
        public void update() {
            opDrive.update();
        }
        @Override
        public boolean isDone(){
            return opDrive.isDone();
        }
    };
}
