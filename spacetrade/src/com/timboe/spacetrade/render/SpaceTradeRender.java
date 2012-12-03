package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.NormalMapShader;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.utility.Serialiser;

//This is the master Screen and Render class which other game windows all inherit from

public class SpaceTradeRender implements Screen {
	protected Table leftTable;
	protected Table masterTable;
	protected Stage stage;
	protected Stage secondaryStage = null;
	protected ShapeRenderer g2 = new ShapeRenderer();
	
	protected GestureDetector gestureDetector = null;
	protected InputMultiplexer inputMultiplex = null;
	
	protected SpriteBatch spriteBatch = new SpriteBatch();
	protected SpriteBatch spriteBatchFade = new SpriteBatch();

	protected Camera screenCam = new OrthographicCamera();
	protected Camera planetCam = new OrthographicCamera();
	private Image blackSquare;
	
	protected Matrix4 transform_BG;
	protected Matrix4 transform_FX;
	protected Vector3 lightPos = new Vector3(750,-700,0.005f);
	
	protected ShaderProgram shader;
	
	//moving
	protected static float xOffset=0;
	protected static float xTarget=0;
	protected float deltaTot;
	
//	protected DistanceFieldShader distanceFieldShader;
//	public static class DistanceFieldShader extends ShaderProgram {
//		public DistanceFieldShader () {
//			super(
//				Gdx.files.internal("data/skin/distanceField.vert"),
//				Gdx.files.internal("data/skin/distanceField.frag"));
//			if (!isCompiled()) {
//				throw new RuntimeException("Shader compilation failed:\n" + getLog());
//			}
//		}
//		
//		/** @param smoothing a value between 0 and 1 */
//		public void setSmoothing(float smoothing) {
//			float delta = 0.5f * MathUtils.clamp(smoothing, 0, 1);
//			setUniformf("u_lower", 0.5f - delta);
//			setUniformf("u_upper", 0.5f + delta);
//		}
//	}

	public Image getBlackSquare() {
		return blackSquare;
	}
	
	public SpaceTradeRender() {
		masterTable = new Table();
		masterTable.debug();
		masterTable.align(Align.bottom | Align.left);
		masterTable.setSize(SpaceTrade.CAMERA_WIDTH, SpaceTrade.CAMERA_HEIGHT);

		stage = new Stage();

		leftTable = new Table();
		if (SpaceTrade.debug == true) leftTable.debug();
		leftTable.align( Align.center);
		leftTable.setSize(SpaceTrade.GAME_WIDTH, SpaceTrade.GAME_HEIGHT);
		
		blackSquare = new Image(Textures.getBlackSquare());
		blackSquare.addAction( Actions.fadeOut(0));
		blackSquare.act(1);
		blackSquare.setSize(500, 500);
		
		shader = new ShaderProgram(NormalMapShader.mVertexShader, NormalMapShader.mFragmentShader);
		if (shader.isCompiled() == false) {
            Gdx.app.log("ShaderTest", shader.getLog());
            System.exit(0);
		}
		
		planetCam.translate(0,100,0);
		
		//init(); should be caller after superclass constructor
	}
	
	public void init() {
		masterTable.clear();
		masterTable.add(leftTable).width(SpaceTrade.GAME_WIDTH).height(SpaceTrade.GAME_HEIGHT);
		masterTable.add(RightBar.getRightBarTable()).width(SpaceTrade.GUI_WIDTH).height(SpaceTrade.GAME_HEIGHT);
		stage.clear();
		stage.addActor(masterTable);
	}
	
	public boolean needsGL20 () {
		return true;
	}
	
	public void dispose () {
		stage.dispose();
	}
	
	public void render(float delta) {
		deltaTot += delta;
		renderClear(delta);
		renderBackground(delta);
		renderFX(delta);
		renderForeground(delta);
		renderFade(delta);
	}
	
