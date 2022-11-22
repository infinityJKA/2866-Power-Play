

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.dependencies.LinearSlide;

@TeleOp
public class NewTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("left");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("bLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("right");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("bRight");
        DcMotor motorls = hardwareMap.dcMotor.get("ls");
        Servo claw = hardwareMap.servo.get("claw");
        double gripPosition = 0;
        double MIN_POS = 0, MAX_POS = 1;
        gripPosition = MAX_POS;
        boolean isOpen = true;


        LinearSlide.LinearPosition slidePos = LinearSlide.LinearPosition.ZERO;


        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
//            claw.setPosition(Range.clip(gripPosition, MIN_POS, MAX_POS));
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (Math.pow((y + x + rx),3) * 0.8) / denominator;
            double backLeftPower = (Math.pow((y - x + rx),3) * 0.8) / denominator;
            double frontRightPower = (Math.pow((y - x - rx),3) * 0.8) / denominator;
            double backRightPower = (Math.pow((y + x - rx),3) * 0.8) / denominator;

            if(gamepad1.dpad_left){
                frontLeftPower = -0.7;
                backLeftPower = 0.7;
                backRightPower = -0.7;
                frontRightPower = 0.7;
            }else if(gamepad1.dpad_right ){
                backRightPower = 0.7;
                frontRightPower = -0.7;
                frontLeftPower = 0.7;
                backLeftPower = -0.7;
            }
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            //if a is pressed, set LinearPosition to ZERO
            LinearSlide linslde = new LinearSlide(motorls, claw, this);
            if(gamepad1.a){
                linslde.moveToPosition(LinearSlide.LinearPosition.ZERO, 0.8);
                slidePos = LinearSlide.LinearPosition.ZERO;
            }
            else if (gamepad1.x) {
                linslde.moveToPosition(LinearSlide.LinearPosition.ONE, 0.8);
                slidePos = LinearSlide.LinearPosition.ONE;
            } else if (gamepad1.y) {
                linslde.moveToPosition(LinearSlide.LinearPosition.TWO, 0.8);
                slidePos = LinearSlide.LinearPosition.TWO;
            } else if (gamepad1.b) {
                linslde.moveToPosition(LinearSlide.LinearPosition.THREE, 0.8);
                slidePos = LinearSlide.LinearPosition.THREE;
            }

//            if (gamepad1.left_bumper) {
//                linslde.openClaw();
//            } else if (gamepad1.right_bumper) {
//                linslde.closeClaw();
//            }
            if (gamepad1.right_trigger > 0) {
                linslde.setPower(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0) {
                linslde.setPower(-gamepad1.left_trigger);
            }
            if (gamepad1.right_bumper && gripPosition < MAX_POS){
                gripPosition += 0.05;
            }
            else if (gamepad1.left_bumper && gripPosition > MIN_POS){
                gripPosition -= 0.05;
            }
            linslde.changeClawPos(gripPosition);

            telemetry.addData("Left trigger value: ", gamepad1.left_trigger);
            telemetry.update();
        }
    }
}
