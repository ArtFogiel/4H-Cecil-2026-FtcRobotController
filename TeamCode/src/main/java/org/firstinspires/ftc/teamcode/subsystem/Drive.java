package org.firstinspires.ftc.teamcode.subsystem;

import com.skeletonarmy.marrow.settings.Settings;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DifferentialArcadeDriverControlled;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;
import static java.lang.Math.PI;

public class Drive implements Subsystem {
    public static Drive DRIVE = new Drive();
    private Drive() {}

    public enum DriveState {
        IDLE,
        TELEOP,
        DRIVING_INCHES,
        TURNING_DEGREES
    }
    private DriveState state = DriveState.IDLE;
    private double headingOffset = 90.0;

    private final MotorEx leftDrive = new MotorEx("leftDrive");
    private final MotorEx rightDrive = new MotorEx("rightDrive").reversed();
    private final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD);

    private static final double TICKS_PER_REV = 537.7;
    private static final double WHEEL_DIAMETER_INCHES = 96.0 / 25.4;
    private static final double TICKS_PER_INCH = TICKS_PER_REV / (WHEEL_DIAMETER_INCHES * PI);

    public double getHeading() {
        return imu.get().inDeg + headingOffset;
    }
    public void setHeading(double degrees) {
        imu.zero();
        headingOffset = degrees;
    }

    public Command teleDrive = new Command() {
        final DifferentialArcadeDriverControlled opDrive = new DifferentialArcadeDriverControlled(leftDrive, rightDrive, Gamepads.gamepad1().leftStickY(), Gamepads.gamepad1().rightStickX());
        @Override
        public void start() {
            state = DriveState.TELEOP;
            opDrive.start();
        }
        @Override
        public void update() {
            opDrive.update();
        }
        @Override
        public void stop(boolean interrupted) {
            state = DriveState.IDLE;
        }
        @Override
        public boolean isDone(){
            return opDrive.isDone();
        }
    }.requires(this);
    public Command driveInches(double inches) {
        return new Command() {
            final ControlSystem control = ControlSystem.builder()
                    .posPid(0.05, 0.0, 0.01)
                    .build();

            @Override
            public void start() {
                state = DriveState.DRIVING_INCHES;
                leftDrive.setCurrentPosition(0);
                rightDrive.setCurrentPosition(0);
                control.setGoal(new KineticState(inches * TICKS_PER_INCH, 0, 0));
            }

            @Override
            public void update() {
                double currentTicks = (leftDrive.getCurrentPosition() + rightDrive.getCurrentPosition()) / 2.0;
                double power = control.calculate(new KineticState(currentTicks, 0, 0));
                leftDrive.setPower(power);
                rightDrive.setPower(power);
            }

            @Override
            public boolean isDone() {
                return control.isWithinTolerance(new KineticState(10, 5, 0));
            }

            @Override
            public void stop(boolean interrupted) {
                state = DriveState.IDLE;
                leftDrive.setPower(0);
                rightDrive.setPower(0);
            }
        }.requires(this);
    }
    public Command turnToAngle(double degrees) {
        return new Command() {
            final ControlSystem control = ControlSystem.builder()
                    .angular(AngleType.DEGREES, b -> b.posPid(0.01, 0, 0.001))
                    .build();

            @Override
            public void start() {
                state = DriveState.TURNING_DEGREES;
                control.setGoal(new KineticState(degrees, 0, 0));
            }

            @Override
            public void update() {
                double currentAngle = getHeading();
                double power = control.calculate(new KineticState(currentAngle, 0, 0));
                leftDrive.setPower(-power);
                rightDrive.setPower(power);
            }

            @Override
            public boolean isDone() {
                return control.isWithinTolerance(new KineticState(1.0, 0.5, 0));
            }

            @Override
            public void stop(boolean interrupted) {
                state = DriveState.IDLE;
                leftDrive.setPower(0);
                rightDrive.setPower(0);
            }
        }.requires(this);
    }
    
    @Override
    public void periodic() {
        ActiveOpMode.telemetry().addData("Drive State", state);
        if (Settings.get("debug_mode", false)) {
            ActiveOpMode.telemetry().addData("Left Ticks", leftDrive.getCurrentPosition());
            ActiveOpMode.telemetry().addData("Right Ticks", rightDrive.getCurrentPosition());
            ActiveOpMode.telemetry().addData("Heading", getHeading());
        }
        ActiveOpMode.telemetry().update();
    }
}
