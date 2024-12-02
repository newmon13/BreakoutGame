package dev.jlipka;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class BreakoutFactory implements EntityFactory {

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        PhysicsComponent component = new PhysicsComponent();
        component.setBodyType(BodyType.DYNAMIC);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution(1.0f);
        fixtureDef.density(0.3f);
        component.setFixtureDef(fixtureDef);
        component.setOnPhysicsInitialized(() -> {
            component.setLinearVelocity(100, -100);
        });

        return entityBuilder(data).type(EntityType.BALL)
                .view(new Circle(5,5,5, Color.RED))
                .bbox(BoundingShape.circle(5))
                .with(component)
                .with(new CollidableComponent(true))
                .with(new BallComponent())
                .build();
    }

    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        PhysicsComponent component = new PhysicsComponent();
        component.setBodyType(BodyType.KINEMATIC);

        return entityBuilder(data).type(EntityType.PADDLE)
                .viewWithBBox(new Rectangle(100,20 , Color.rgb(16,228,223)))
                .with(component)
                .with(new CollidableComponent(true))
                .with(new PaddleComponent())
                .build();
    }

    @Spawns("tile")
    public Entity newTile(SpawnData data) {
        Color color = data.get("color");
        PhysicsComponent component = new PhysicsComponent();
        component.setBodyType(BodyType.STATIC);
        return entityBuilder(data).type(EntityType.TILE)
                .viewWithBBox(new Rectangle(95,18, color))
                .with(new CollidableComponent(true))
                .with(new TileComponent())
                .with(component)
                .build();
    }
}
