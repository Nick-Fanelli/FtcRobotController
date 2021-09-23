package org.firstinspires.ftc.teamcode;

import com.bravenatorsrobotics.drive.AbstractDrive;
import com.bravenatorsrobotics.drive.FourWheelDrive;
import com.bravenatorsrobotics.drive.MecanumDrive;
import com.bravenatorsrobotics.drive.TwoWheelDrive;
import com.bravenatorsrobotics.operation.AutonomousMode;
import com.bravenatorsrobotics.vision.TensorFlowObjectDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.tensorflow.lite.Tensor;

@Autonomous(name="Autonomous")
public class Auto extends AutonomousMode<MecanumDrive> {

    private Config config;
    private TensorFlowObjectDetector objectDetector;

    public Auto() { super(new Specifications()); }

    public enum DuckPosition {
        LEFT,
        CENTER,
        RIGHT
    }
    private DuckPosition duckPosition = DuckPosition.LEFT;

    @Override
    public void OnInitialize() {
        config = new Config(hardwareMap.appContext);

        telemetry.addData("Status", "Updating Recognitions");
        telemetry.update();

        // TODO: REPLACE WITH ACCURATE THRESHOLDS
        // Position Thresholds
        float leftThreshold = 200.0f;
        float rightThreshold = 700.0f;

        // Detect for the duck
        objectDetector.Initialize();

        // Continue Detection Until Start
        while(!isStarted()) {
            // Scan and update registry of recognitions
            objectDetector.UpdateRecognitions();

            boolean isFound = false; // Used for telemetry

            // Detect what position the duck is in
            if(objectDetector.GetRecognition(TensorFlowObjectDetector.ObjectType.DUCK) != null) {
                Recognition duck = objectDetector.GetRecognition(TensorFlowObjectDetector.ObjectType.DUCK);

                // Check left position of duck by threshold
                if(duck.getLeft() <= leftThreshold)
                    duckPosition = DuckPosition.LEFT;
                else if(duck.getLeft() >= rightThreshold)
                    duckPosition = DuckPosition.RIGHT;
                else
                    duckPosition = DuckPosition.CENTER;

                isFound = true;
            }

            // Update Duck Position to Telemetry
            telemetry.addData("Is Duck Found", isFound ? "True" : "False");
            if(isFound)
                telemetry.addData("Duck Position", duckPosition.name());
            telemetry.update();

            sleep(1);
        }
    }

    @Override
    public void OnStart() {
        // Negate the movement modifier if alliance color is blue
        int movementModifier = config.allianceColor == Config.AllianceColor.RED ? 1 : -1;

        switch (config.startingPosition) {
            case WAREHOUSE:
                RunWarehouse(movementModifier);
                break;
            case STORAGE_UNIT:
                RunStorageUnit(movementModifier);
                break;
        }
    }

    private void RunWarehouse(int movementModifier) {

    }

    private void RunStorageUnit(int movementModifier) {

    }

    @Override
    public void OnStop() {
        robot.drive.Stop();
    }
}
