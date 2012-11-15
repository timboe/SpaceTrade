package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.timboe.spacetrade.render.StarmapRender;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.world.Planet;

public class StarmapScreen implements Screen, InputProcessor {

	StarmapRender theStarmapRenderer;
	private Utility util = Utility.getUtility();
	
//	private ShapeRenderer g2 = new ShapeRenderer();
//    private Mesh mesh;
//	private Mesh sphereMesh;
//	private Texture texture;

	
	public StarmapScreen() {
		theStarmapRenderer = new StarmapRender();
		
		
//        mesh = new Mesh(true, 3, 3, 
//                new VertexAttribute(Usage.Position, 3, "a_position"),
//                new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
//                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
//
//        mesh.setVertices(new float[] { -0.5f, -0.5f, 0, Color.toFloatBits(255, 0, 0, 255), 0, 1,
//                                       0.5f, -0.5f, 0, Color.toFloatBits(0, 255, 0, 255), 1, 1,
//                                       0, 0.5f, 0, Color.toFloatBits(0, 0, 255, 255), 0.5f, 0 });
//                                       
//        mesh.setIndices(new short[] { 0, 1, 2 });
//        
//		InputStream in = Gdx.files.internal("data/sphere.obj").read();
//		sphereMesh = ObjLoader.loadObj(in);
//		try {
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		sphereMesh.scale(0.2f, 0.4f, 0.5f);
//		//sphereMesh.
//		System.out.println(sphereMesh.getNumVertices()+" "+ sphereMesh.getNumIndices());
//
//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		//texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

		theStarmapRenderer.theStarmap.newYear(10);
		for (Planet _p : theStarmapRenderer.theStarmap.thePlanets) {
			_p.printStat();
		}

	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		theStarmapRenderer.render(delta);
		util.getRightBar().render();
		
		
//		 Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		 Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
//		 texture.bind();
//		 sphereMesh.render(GL10.GL_TRIANGLE_STRIP);
//		 
//		 mesh.render(GL10.GL_TRIANGLES, 0, 3);

	}

	@Override
	public void resize(int width, int height) {
		theStarmapRenderer.resize(width, height);
		util.getRightBar().resize(width, height);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	//Input processor
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			util.getRightBar().handleClick(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
