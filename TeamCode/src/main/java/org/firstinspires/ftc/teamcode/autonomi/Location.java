package org.firstinspires.ftc.teamcode.autonomi;

    // NOTE: this file used to be FreightAuto but was renamed to be more concisely named
// This file contains the run method these 4 files call with different allianceSides and alliancePositions:
// WarehousePark_RedLeft
// WarehousePark_RedRight
// WarehousePark_BlueLeft
// WarehousePark_BlueRight
// - jack

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.dependencies.ColorSensor;
import org.firstinspires.ftc.teamcode.dependencies.Parking;
import org.firstinspires.ftc.teamcode.enums.AllianceSide;


public class Location {
    public static void run(LinearOpMode linearOp, AllianceSide allianceSide){
        // Declare any local / helper variables here
        // Our initialization code should go here
        HardwareMap hardwareMap = linearOp.hardwareMap;
        DcMotor leftDrive = hardwareMap.get(DcMotor.class, "left");
        DcMotor rightDrive = hardwareMap.get(DcMotor.class, "right");
        //Declare linear slide
        //Declare declare claw
        ColorSensor colorSensor = new ColorSensor("Webcam", hardwareMap, linearOp);

        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);

//        TwoWheelEncoder encoder = new TwoWheelEncoder(leftDrive, rightDrive, imu, CommonParameters.FREIGHT_ROBOT, linearOp);

        linearOp.waitForStart();

        // me when cola & fortnite
        linearOp.telemetry.speak("yippie!");

        //Insert camera code here
        // for now, assume the duck is in the middle
        Parking parking;
        if (colorSensor.isRegionGreen(0)){
            parking = Parking.G;
        } else if (colorSensor.isRegionBlue(0)){
            parking = Parking.B;
        }
        else{
            parking = Parking.R;
        }


//        // Move the arm to the appropriate height
//        arm.moveToPosition(armPos, 0.35);
//        arm.closeHand();
//        arm.wristDown();
//        linearOp.sleep(250);
//        encoder.moveInches(6);
//
//        // Move to the hub (This will run while the arm is moving to save time)
//        rotationalDirection towardsHub;
//        if (alliancePosition == AlliancePosition.LEFT){
//            towardsHub = rotationalDirection.CLOCKWISE;
//        } else {
//            towardsHub = rotationalDirection.COUNTERCLOCKWISE;
//        }
//        encoder.rotateDegrees(towardsHub, 34.75, 0.3);
//        encoder.moveInches(armPos == ArmPosition.THREE ? 23.75 : 20, 0.5);
//
//        // Spit out the block
//        arm.stopHand();
//        linearOp.sleep(1000);
//
//        rotationalDirection towardsDepot;
//        rotationalDirection awayFromDepot;
//        if (allianceSide == AllianceSide.RED){
//            towardsDepot = rotationalDirection.CLOCKWISE;
//            awayFromDepot = rotationalDirection.COUNTERCLOCKWISE;
//        } else {
//            towardsDepot = rotationalDirection.COUNTERCLOCKWISE;
//            awayFromDepot = rotationalDirection.CLOCKWISE;
//        }
//        int rotation1;
//        int rotation2;
//        int distanceFromDepot;
//        if (allianceSide == AllianceSide.RED && alliancePosition == AlliancePosition.RIGHT
//                || allianceSide == AllianceSide.BLUE && alliancePosition == AlliancePosition.LEFT ){
//            // CLoser to depot
//            rotation1 = 33;
//            rotation2 = 40;
//            distanceFromDepot = 16;
//        } else {
//            // Further from depot
//            rotation1 = 70;
//            rotation2 = 18;
//            distanceFromDepot = 47;
//        }
//
//        // move back halfway, turn 90 degrees and move the rest of the way
//        encoder.moveInches(DcMotorSimple.Direction.REVERSE, 12, 0.5);
//        arm.wristUp();
//        arm.moveToPosition(ArmPosition.NEUTRAL, 0.5);
//        encoder.rotateDegrees(towardsDepot, 190, 0.5);
//        encoder.moveInches(DcMotorSimple.Direction.FORWARD, armPos == ArmPosition.THREE ? 15.75 : 12.5, 0.5);
//
//        //turn towards garage opening and move towards it, then turn towards inside of garage and move into it
//        encoder.rotateDegrees(awayFromDepot, rotation1);
//        encoder.moveInches(distanceFromDepot);
//        encoder.rotateDegrees(awayFromDepot, rotation2);
//        encoder.moveInches(20);
//
//
//        arm.moveToPosition(ArmPosition.NEUTRAL, 0.4);
//
    }


}
