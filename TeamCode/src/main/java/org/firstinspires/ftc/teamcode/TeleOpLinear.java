package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.dependencies.*;

@TeleOp(name="TeleOp", group="TeleOp")

public class TeleOpLinear extends OpMode{
    private DcMotor ls; //LINEAR SLIDE
    private DcMotor flMotor, frMotor, blMotor, brMotor;
    private LinearSlide arm;

    private RobotParameters rP;

    private double maxSpeed;

    public void init(){
        ls = hardwareMap.dcMotor.get("ls");
        flMotor = hardwareMap.dcMotor.get("left");
        frMotor = hardwareMap.dcMotor.get("right");
        blMotor = hardwareMap.dcMotor.get("bLeft");
        brMotor = hardwareMap.dcMotor.get("bRight");

        arm = new LinearSlide(
                hardwareMap.get(DcMotor.class, "ls"),
                hardwareMap.get(Servo.class, "claw")
        );

        this.rP = new RobotParameters(flMotor, frMotor, blMotor, brMotor, 1440, 2, 60, ls, 8.75);
    }


    public void loop(){   // Most this code copy-pasted from the 2867 Freight Frenzy code (god I hate dependences) -Justin
        
        double leftInput = gamepad1.right_stick_x;
        double rightInput = gamepad1.left_stick_y;
        double leftSpd = leftInput + rightInput;
        double rightSpd = leftInput - rightInput;

        boolean slowLeft = gamepad1.left_bumper || gamepad1.left_trigger>0;
        boolean slowRight = gamepad1.right_bumper || gamepad1.right_trigger>0;

        if(slowRight && slowLeft){
            maxSpeed = 0.125; // L+R  = 12.5% speed
        } else if (slowRight) {
            maxSpeed = 0.25; // R    = 25% speed
        } else if (slowLeft) {
            maxSpeed = 0.5; // L    = 50% speed
        } else {
            maxSpeed = 1.0; // None = 100% speed
        }

        flMotor.setPower(leftSpd * Math.abs(leftSpd) * maxSpeed);
        frMotor.setPower(rightSpd * Math.abs(rightSpd) * maxSpeed);

        //// (see the next line of code)  We might not use bumber-slowing thingies -Justin
        boolean backArm = gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.back;

        ////////////////////////////////////////////////////////////////////////////////
//        if(gamepad1.a){
//            currentArmPosition = backArm ? ArmPosition.BACK_NEUTRAL : ArmPosition.NEUTRAL;
//        }else if(gamepad1.x){
//            currentArmPosition = backArm ? ArmPosition.BACK_ONE : ArmPosition.ONE;
//        }else if(gamepad1.y){
//            currentArmPosition = backArm ? ArmPosition.BACK_TWO : ArmPosition.TWO;
//        }else if(gamepad1.b){
//            currentArmPosition = backArm ? ArmPosition.BACK_THREE : ArmPosition.THREE;
//        }else if (gamepad1.left_stick_button || gamepad1.right_stick_button || gamepad1.start){
//            currentArmPosition = ArmPosition.CAP;
//        }
        ////////////////////////////////////////////////////////////////////////////////
        

        ////////////////////////////////////////////////////////////////////////
        ///////   TEMP CODEEEEEEEEEEEEE
        /////     MAKE THIS PART OF SOMETHING ELSE AFTER TESTINGGGGGG
        //////           -Justin, Oct 2022
        
        maxSpeed = 10;

        ////////////////////////////////////////////////

        flMotor.setPower(leftSpd * Math.abs(leftSpd) * maxSpeed);
        frMotor.setPower(rightSpd * Math.abs(rightSpd) * maxSpeed);

        backArm = gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.back;

        // Move to appropriate location based on button pressed and left bumper state
//        if(gamepad1.a){
//            currentArmPosition = backArm ? ArmPosition.BACK_NEUTRAL : ArmPosition.NEUTRAL;
//        }else if(gamepad1.x){
//            currentArmPosition = backArm ? ArmPosition.BACK_ONE : ArmPosition.ONE;
//        }else if(gamepad1.y){
//            currentArmPosition = backArm ? ArmPosition.BACK_TWO : ArmPosition.TWO;
//        }else if(gamepad1.b){
//            currentArmPosition = backArm ? ArmPosition.BACK_THREE : ArmPosition.THREE;
//        }else if (gamepad1.left_stick_button || gamepad1.right_stick_button || gamepad1.start){
//            currentArmPosition = ArmPosition.CAP;
//        }





    }
    

}