	protected void renderPlanet(float delta, Texture _t0, Texture _t1, WorldSize _ws) {
		
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		
		_t1.bind(1);
		_t0.bind(0);
		
		transform_FX = planetCam.combined.cpy();
		transform_FX.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_FX.translate(200+xOffset, 0, 0);
		transform_FX.rotate(1, 0, 0, -20);
		transform_FX.rotate(0, 1, 0, deltaTot*3);
		
		shader.begin();	
		shader.setUniformi("s_baseMap", 0);		
		shader.setUniformi("s_bumpMap", 1);
		shader.setUniformMatrix("u_matViewProjection", transform_FX);
		shader.setUniformf("u_lightPosition", lightPos.x, lightPos.y, lightPos.z);
		shader.setUniformf("u_eyePosition", planetCam.position.x, planetCam.position.y, planetCam.position.z);
		shader.setUniformf("u_ambient", 1f,1f,1f,1f);
		shader.setUniformf("u_specular", 0.4f,0.4f,0.4f,1f);
		shader.setUniformf("u_diffuse",0.5f,0.5f,0.5f,1f);
		shader.setUniformf("u_specularPower",200);
		Meshes.getPlanet(_ws).render(shader, GL20.GL_TRIANGLES);
		shader.end();
				
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl20.glDisable(GL20.GL_TEXTURE);
		
//		IntBuffer maxTextureSize = new IntBuffer();
//		Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, maxTextureSize);
//		Gdx.app.log("log",""+maxTextureSize);
		
//		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
//		transform_FX.rotate(0, 1, 0, delta);
//		shader.begin();
//        Vector3 lightPos = new Vector3(0,0,0.005f);
//        lightPos.x = Gdx.input.getX();
//        lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
//        shader.setUniformf("light", lightPos);
//        shader.setUniformMatrix("u_projTrans", transform_FX);
//        PlanetFX.getNormals(Player.getPlanetID()).bind(1);
//        PlanetFX.getTexture(Player.getPlanetID()).bind(0);
//        Meshes.getPlanet(Player.getPlanet().getSize()).render(shader, GL20.GL_TRIANGLES);
//        shader.end();
//        Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}
	
	protected void renderPlanetBackdrop() {
		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.setColor( Color.WHITE );
		spriteBatch.draw(Textures.getStarscape( Player.getPlanetID() ),-500+xOffset,0);
		spriteBatch.setColor( PlanetFX.getLandColor( Player.getPlanetID() ) ); //TODO put proper colour back in here
		if (Player.getPlanet().getSize() == WorldSize.Small) { //image is 705x800
			spriteBatch.draw(Textures.getPlanetBlur(),550+xOffset,80,580,635);
		} else if (Player.getPlanet().getSize() == WorldSize.Medium) {
			spriteBatch.draw(Textures.getPlanetBlur(),490+xOffset,0);
		} else if (Player.getPlanet().getSize() == WorldSize.Large) {
			spriteBatch.draw(Textures.getPlanetBlur(),455+xOffset,-20,765,840);
		}
		spriteBatch.setColor( Color.WHITE );
		spriteBatch.draw(Textures.getBlackSquare(),-500,-500, 1, 1); //BUG need to draw something to reset colour
		spriteBatch.end();
	}

	protected void renderFade(float delta) {
		if (ScreenFade.checkFade() == false) return;
		Matrix4 transform_Fade = screenCam.combined.cpy();
		transform_Fade.scale(2f/Gdx.graphics.getWidth(), 2f/Gdx.graphics.getHeight(), 0f);
		transform_Fade.translate(-Gdx.graphics.getWidth()/2f, -Gdx.graphics.getHeight()/2f, 0f);
		
		spriteBatchFade.setProjectionMatrix(transform_Fade);
		spriteBatchFade.begin();
		blackSquare.act(delta);
		blackSquare.draw(spriteBatchFade, 1f);
		spriteBatchFade.end();
	}

	protected void renderForeground(float delta) {
		stage.act(delta);
		stage.draw();
		if (SpaceTrade.debug == true) Table.drawDebug(stage);
		if (secondaryStage != null) {
			secondaryStage.act(delta);
			secondaryStage.draw();
		}		
	}

	
	protected void renderFX(float delta) {
		
	}

	protected void renderBackground(float delta) {
		
	}

	protected void renderClear(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void resize (int width, int height) {
		Gdx.app.log("Resize", "ReSize in Render ["+this+"] ("+width+","+height+")");
		//Constrain
//		stage.setViewport(SpaceTrade.CAMERA_WIDTH, SpaceTrade.CAMERA_HEIGHT, true);
//		stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
		//Dont cnstrain
		stage.setViewport(SpaceTrade.CAMERA_WIDTH, SpaceTrade.CAMERA_HEIGHT, false);

		
		blackSquare.setWidth(Gdx.graphics.getWidth()*10);
		blackSquare.setHeight(Gdx.graphics.getHeight()*10);
		
		//screenCam = new OrthographicCamera();
		transform_BG = screenCam.combined.cpy();
		transform_BG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_BG.translate(-SpaceTrade.CAMERA_WIDTH/2f, -SpaceTrade.CAMERA_HEIGHT/2f, 0f);
		
		//planetCam = new OrthographicCamera();

	
		if (secondaryStage != null) {
			secondaryStage.setCamera(stage.getCamera());
		}
	}
	
	@Override
	public void show() {
		if (secondaryStage == null && gestureDetector == null) {
			Gdx.input.setInputProcessor( stage );
		} else {
			if (inputMultiplex == null) {
				inputMultiplex = new InputMultiplexer( stage );
				if (secondaryStage != null) inputMultiplex.addProcessor(secondaryStage);
				if (gestureDetector != null) inputMultiplex.addProcessor(gestureDetector);
			}
			Gdx.input.setInputProcessor( inputMultiplex );
		}
//		hookStage();
		init();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
//		unHookStage();
	}
	

	@Override
	public void pause() {
//		unHookStage();
//		Starmap.unHookListners();
		Serialiser.saveState();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		//ADD RESTORE LINE
//		hookStage();		
	}
	
}
