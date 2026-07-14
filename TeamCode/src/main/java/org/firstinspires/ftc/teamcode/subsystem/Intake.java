package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Intake implements Subsystem {
    public static Intake INTAKE = new Intake();
    private Intake() {}

    private final ServoEx intake = new ServoEx("intake");

    public Command open = new Command() {
        @Override
        public void start() {
            intake.setPosition(0.7);
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }.requires(this);
    public Command close = new Command() {
        @Override
        public void start() {
            intake.setPosition(0.5);
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }.requires(this);
}
