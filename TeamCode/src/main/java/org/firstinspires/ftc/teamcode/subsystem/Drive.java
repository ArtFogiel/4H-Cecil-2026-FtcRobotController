package org.firstinspires.ftc.teamcode.subsystem;

import com.skeletonarmy.marrow.settings.Settings;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.driving.DifferentialArcadeDriverControlled;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;
import static java.lang.Math.PI;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.util.function.Supplier;

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

    private final MotorEx leftDrive = new MotorEx("leftDrive").reversed().zeroed();
    private final MotorEx rightDrive = new MotorEx("rightDrive").zeroed();
    private final IMUEx imu = new IMUEx("imu", Direction.UP, Direction.FORWARD);

    private static final double TICKS_PER_REV = 537.7;
    private static final double WHEEL_DIAMETER_IN = 96/25.4;
    private static final double TICKS_PER_INCH = TICKS_PER_REV / (WHEEL_DIAMETER_IN * PI);
    private final Supplier<Double> turnPower = ()-> (double) ActiveOpMode.gamepad1().right_stick_x*((ActiveOpMode.gamepad1().left_trigger*0.5)+0.5);
    private final Supplier<Double> forwardPower = ()-> (double) ActiveOpMode.gamepad1().left_stick_y*-((ActiveOpMode.gamepad1().left_trigger*0.5)+0.5);

    private double getHeading() {
        return imu.get().inDeg + headingOffset;
    }
    public void setHeading(double degrees) {
        imu.zero();
        headingOffset = degrees;
    }
    private final ControlSystem control = ControlSystem.builder()
            .posPid(0.0005, 0.000000000002, 0.00005)
            .build();
    private final ControlSystem headingControl = ControlSystem.builder()
            .posPid(0.01, 0.00000000002, 0.001)
            .build();

    public Command teleDrive = new Command() {
        final DifferentialArcadeDriverControlled opDrive = new DifferentialArcadeDriverControlled(leftDrive, rightDrive, forwardPower, turnPower);
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
            @Override
            public void start() {
                state = DriveState.DRIVING_INCHES;
                leftDrive.zero();
                rightDrive.zero();
                control.setGoal(new KineticState(inches*TICKS_PER_INCH, 0, 0));
                headingControl.setGoal(new KineticState(getHeading(), 0, 0));
            }
            @Override
            public void update() {
                double currentTicks = (leftDrive.getCurrentPosition() + rightDrive.getCurrentPosition()) / 2.0;
                double currentVelocity = (leftDrive.getVelocity() + rightDrive.getVelocity());
                double forwardPower = control.calculate(new KineticState(currentTicks, currentVelocity, 0));

                double currentHeading = getHeading();
                double angularVelocity = imu.getImu().getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate;
                double correction = headingControl.calculate(new KineticState(currentHeading, angularVelocity, 0));

                double leftPower = forwardPower - correction;
                double rightPower = forwardPower + correction;

                if (leftPower < -0.5) {
                    leftPower = -0.5;
                } else if (leftPower > 0.5) {
                    leftPower = 0.5;
                }
                if (rightPower < -0.5) {
                    rightPower = -0.5;
                } else if (rightPower > 0.5) {
                    rightPower = 0.5;
                }
                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);
            }
            @Override
            public boolean isDone() {
                return control.isWithinTolerance(new KineticState(15, 5, 0)) && headingControl.isWithinTolerance(new KineticState(3.0, 0.5, 0));
            }
            @Override
            public void stop(boolean interrupted) {
                state = DriveState.IDLE;
                leftDrive.setPower(0);
                rightDrive.setPower(0);
                leftDrive.zeroed();
                rightDrive.zeroed();
            }
        }.requires(this);
    }
    public Command turnToAngle(double degrees) {
        return new Command() {
            @Override
            public void start() {
                state = DriveState.TURNING_DEGREES;
                headingControl.setGoal(new KineticState(degrees, 0, 0));
            }
            @Override
            public void update() {
                double currentAngle = getHeading();
                double power = headingControl.calculate(new KineticState(currentAngle, imu.getImu().getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate, 0));
                if (power < -0.75) {
                    leftDrive.setPower(0.75);
                    rightDrive.setPower(-0.75);
                } else if (power > 0.75){
                    leftDrive.setPower(-0.75);
                    rightDrive.setPower(0.75);
                } else {
                    leftDrive.setPower(-power);
                    rightDrive.setPower(power);
                }
            }
            @Override
            public boolean isDone() {
                return headingControl.isWithinTolerance(new KineticState(1.0, 0.5, 0));
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
        ActiveOpMode.telemetry().addData("speed multiplier", ActiveOpMode.gamepad1().left_trigger);
        ActiveOpMode.telemetry().update();
    }
}
