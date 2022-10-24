package org.firstinspires.ftc.teamcode.dependencies;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.enums.Levels;

// Abstract class for an arm that has a hand attached to it.
// As of right now the only extension of this class that is really used is ClawWithWristArm
// but that may change in the future.

public abstract class Arm {
    private final DcMotor armMotor;


    public enum ArmPosition {
        NEUTRAL(0), ONE(70), TWO(133), THREE(210),
        BACK_NEUTRAL(700), BACK_ONE(620), BACK_TWO(550), BACK_THREE(487),
        CAP(435);

        public final int ticks;
        ArmPosition(int i) {
            this.ticks = i;
        }
    }

    public int currentPosition;
    public Arm(DcMotor armMotor) {
        this.armMotor = armMotor;
        this.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (armMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            armMotor.setTargetPosition(0);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        this.currentPosition = 0;
    }

    public void moveToPosition(ArmPosition pos, double power){
        moveToPosition(pos.ticks, power);
    }

    public void moveToPosition(int ticks, double power){
        if (armMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
            armMotor.setTargetPosition(0);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        armMotor.setTargetPosition(ticks);
//        armMotor.setPower(Math.abs(ticks-currentPosition)>200 ? power*0.6 : power);
        armMotor.setPower(power);
        currentPosition = ticks;
        /*
        while(armMotor.isBusy()&&(Math.abs(armMotor.getTargetPosition()-armMotor.getCurrentPosition())>10)){
            Thread.yield();
        }
        armMotor.setPower(0);

         */
    }

    public void setArmPower(double power){
        this.armMotor.setPower(power);
        this.currentPosition = this.armMotor.getCurrentPosition();
    }
    public void stopArm(){
        this.armMotor.setPower(0);
    }

    public DcMotor getArmMotor() {
        return this.armMotor;
    }

    // MUST BE IMPLEMENTED IN CHILD CLASSES, AN ARM MUST HAVE A HAND THAT CAN
    // INTAKE, OUTTAKE, AND STOP INTAKING / OUTTAKING (literally 1984)
    public abstract void startIntake();

    public abstract void startOuttake();

    public abstract void stopHand();
}