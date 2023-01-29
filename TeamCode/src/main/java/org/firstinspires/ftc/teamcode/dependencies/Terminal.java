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
        RobotParameters rP = new RobotParameters(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, 1440, 2.6, 60, linearSlide, imu, 10.5);
        MecanumEncoder mecanumEncoder = new MecanumEncoder(rP, linearOpMode);
        LinearSlide linSlide = new LinearSlide(linearSlide, claw, linearOpMode);
        Direction direction = null, rotation = null;

        ColorSensor colorSensor = new ColorSensor("LiveLeak", linearOpMode.hardwareMap, linearOpMode);

        linearOpMode.waitForStart();

//        linearOpMode.telemetry.speak("Autonomous has started!");

        // D = Dual (Red/Blue)
        // O = Other
        // Y = Yellow
        // G = Green
        Parking parking = Parking.NOT_DECIDED;
        if ((colorSensor.isRegionBlue(1) && colorSide == ColorSide.RED) || (colorSensor.isRegionRed(1) && colorSide == ColorSide.BLUE)){
            parking = Parking.D;
            linearOpMode.telemetry.addData("PARKING: ","DUAL");
            linearOpMode.telemetry.update();
//            linearOpMode.telemetry.speak("DUAL");
        } else if (colorSensor.isRegionYellow(1)) {
            parking = Parking.Y;
            linearOpMode.telemetry.addData("PARKING: ","YELLOW");
            linearOpMode.telemetry.update();
//            linearOpMode.telemetry.speak("Yellow");
        }
        else{
            parking = Parking.O;
            linearOpMode.telemetry.addData("PARKING: ","OTHER");
            linearOpMode.telemetry.update();
//            linearOpMode.telemetry.speak("Yippie!");
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

            //The code below supposedly places a cone on the low junction then comes back to park

            linSlide.closeClaw();
            linSlide.sleep(2000);
            linSlide.moveToPosition(LinearSlide.LinearPosition.ONE, 1);
//            mecanumEncoder.sleep(10);
            mecanumEncoder.moveInches(direction.FORWARD, 40, 1);
            mecanumEncoder.moveInches(direction.BACKWARD, 8, 1);
            linSlide.moveToPosition(LinearSlide.LinearPosition.TWO, 1);
            mecanumEncoder.rotateDegrees(Direction.CW, 40, 1);
            mecanumEncoder.moveInches(Direction.FORWARD, 4.5, 1);

            mecanumEncoder.sleep(100);
            linSlide.sleep(100);

            linSlide.openClaw();
            //
            linSlide.moveToPosition(LinearSlide.LinearPosition.ZERO, 1);
            mecanumEncoder.moveInches(Direction.BACKWARD, 4.5, 1);
            mecanumEncoder.rotateDegrees(Direction.CCW, 40, 1);
            mecanumEncoder.sleep(150);
            mecanumEncoder.moveInches(Direction.FORWARD, 28, 1);
            mecanumEncoder.moveInches(Direction.BACKWARD, 5, 1);
//            mecanumEncoder.rotateDegrees(Direction.CCW, 100, 1);
//            linSlide.moveToPosition(LinearSlide.LinearPosition.CONE5, 1);
//            mecanumEncoder.moveInches(Direction.FORWARD, 28, 1);
//            linSlide.closeClaw();
//            linSlide.sleep(400);
//            linSlide.moveToPosition(LinearSlide.LinearPosition.ONE, 1);
//            mecanumEncoder.sleep(400);
//            mecanumEncoder.moveInches(Direction.BACKWARD, 22, 1);
//            mecanumEncoder.rotateDegrees(Direction.CCW, 43, 1);
//            linSlide.moveToPosition(LinearSlide.LinearPosition.ONE, 1);
//            mecanumEncoder.moveInches(Direction.FORWARD, 6, 1);
//            mecanumEncoder.sleep(100);
//            linSlide.sleep(100);
//            linSlide.openClaw();
//            mecanumEncoder.moveInches(Direction.BACKWARD, 6, 1);
//            mecanumEncoder.rotateDegrees(Direction.CW, 40, 1);
//            linSlide.moveToPosition(LinearSlide.LinearPosition.ZERO, 0.8);





//
            if(parking == Parking.D){
                mecanumEncoder.rotateDegrees(Direction.CCW, 93, 1);
                mecanumEncoder.moveInches(Direction.FORWARD,21.75,1);
//                mecanumEncoder.rotateDegrees(Direction.CCW, 90, 1);
//                mecanumEncoder.moveInches(Direction.BACKWARD, 5, 1);
//                mecanumEncoder.rotateDegrees(Direction.CW, 90, 1);
//                mecanumEncoder.moveInches(direction.BACKWARD, 27, 1);
//                mecanumEncoder.rotateDegrees(Direction.CCW, 90, 1);
//                mecanumEncoder.moveInches(direction.FORWARD, 5, 1);
            }
            else if(parking == Parking.Y){
                mecanumEncoder.moveInches(Direction.BACKWARD,5,1);
//                mecanumEncoder.moveInches(Direction.BACKWARD, 5, 1);
            }
            else{
                mecanumEncoder.rotateDegrees(Direction.CCW, 93, 1);
                mecanumEncoder.rotateDegrees(Direction.CW, 10, 1);
                mecanumEncoder.moveInches(Direction.BACKWARD,24,1);
//                mecanumEncoder.moveInches(Direction.BACKWARD, 5, 1);
//                mecanumEncoder.rotateDegrees(Direction.CCW, 90, 1);
//                mecanumEncoder.moveInches(direction.BACKWARD, 25, 1);
//                mecanumEncoder.rotateDegrees(Direction.CW, 90, 1);
//                mecanumEncoder.moveInches(direction.FORWARD, 5, 1);
            }
////            mecanumEncoder.moveInches(Direction.FORWARD, 29, 1);
            mecanumEncoder.sleep(500);

        }

    }
}
