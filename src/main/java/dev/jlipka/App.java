package dev.jlipka;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class App extends GameApplication {

    private PaddleComponent player;

    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setHeight(400);
        gameSettings.setTitle("Breakout");
        gameSettings.setVersion("1.0");
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("left") {
            @Override
            protected void onAction() {
                player.moveLeft();
            }
            @Override
            protected void onActionEnd() {
                player.stop();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("right") {
            @Override
            protected void onAction() {
                player.moveRight();
            }
            @Override
            protected void onActionEnd() {
                player.stop();
            }
        }, KeyCode.D);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("playerScore", 0);
    }

    @Override
    protected void initUI() {
        HBox box = new HBox();
        Label scoreLabel = new Label("Score: ");
        box.getChildren().add(scoreLabel);
        scoreLabel.textProperty().bind(getWorldProperties().intProperty("playerScore").asString());
        FXGL.getGameScene().addUINode(box);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.rgb(100,100,100));
        getGameWorld().addEntityFactory(new BreakoutFactory());
        initScreenBounds();
        initGameObjects();
        createTileLayout(5,7);
    }

    private void showWinningPrompt() {
        getDialogService().showMessageBox("You won!");
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.TILE) {
            @Override
            protected void onHitBoxTrigger(Entity a, Entity b, HitBox boxA, HitBox boxB) {
                b.removeFromWorld();
            }
        });
    }

    private void initScreenBounds() {
        Entity walls = entityBuilder()
                .type(EntityType.WALL)
                .collidable()
                .buildScreenBounds(150);
        getGameWorld().addEntity(walls);
    }

    private void initGameObjects() {
        Entity paddle = spawn(
                "paddle",
                (double) getAppWidth() / 2,
                (double) (3 * getAppHeight()) / 4
        );
        Entity ball = spawn(
                "ball",
                (double) getAppWidth() / 2,
                (double) (getAppHeight()) / 2
        );
        player = paddle.getComponent(PaddleComponent.class);
    }

    private void createTileLayout(int rows, int columns) {
        int startX = getAppWidth() / 32;
        int startY = getAppHeight() / 32;
        int horizontalMargin = 5;
        int verticalMargin = 5;

        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};

        for (int i = 0; i < rows; i++) {
            int currentX = startX;
            Color currentColor = colors[i];
            for (int j = 0; j < columns; j++) {
                spawn("tile", new SpawnData(currentX, startY).put("color", currentColor));
                currentX += 100 + horizontalMargin;
            }
            startY += 20 + verticalMargin;
        }
    }
}
