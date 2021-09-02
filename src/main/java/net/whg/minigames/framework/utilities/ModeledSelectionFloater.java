package net.whg.minigames.framework.utilities;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

/**
 * A selection float that is decorated as a modeled entity. Uses ModelEngine to
 * provide the model that is used.
 */
public class ModeledSelectionFloater extends SelectionFloater {
    private final ActiveModel model;
    private final String modelName;
    private ModeledEntity modeledEntity;

    /**
     * Creates a new ModeledSelectionFloater. This will also immediately start the
     * "idle" animation.
     * 
     * @param location    - The location to spawn the floater at.
     * @param displayName - The display name to appear above the floater.
     * @param modelName   - The name of the model to load.
     */
    public ModeledSelectionFloater(Location location, String displayName, String modelName) {
        super(location, displayName);

        this.modelName = modelName;
        model = ModelEngineAPI.api.getModelManager().createActiveModel(modelName.toLowerCase());
    }

    @Override
    protected void decorateEntity(ArmorStand armorStand) {
        modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(armorStand);
        modeledEntity.addActiveModel(model);
        modeledEntity.detectPlayers();
        playAnimation("idle");
    }

    /**
     * Updates this model to be shown to all players currently in the area.
     */
    public void detectPlayers() {
        modeledEntity.detectPlayers();
    }

    /**
     * Triggers the model to play the given animation. If the animation loops, this
     * animation will continue to play forever. If the animation does not loop, the
     * animation will play once and then stop.
     * 
     * @param animationName  - The name of the animation to play.
     * @param lerpInTicks    - The number of ticks to lerp into the animation.
     * @param lerpOutTicks   - The number of ticks to lerp out of the animation.
     * @param animationSpeed - The playback multiplier speed.
     */
    public void playAnimation(String animationName, int lerpInTicks, int lerpOutTicks, double animationSpeed) {
        var name = String.format("animation.%s.%s", modelName, animationName);
        model.addState(name, lerpInTicks, lerpOutTicks, animationSpeed);
    }

    /**
     * Triggers the model to play the given animation. If the animation loops, this
     * animation will continue to play forever. If the animation does not loop, the
     * animation will play once and then stop.
     * 
     * @param animationName - The name of the animation to play.
     * @param lerpInTicks   - The number of ticks to lerp into the animation.
     * @param lerpOutTicks  - The number of ticks to lerp out of the animation.
     * @see #playAnimation(String, int, int, double)
     */
    public void playAnimation(String animationName, int lerpInTicks, int lerpOutTicks) {
        playAnimation(animationName, lerpInTicks, lerpOutTicks, 1.0);
    }

    /**
     * Triggers the model to play the given animation. If the animation loops, this
     * animation will continue to play forever. If the animation does not loop, the
     * animation will play once and then stop.
     * 
     * @param animationName - The name of the animation to play.
     * @see #playAnimation(String, int, int, double)
     */
    public void playAnimation(String animationName) {
        playAnimation(animationName, 0, 0, 1.0);
    }

    /**
     * Stops an animation that is currently being played.
     * 
     * @param animationName - The name of the animation to stop playing.
     * @param lerp          - Whether or not to lerp out of the animation or stop
     *                      instantly.
     */
    public void stopAnimation(String animationName, boolean lerp) {
        var name = String.format("animation.%s.%s", modelName, animationName);
        model.removeState(name, lerp);
    }
}
