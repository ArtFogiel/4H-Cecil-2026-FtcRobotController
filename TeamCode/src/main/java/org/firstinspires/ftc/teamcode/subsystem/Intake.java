package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Intake implements Subsystem {
    public static Intake INTAKE = new Intake();
    private Intake() {}

    private final ServoEx intake = new ServoEx("intake");
    private boolean intakeOpen = false;

    public Command open = new Command() {
        @Override
        public void start() {
            intakeOpen = true;
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }.requires(this);
    public Command close = new Command() {
        @Override
        public void start() {
            intakeOpen = false;
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }.requires(this);
    @Override
    public void periodic() {
        if(intakeOpen) {
            intake.setPosition(0.3);
        } else {
            intake.setPosition(0.5);
        }
    }
}
