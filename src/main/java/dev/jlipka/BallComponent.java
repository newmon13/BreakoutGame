package dev.jlipka;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static java.lang.Math.*;

public class BallComponent extends Component {
    private final int ANGLE_ADJUSTMENT = 3;
    private final int MIN_ANGLE = 15;
    PhysicsComponent physics;

    @Override
    public void onUpdate(double tpf) {
        checkOffScreen();
        limitVelocity();
        increaseBounceAngle();
    }

    private void checkOffScreen() {
        var visArea = getGameScene()
                .getViewport()
                .getVisibleArea();
        var ballEntity = getEntity().getBoundingBoxComponent();
        if (ballEntity.isOutside(visArea)) {
            physics.overwritePosition(FXGL.getAppCenter());
        }
    }

    private void limitVelocity() {
        if (abs(physics.getVelocityX()) < 5 * 60) {
            physics.setVelocityX(signum(physics.getVelocityX())
                    * 5 * 60);
        }

        if (abs(physics.getVelocityY()) > 5 * 60 * 2) {
            physics.setVelocityY(signum(physics.getVelocityY())
                    * 5 * 60);
        }
    }

    public void increaseBounceAngle() {
        double angle = Math.toDegrees(Math.atan2(physics.getVelocityY(), physics.getVelocityX()));

        if (Math.abs(angle) < MIN_ANGLE) {
            double sign = Math.signum(angle);
            double newAngle = sign * (Math.abs(angle) + ANGLE_ADJUSTMENT);

            double newVelocityX = Math.cos(Math.toRadians(newAngle));
            double newVelocityY = Math.sin(Math.toRadians(newAngle));
            double speed = physics.getLinearVelocity().magnitude();
            Point2D newVelocity = new Point2D(newVelocityX, newVelocityY).normalize().multiply(speed);
            physics.setLinearVelocity(newVelocity);
        }
    }
}
