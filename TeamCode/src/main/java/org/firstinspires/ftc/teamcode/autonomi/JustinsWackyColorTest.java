package org.firstinspires.ftc.teamcode.autonomi;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public class JustinsWackyColorTest {

    OpenCvWebcam webcam1 = null;

    @Override   // No idea why @Override is giving an error. (as of Nov. 3, 2023) - Justin
    public void init(){
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam1.setPipeline(new justinsGoofyPipeline());

        webcam1.openCameraDeviceAsync(
            new OpenCvCamera.AsyncCameraOpenListener() {
                public void onOpened() {
                    webcam1.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
                }

                public void onError(int errorCode) {
                }
            }
        );
    }

    @Override   // No idea why @Override is giving an error. (as of Nov. 3, 2023) - Justin
    public void loop(){
        // Hi, this is an empty loop.
        // Hi this is an empty loop, I'm Justin.
    }

    class justinsGoofyPipeline extends OpenCvPipeline {

        Mat YCbCr = new Mat(); // Note: I have no idea what a "YCbCr" means.
        Mat leftCrop; // Left side of the screen thingy
        Mat rightCrop; // The right part of the camera screen thing
        double leftavgfin; // idk man
        double rightavgfin; // ^same! so true!
        Mat outPut = new Mat(); // The word "Put" reminds me of golf, got the P capitalized for some reason, probably some deep allegory from something idk.
        Scalar rectColor = new Scalar(225.0, 0.0, 0.0); // "Scalar" is such a funny word.

        public Mat processFrame(Mat input){

            Imgproc.cvtColor(input,YCbCr,Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("The wrath of \"JustinsWackyColorTest.java\" dawns upon us.");

            Rect leftRect = new Rect(1,1,319,359);
            Rect rightRect = new Rect(320,1,319,359);

            input.copyTo(outPut);
            Imgproc.rectangle(outPut, leftRect, rectColor, 2);
            Imgproc.rectangle(outPut, rightRect, rectColor, 2);

            leftCrop = YCbCr.submat(leftRect);
            rightCrop = YCbCr.submat(rightRect);

            Core.extractChannel(leftCrop, leftCrop, 2);
            Core.extractChannel(rightCrop, rightCrop, 2);

            Scalar leftavg = Core.mean(leftCrop);
            Scalar rigthavg = Core.mean(rightCrop);

            return(outPut);
        }
    }

}
