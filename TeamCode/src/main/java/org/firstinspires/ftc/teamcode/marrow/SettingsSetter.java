package org.firstinspires.ftc.teamcode.marrow;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.settings.SettingsOpMode;

@TeleOp(name="Settings")
public class SettingsSetter extends SettingsOpMode {
    public enum alliance {RED, BLUE}
    @Override
    public void defineSettings() {
        add("debug_mode", "Debug Mode", new BooleanPrompt("Enable debug mode?", false));
        add("alliance", "Select Alliance", new OptionPrompt<>("Select alliance", alliance.RED, alliance.BLUE));
        add("controller", "Controller Count", new OptionPrompt<>("How Many Controllers", 1, 2));
    }
}