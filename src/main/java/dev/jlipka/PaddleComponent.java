package dev.jlipka;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PaddleComponent extends Component {
    PhysicsComponent physics;
    private final double speed = 400;
    void moveLeft() {
        if (entity.getX() >= (speed / 60)){
            physics.setVelocityX(-speed);
        } else {
            stop();
        }
    }

    void moveRight() {
        if (entity.getX() + entity.getWidth() <= FXGL.getAppWidth() - speed / 60 ){
            physics.setVelocityX(speed);
        } else {
            stop();
        }
    }

    public void stop() {
        physics.setLinearVelocity(0, 0);
    }
}
