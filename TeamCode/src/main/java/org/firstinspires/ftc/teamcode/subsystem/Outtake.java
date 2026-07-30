package org.firstinspires.ftc.teamcode.subsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;

public class Outtake implements Subsystem {
    public static Outtake OUTTAKE = new Outtake();
    private Outtake() {}

    private final ServoEx outtake = new ServoEx("outtake");
    private boolean slot1Full = false;
    private boolean slot2Full = false;

    public void loadSlot1() {
        outtake.setPosition(0);
        slot1Full = true;
    }
    public void loadSlot2() {
        outtake.setPosition(0.32);
        slot2Full = true;
    }
    public void dropSlot1() {
        outtake.setPosition(0.6);
        slot1Full = false;
    }
    public void dropSlot2() {
        outtake.setPosition(1);
        slot2Full = false;
    }

    public Command load = new Command() {
        @Override
        public void start() {
            if(!slot1Full) {
                loadSlot1();
            } else {
                loadSlot2();
            }
        }
        @Override
        public boolean isDone() {
            return true;
        }
    }.requires(this);
    public Command drop = new Command() {
        @Override
        public void start() {
            if (slot1Full) {
                dropSlot1();
            } else {
                dropSlot2();
            }
        }
        @Override
        public boolean isDone() {
            return true;
        }
    }.requires(this);
    public Command preLoad = new Command() {
        @Override
        public void start() {
            slot1Full = true;
            slot2Full = true;
        }
        @Override
        public boolean isDone() {
            return true;
        }
    };

    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Slot1 Loaded", slot1Full);
        ActiveOpMode.telemetry().addData("Slot2 Loaded", slot2Full);
    }
}
