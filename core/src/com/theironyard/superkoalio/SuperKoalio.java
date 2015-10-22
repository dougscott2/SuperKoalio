package com.theironyard.superkoalio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SuperKoalio extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion stand;
    TextureRegion jump;
    Animation walk;
    FitViewport viewPort;
    OrthogonalTiledMapRenderer renderer;
   // OrthographicCamera camera;

    float x = 0;
    float y = 0;
    float xv = 0; //velocity variables
    float yv = 0;
    float time = 0;
    boolean canJump;

    final float MAX_VELOCITY = 250;
    final int WIDTH = 18;
    final int HEIGHT = 26;
    final int DRAW_WIDTH = WIDTH *3;
    final int DRAW_HEIGHT = HEIGHT * 3;
    final int GRAVITY = -50;
    final float MAX_JUMP_VELOCITY = 1000;

    @Override
    public void create () {

        batch = new SpriteBatch();
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TmxMapLoader loader = new TmxMapLoader();
        renderer = new OrthogonalTiledMapRenderer(loader.load("level1.tmx"));
       //camera = new OrthographicCamera();


        Texture sheet = new Texture("koalio.png");
        TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT); //array of an array...k
        stand = tiles[0][0];
        jump = tiles[0][1];
        walk = new Animation(0.1f, tiles[0][2], tiles[0][3], tiles[0][4]);


    }

    @Override
    public void render () {
        move();
        draw();
    }

    @Override
    public void resize(int width, int height){
        viewPort.update(width, height);
       // camera.setToOrtho(true, width, height);
    }

    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) { //WASD controls
           if (canJump) {
               yv = MAX_JUMP_VELOCITY;
               canJump = false;
           }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            yv = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) || Gdx.input.isKeyPressed(Input.Keys.D)){
            xv = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            xv = MAX_VELOCITY * -1;
        }

        yv += GRAVITY;

        x += xv * Gdx.graphics.getDeltaTime();  //getting delta time (time since last frame)
        y += yv * Gdx.graphics.getDeltaTime();

        if (y < 0){
            y = 0;
            canJump = true;
        }
        xv *= 0.8;
        yv *= 0.9;

    }
    void draw() {
       // renderer.setView(camera);

        renderer.render();


        time += Gdx.graphics.getDeltaTime();
        TextureRegion img;
        if (y > 0){
            img = jump;
        }else if (Math.round(xv) != 0){
            img = walk.getKeyFrame(time, true);
        }
        else {
            img = stand;
        }
        Gdx.gl.glClearColor(0.5f, 1, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (xv >= 0) {
            batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        } else {
            batch.draw(img, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT); //flipping image and having it draw turned around
        }
        batch.end();
    }
}
