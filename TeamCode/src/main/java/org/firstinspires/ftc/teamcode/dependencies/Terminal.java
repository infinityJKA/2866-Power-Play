package org.firstinspires.ftc.teamcode.dependencies;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.enums.AllianceSide;
import org.firstinspires.ftc.teamcode.enums.ColorSide;
import org.firstinspires.ftc.teamcode.enums.Direction;
import org.firstinspires.ftc.teamcode.enums.Parking;

public class Terminal {
    private static DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, linearSlide;
    private static Servo claw;

    private static BNO055IMU imu;

    public static void run(LinearOpMode linearOpMode, AllianceSide allianceSide, ColorSide colorSide) {
        frontLeftMotor = linearOpMode.hardwareMap.get(DcMotor.class, "left");
        frontRightMotor = linearOpMode.hardwareMap.get(DcMotor.class, "right");
        backLeftMotor = linearOpMode.hardwareMap.get(DcMotor.class, "bLeft");
        backRightMotor = linearOpMode.hardwareMap.get(DcMotor.class, "bRight");
        linearSlide = linearOpMode.hardwareMap.get(DcMotor.class, "ls");
        imu = linearOpMode.hardwareMap.get(BNO055IMU.class, "imu");
        claw = linearOpMode.hardwareMap.servo.get("claw");
        RobotParameters rP = new RobotParameters(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, 1440, 2.6, 60, linearSlide, imu, 12.5);
        MecanumEncoder mecanumEncoder = new MecanumEncoder(rP, linearOpMode);
        LinearSlide linSlide = new LinearSlide(linearSlide, claw, linearOpMode);
        Direction direction = null, rotation = null;

        ColorSensor colorSensor = new ColorSensor("LiveLeak", linearOpMode.hardwareMap, linearOpMode);

        linearOpMode.waitForStart();

        linearOpMode.telemetry.speak("Autonomous has started!");

        // D = Dual (Red/Blue)
        // Y = Yellow
        // G = Green
        Parking parking = Parking.NOT_DECIDED;
        if (colorSensor.isRegionGreen(1)) {
            parking = Parking.G;
            linearOpMode.telemetry.speak("Green");
        } else if (colorSensor.isRegionYellow(1)) {
            parking = Parking.Y;
            linearOpMode.telemetry.speak("Yellow");
        }
        else{
            parking = Parking.D;
            linearOpMode.telemetry.speak("Yippie!");
        }

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
//        int rotator = allianceSide.LEFT ? -1:1;
        if (linearOpMode.opModeIsActive()) {
//            linSlide.closeClaw();
//            linSlide.moveToPosition(LinearPosition.ONE, 1);
//            mecanumEncoder.moveInches(direction.FORWARD, 54.5, 1);
////            for (int i = 1; i < 4; i++){
//            mecanumEncoder.rotateDegrees(rotation.CCW, 90 * rotator, 1);
//            mecanumEncoder.moveInches(direction.FORWARD, 12, 1);
//            mecanumEncoder.rotateDegrees(rotation.CW, 90 * rotator, 1);
//            linSlide.moveToPosition(LinearPosition.THREE, 1);
//            mecanumEncoder.moveInches(direction.FORWARD, 4, 1);
//            linSlide.moveToPosition(LinearPosition.ZERO, 1);
//            mecanumEncoder.moveInches(direction.BACKWARD, 4, 1);
////                mecanumEncoder.rotateDegrees(rotation.CW, 90 * rotator, 1);
////                mecanumEncoder.moveInches(direction.FORWARD, 37.75, 1);
////                mecanumEncoder.moveInches(direction.BACKWARD, 37.75, 1);
////                mecanumEncoder.rotateDegrees(rotation.CCW, 90 * rotator, 1);
////                LinearPosition coneEnum = LinearPosition.valueOf("CONE"+Integer.toString(i));
////                linSlide.pickupCone(coneEnum,1);
////            }
//
//            if(parking == Parking.G){
//                mecanumEncoder.moveInches(direction.LEFT, 10*rotator, 1);
//            }
//            else if(parking == Parking.Y){
//                mecanumEncoder.moveInches(direction.RIGHT, 10*rotator, 1);
//            }
//            else{
//                mecanumEncoder.moveInches(direction.RIGHT, 25*rotator, 1);
//            }
            linearOpMode.telemetry.speak("sayonara");
            mecanumEncoder.moveInches(Direction.FORWARD, 28, 1);
            if(parking == Parking.G){
                mecanumEncoder.rotateDegrees(rotation.CCW, 90, 1);
                mecanumEncoder.moveInches(direction.FORWARD, 24, 1);
                mecanumEncoder.rotateDegrees(rotation.CW, 95, 1);
                mecanumEncoder.moveInches(direction.FORWARD, 5, 1);
            }
            else if(parking == Parking.Y){
                Thread.yield();
            }
            else{
                mecanumEncoder.rotateDegrees(rotation.CW, 90, 1);
                mecanumEncoder.moveInches(direction.FORWARD, 24, 1);
                mecanumEncoder.rotateDegrees(rotation.CCW, 90, 1);
                mecanumEncoder.moveInches(direction.FORWARD, 5, 1);
            }

        }

    }
}
