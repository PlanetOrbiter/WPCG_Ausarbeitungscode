/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base.animatedmesh;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Container for an animated mesh in the jMonkey scene graph.
 */
public class AnimatedMesh extends Node {

    /**
     * Scene graph node.
     */
    private Node node;

    /**
     * Animation channel for the player wpcg.model.
     */
    private AnimChannel channel;

    /**
     * This controller is used to define the movement of the mesh over time.
     */
    private AnimationController animationController;

    public AnimatedMesh(Node node, AnimationController animationController) {
        this.node = node;
        this.animationController = animationController;
        AnimControl control = node.getControl(AnimControl.class);
        channel = control.createChannel();
        channel.setAnim("walk");
    }

    /**
     * Call this method to advance in the animation based on the current controller.
     */
    public void update(float time) {
        animationController.move(time);
        getNode().setLocalTranslation(animationController.getPose().pos);
        getNode().setLocalRotation(animationController.getPose().rot);
    }

    // +++ GETTER/SETTER ++++++++++++++++

    public Vector3f getDirection() {
        return animationController.getDirection();
    }

    public Node getNode() {
        return node;
    }

    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
    }
}
