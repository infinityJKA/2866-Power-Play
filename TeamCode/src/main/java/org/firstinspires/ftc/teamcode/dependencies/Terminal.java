package org.firstinspires.ftc.teamcode.dependencies;

//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.enums.AllianceSide;
import org.firstinspires.ftc.teamcode.enums.Direction;

public class Terminal {
    private static DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, linearSlide;
    ;
    private static BNO055IMU imu;

    public static void run(LinearOpMode linearOpMode, AllianceSide allianceSide) {
        frontLeftMotor = linearOpMode.hardwareMap.get(DcMotor.class, "left");
        frontRightMotor = linearOpMode.hardwareMap.get(DcMotor.class, "right");
        backLeftMotor = linearOpMode.hardwareMap.get(DcMotor.class, "bLeft");
        backRightMotor = linearOpMode.hardwareMap.get(DcMotor.class, "bRight");
        linearSlide = linearOpMode.hardwareMap.get(DcMotor.class, "ls");
        imu = linearOpMode.hardwareMap.get(BNO055IMU.class, "imu");
        RobotParameters rP = new RobotParameters(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, 1440, 2.6, 60, linearSlide, imu, 12.5);
        MecanumEncoder mecanumEncoder = new MecanumEncoder(rP, linearOpMode);
        Direction direction = null, rotation = null;

        linearOpMode.waitForStart();
        int rotator = 0;
        switch (allianceSide) {
            case LEFT:
                rotator = -1;
                break;
            case RIGHT:
                rotator = 1;
                break;
            default:
                throw new IllegalArgumentException("Position must be LEFT or RIGHT!");
        }
        // int rotator = allianceSide.LEFT ? -1:1;
        if (linearOpMode.opModeIsActive()) {
            mecanumEncoder.moveInches(direction.FORWARD, 74, 1);
            mecanumEncoder.rotateDegrees(rotation.CCW, 90 * rotator, 1);
            mecanumEncoder.moveInches(direction.FORWARD, 3.25, 1);
            mecanumEncoder.moveInches(direction.BACKWARD, 3.25, 1);
            mecanumEncoder.rotateDegrees(rotation.CW, 90 * rotator, 1);
            mecanumEncoder.moveInches(direction.BACKWARD, 15.5, 1);
            mecanumEncoder.rotateDegrees(rotation.CW, 90 * rotator, 1);
            mecanumEncoder.moveInches(direction.FORWARD, 22.25, 1);
            mecanumEncoder.moveInches(direction.BACKWARD, 22.25, 1);
            mecanumEncoder.rotateDegrees(rotation.CCW, 90 * rotator, 1);
            mecanumEncoder.moveInches(direction.FORWARD, 15.5, 1);
            mecanumEncoder.rotateDegrees(rotation.CCW, 90 * rotator, 1);
            mecanumEncoder.moveInches(direction.FORWARD, 3.25, 1);

        }

    }
}