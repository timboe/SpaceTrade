package com.timboe.spacetrade;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.timboe.spacetrade.render.PlanetFX;

//import de.swagner.gdx.obj.normalmap.helper.ObjLoaderTan;
//import de.swagner.gdx.obj.normalmap.shader.NormalMapShader;

public class NormalMapTest implements Screen {
	
	private Mesh mesh;
	private Texture textureDiff;
	private Texture textureNorm;
	private Matrix4 projection = new Matrix4();
	private Matrix3 modelWorld = new Matrix3();
	private Matrix4 view = new Matrix4();
	private Matrix4 model = new Matrix4();
	private Matrix4 model2 = new Matrix4();
	private Matrix4 combined = new Matrix4();
	private Vector3 yAxis = new Vector3(0, 1, 0).nor();
	private Vector3 xAxis = new Vector3(1, 0, 0).nor();
	private Vector3 light = new Vector3(-2f, 1f, 10f);
	private float angle = 45;
	private ShaderProgram shader;
	private PerspectiveCamera camera;
	
	protected Matrix4 transform_FX;
	OrthographicCamera screenCam;
    Vector2 resolution = new Vector2();



	@Override
	public void show() {

		InputStream in = Gdx.files.internal("data/sphere.obj").read();
		//InputStream in = Gdx.files.internal("data/Infinite-Level_02.OBJ").read();
		
		//mesh = ObjLoader.loadObj(in);
		mesh = ObjLoaderTan.loadObj(in);

		Gdx.app.log("dbg","mesh:"+mesh);
		mesh.getVertexAttribute(Usage.Position).alias = "a_vertex";
		mesh.getVertexAttribute(Usage.Normal).alias = "a_normal";
		mesh.getVertexAttribute(10).alias = "a_tangent";
		mesh.getVertexAttribute(11).alias = "a_binormal";
		mesh.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texcoord0";
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mesh.scale(300f, 350f, 300f);	
		mesh.scale(0.9f, 0.9f, 0.9f);	

		
		shader = new ShaderProgram(NormalMapShader.mVertexShader, NormalMapShader.mFragmentShader);
		if (shader.isCompiled() == false) {
            Gdx.app.log("ShaderTest", shader.getLog());
            System.exit(0);
		}
		
		createTexture();
		camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(0.0f, 0.0f, 2.1f);
		camera.lookAt(0, 0, 0);
        
		camera.update();
	        
		
		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Enable backface culling
		Gdx.graphics.getGL20().glCullFace(GL20.GL_BACK);
		Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

		Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		Gdx.graphics.getGL20().glDepthFunc(GL20.GL_GEQUAL);
		Gdx.graphics.getGL20().glClearDepthf(0.0f);
		
		Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	
		
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
		textureDiff.bind();
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);
		textureNorm.bind();
		
	}
	
	int N = 0;
    private void createTexture() {
    	++N;
    	
    	
    	textureDiff = PlanetFX.getTexture(N);
        textureNorm = PlanetFX.getNormals(N);
    	
//        textureDiff = new Texture(Gdx.files.internal("data/moonTex.jpg"),true);
//        textureDiff.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
//        textureNorm = new Texture(Gdx.files.internal("data/moonNormal.jpg"),true);
//        textureNorm.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
        
        
   }


	@Override
	public void dispose() {

	}

	@Override
	public void pause() { 

	}
	
	float Z = 0.005f;
    Vector3 lightPos = new Vector3(0,-700,Z );

	@Override
	public void render(float delta) {
		
		
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
	
//		angle -= Gdx.graphics.getDeltaTime() * 30f;
//		model.idt();
//		model2.setToRotation(yAxis, angle);
//		model.mul(model2);
//		model2.setToScaling(0.7f, 0.9f, 0.7f);
//		model.mul(model2);
//		
//		model2.setToTranslation(0.0f, 0.1f, 0.0f);
//		model.mul(model2);
//		
//				
//		modelWorld.setToRotation(angle);
//		
//    	light.x = Gdx.input.getX();
//    	light.y = Gdx.input.getY();
//    	
//        camera.update();
			
		
		transform_FX.rotate(0, 1, 0, delta*10);
		
        //lightPos.x =  Gdx.input.getX();
       // lightPos.y =  (Gdx.graphics.getHeight() - Gdx.input.getY());
		Gdx.app.log("in render", ""+lightPos);
		
		//if (Gdx.input.isTouched()) {
	        lightPos.x =  Gdx.input.getX();
	        //lightPos.y =  (Gdx.graphics.getHeight() - Gdx.input.getY());

			
		//}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			createTexture();

		}

		if (Gdx.input.isKeyPressed(Keys.R)) lightPos.z += 0.005f;
		if (Gdx.input.isKeyPressed(Keys.F)) lightPos.z -= 0.005f;
		
		if (Gdx.input.isKeyPressed(Keys.W)) lightPos.y += 10; 
		if (Gdx.input.isKeyPressed(Keys.A)) lightPos.x -= 10;
		if (Gdx.input.isKeyPressed(Keys.S)) lightPos.y -= 10; 
		if (Gdx.input.isKeyPressed(Keys.D)) lightPos.x += 10;

		screenCam.update();



		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
		textureDiff.bind();
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);
		textureNorm.bind();
		
		// NormalMap
		shader.begin();	
		

		shader.setUniformi("s_baseMap", 0);		
		shader.setUniformi("s_bumpMap", 1);
//		shader.setUniformMatrix("u_matViewInverse",  camera.combined.inv());
		shader.setUniformMatrix("u_matViewProjection", transform_FX);//model
		shader.setUniformf("u_lightPosition", lightPos.x, lightPos.y, lightPos.z);
		shader.setUniformf("u_eyePosition", screenCam.position.x, screenCam.position.y, -screenCam.position.z); //camera
		
	//	shader.setUniformf("v_res", resolution);
	//shader.setUniformf("u_light", lightPos.x, lightPos.y, lightPos.z);
		
		shader.setUniformf("u_ambient", 1f,1f,1f,1f);
		shader.setUniformf("u_specular", 0.5f * PlanetFX.getLandColor(N).r,
				0.5f * PlanetFX.getLandColor(N).g,
				0.5f * PlanetFX.getLandColor(N).b,
				1f);
		//shader.setUniformf("u_specular", 1f,1f,1f,1f);
		shader.setUniformf("u_specular", 0.4f,0.4f,0.4f,1f);

		shader.setUniformf("u_diffuse",0.5f,0.5f,0.5f,1f);
		shader.setUniformf("u_specularPower",200);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = Gdx.graphics.getWidth();
		
		screenCam = new OrthographicCamera();
		screenCam.translate(0,100);

		
		transform_FX = screenCam.combined.cpy();
		transform_FX.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_FX.translate(200, 0, 0);
		transform_FX.rotate(1, 0, 0, -10);
		
        resolution.set(width, height);

	}

	@Override
	public void resume() {

	}




	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}

