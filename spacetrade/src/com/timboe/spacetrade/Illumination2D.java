package com.timboe.spacetrade;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Random;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.timboe.spacetrade.utility.Rnd;
import com.timboe.spacetrade.utility.SimplexNoise;
 
/**
 * Simple illumination model with shaders in LibGDX.
 * @author davedes
 */
public class Illumination2D implements Screen {   
	String _V = "7";
 
        Texture texture, texture_n;
       
        boolean flipY;
        Texture normalBase;
        OrthographicCamera cam;
        SpriteBatch fxBatch, batch;
        

        Texture worldTexture;
        Texture worldHeight;
        Texture worldNormal;
        Mesh sphereMesh;
        float Delta=0;
        
        private static Rnd rnd2 = new Rnd();

        Matrix4 transform = new Matrix4();
 
        Random rnd = new Random();
 
        // position of our light
        final Vector3 DEFAULT_LIGHT_POS = new Vector3(0f, 0f, 0.07f);
        // the color of our light
//        Sun at sunrise or sunset  .71, .49, .36
//        Direct sun at noon        .75, .75, .68
//        Sun through clouds/haze   .74, .75, .75
        final Vector3 DEFAULT_LIGHT_COLOR = new Vector3(0.75f, 0.75f, 0.68f);
        // the ambient color (color to use when unlit)
        final Vector3 DEFAULT_AMBIENT_COLOR = new Vector3(1f, 1f, 1f);
        // the attenuation factor: x=constant, y=linear, z=quadratic
        final Vector3 DEFAULT_ATTENUATION = new Vector3(0.1f, 1f, 1f);
        // the ambient intensity (brightness to use when unlit)
        final float DEFAULT_AMBIENT_INTENSITY = 0.6f;
        final float DEFAULT_STRENGTH = 1f;
       
        final Color NORMAL_VCOLOR = new Color(1f,1f,1f,DEFAULT_STRENGTH);
       
        // the position of our light in 3D space
        Vector3 lightPos = new Vector3(DEFAULT_LIGHT_POS);
        // the resolution of our game/graphics
        Vector2 resolution = new Vector2();
        // the current attenuation
        Vector3 attenuation = new Vector3(DEFAULT_ATTENUATION);
        // the current ambient intensity
        float ambientIntensity = DEFAULT_AMBIENT_INTENSITY;
        float strength = DEFAULT_STRENGTH;
       
        // whether to use attenuation/shadows
        boolean useShadow = true;
 
        // whether to use lambert shading (with our normal map)
        boolean useNormals = true;
       
        DecimalFormat DEC_FMT = new DecimalFormat("0.00000");
 
        ShaderProgram program;
        ShaderProgram texShader;

		//NOISE VARS
		float persistence = 0.6f;
		float frequency = 2f*(1f/512f);
		float amplitude  = 1f;
		int octaves = 6;
		int randomseed = 2 + 1*1;
		int random_r = 0;
		float waterHeight = 0.1f;
		final float maxU = 512f/2;
		final float maxV = 256f/2;
		float repeat = 256f;
        BitmapFont font;
       
        @SuppressWarnings("unused")
		private int texWidth, texHeight;
        final String TEXT = _V+") Use number keys to adjust parameters:\n" +
                        "1: Randomize Ambient Color\n" +
                        "2: Randomize Ambient Intensity {0}\n" +
                        "3: Randomize Light Color\n" +
                        "4/5: Increase/decrease constant attenuation: {1}\n" +
                        "6/7: Increase/decrease linear attenuation: {2}\n" +
                        "8/9: Increase/decrease quadratic attenuation: {3}\n" +
                        "0: Reset parameters\n" +
                        "RIGHT/LEFT: Increase/decrease normal map intensity: {4}\n" +
                        "UP/DOWN: Increase/decrease lightDir.z: {5}\n\n" +
                        "S toggles attenuation, N toggles normal shading\n" +
                        "T to toggle textures";

        private Texture rock, rock_n, teapot, teapot_n;
       
//        public void create() {
//    
//        }
       
        private Vector3 rndColor() {
                return new Vector3(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
        }
 
        private ShaderProgram workingShader() {
        	String vertexShader = "#ifdef GL_ES\n"
        			+ "#define LOWP lowp\n"
        			+ "#define MEDP mediump\n"
        			+ "#define HIGP highp\n"
        			+ "#else\n"
        			+ "#define LOWP\n"
        			+ "#define MEDP\n"
        			+ "#define HIGP\n"
        			+ "#endif\n"
        			+ "\n"
        			+ "attribute vec4 a_position;\n"
        			+ "attribute MEDP vec2 a_texCoord0;\n"
        			+ "\n"
        			+ "uniform mat4 u_projView;\n"
        			+ "\n"
        			+ "varying MEDP vec2 texCoords;\n"
        			+ "\n"
        			+ "void main() {\n"
        			+ "	texCoords = a_texCoord0;\n"
        			+ "	gl_Position = u_projView * a_position;\n"
        			+ "}";
        	
        	String fragmentShader = "#ifdef GL_ES\n"
        			+ "#define LOWP lowp\n"
        			+ "#define MEDP mediump\n"
        			+ "#define HIGP highp\n"
        			+ "precision lowp float;\n"
        			+ "#else\n"
        			+ "#define LOWP\n"
        			+ "#define MEDP\n"
        			+ "#define HIGP\n"
        			+ "#endif\n"
        			+ "\n"
        			+ "uniform sampler2D u_normals;\n"
        			+ "\n"
        			+ "varying MEDP vec2 texCoords;\n"
        			+ "\n"
        			+ "void main() {\n"
        			+ "	gl_FragColor = texture2D(u_normals, texCoords);\n"
        			+ "}";
        	
            ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
            // u_proj and u_trans will not be active but SpriteBatch will still try to set them...
            ShaderProgram.pedantic = false;
            if (program.isCompiled() == false)
                    throw new IllegalArgumentException("couldn't compile shader: "
                                    + program.getLog());
            return program;
        }
        
        private ShaderProgram basicShader()  {
        	String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
        			+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
        			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
        			+ "uniform mat4 u_projTrans;\n" //
        			+ "varying vec4 v_color;\n" //
        			+ "varying vec2 v_texCoords;\n" //
        			+ "\n" //
        			+ "void main()\n" //
        			+ "{\n" //
        			+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
        			+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
        			+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
        			+ "}\n";
        	
        	String fragmentShader = "#ifdef GL_ES\n" //
        			+ "#define LOWP lowp\n" //
        			+ "precision mediump float;\n" //
        			+ "#else\n" //
        			+ "#define LOWP \n" //
        			+ "#endif\n" //
        			+ "varying LOWP vec4 v_color;\n" //
        			+ "varying vec2 v_texCoords;\n" //
        			+ "uniform sampler2D u_texture;\n" //
        			+ "void main()\n"//
        			+ "{\n" //
        			+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
        			+ "}";
        	
            ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
            // u_proj and u_trans will not be active but SpriteBatch will still try to set them...
            ShaderProgram.pedantic = false;
            if (program.isCompiled() == false)
                    throw new IllegalArgumentException("couldn't compile shader: "
                                    + program.getLog());
            return program;
		}
        
        private ShaderProgram createShader() {
                // see the code here: http://pastebin.com/7fkh1ax8
                // simple illumination model using ambient, diffuse (lambert) and attenuation
                // see here: http://nccastaff.bournemouth.ac.uk/jmacey/CGF/slides/IlluminationModels4up.pdf
                String vert = "#ifdef GL_ES\n"
            			+ "#define LOWP lowp\n"
            			+ "#define MEDP mediump\n"
            			+ "#define HIGP highp\n"
            			//+ "precision lowp float;\n"
            			+ "#else\n"
            			+ "#define LOWP\n"
            			+ "#define MEDP\n"
            			+ "#define HIGP\n"
            			+ "#endif\n"
                				+ "attribute HIGP vec4 a_position;\n" //
                                + "attribute HIGP vec4 a_color;\n" //
                                + "attribute MEDP vec2 a_texCoord0;\n" //
//                                + "uniform MEDP mat4 u_proj;\n" //
//                                + "uniform MEDP mat4 u_trans;\n" //
                                + "uniform MEDP mat4 u_projTrans;\n" //
                                + "varying MEDP vec4 v_color;\n" //
                                + "varying MEDP vec2 v_texCoords;\n" //
                                + "\n" //
                                + "void main()\n" //
                                + "{\n" //
                                + "   v_color = a_color;\n" //
                                + "   v_texCoords = a_texCoord0;\n" //
                                + "   gl_Position =  u_projTrans * a_position;\n" //
                                + "}\n";
               
                
                String frag = "#ifdef GL_ES\n"
                			+ "#define LOWP lowp\n"
                			+ "#define MEDP mediump\n"
                			+ "#define HIGP highp\n"
                			+ "precision lowp float;\n"
                			+ "#else\n"
                			+ "#define LOWP\n"
                			+ "#define MEDP\n"
                			+ "#define HIGP\n"
                			+ "#endif\n"
                			//"#ifdef GL_ES\n" +
                            //"precision mediump float;\n" +
                            //"#endif\n" +
                            +   "varying HIGP vec4 v_color;\n" +
                                "varying MEDP vec2 v_texCoords;\n" +
                                "uniform LOWP sampler2D u_texture;\n" +
                                "uniform LOWP sampler2D u_normals;\n" +
                                "uniform MEDP vec3 light;\n" +
                                "uniform MEDP vec2 resolution;\n" +
                                "uniform MEDP vec3 attenuation;\n" +
                                "MEDP vec3 ambientColor = vec3(1.0, 1.0, 1.0);\n" +
                                "MEDP float ambientIntensity = 0.6; \n" +
                                "MEDP vec3 lightColor = vec3(0.75, 0.75, 0.68);\n" +
//                                "uniform bool useNormals;\n" +
//                                "uniform bool useShadow;\n" +
                                "MEDP float strength = 1.0;\n" +
//                                "uniform bool yInvert;\n"+
                                "\n" +
                                "void main() {\n" +
                                "       //sample color & normals from our textures\n" +
                                "       MEDP vec4 color = texture2D(u_texture, v_texCoords.st); \n" +
                                "       MEDP vec3 nColor = texture2D(u_normals, v_texCoords.st).rgb; \n\n" +
                                
//                                "       //some bump map programs will need the Y value flipped..\n" +
//                                "       nColor.g = yInvert ? 1.0 - nColor.g : nColor.g;\n\n" +

                                //"       //this is for debugging purposes, allowing us to lower the intensity of our bump map\n" +
                                //"       vec3 nBase = vec3(0.5, 0.5, 1.0);\n" +
                                //"       nColor = mix(nBase, nColor, strength);\n\n" +
                                
                                "       //normals need to be converted to [-1.0, 1.0] range and normalized\n" +
                                "       MEDP vec3 normal = normalize(nColor * 2.0 - 1.0);\n\n" +
                                
                                "       //here we do a simple distance calculation\n" +
                                "       MEDP vec3 deltaPos = MEDP vec3( (light.xy - gl_FragCoord.xy) / resolution.xy, light.z );\n\n" +
                                "       MEDP vec3 lightDir = normalize(deltaPos);\n" +
                                "       MEDP float lambert = clamp(dot(normal, lightDir), 0.0, 1.0);\n" +
                                "       \n" +
                                
                                "       //now let's get a nice little falloff\n" +
                                "       MEDP float d = sqrt(dot(deltaPos, deltaPos));"+
                                "       \n" +
                                
                          //usShaddows (att = 1 if false)
                                "       MEDP float att =  1.0 / ( attenuation.x + (attenuation.y*d) + (attenuation.z*d*d) );\n" +
                                "       \n" +
                                
                       // ORIG//"       MEDP vec3 result = (ambientColor * ambientIntensity) + (lightColor.rgb * lambert) * att;\n" +
                                "       MEDP vec3 result = vec3(1.0,.0,1.0);\n" +
          
                                "       result *= color.rgb;\n" +
                                "       \n" +
                                
                                //"       gl_FragColor = v_color ;\n" +
                   				"	gl_FragColor = texture2D(u_texture, v_texCoords);\n"+

                                "}";
               // System.out.println("VERTEX PROGRAM:\n------------\n\n"+vert);
                //System.out.println("FRAGMENT PROGRAM:\n------------\n\n"+frag);
                ShaderProgram program = new ShaderProgram(vert, frag);
                // u_proj and u_trans will not be active but SpriteBatch will still try to set them...
                ShaderProgram.pedantic = false;
                if (program.isCompiled() == false)
                        throw new IllegalArgumentException("couldn't compile shader: "
                                        + program.getLog());
 
                // set resolution vector
                resolution.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
 
                // we are only using this many uniforms for testing purposes...!!
                program.begin();
                program.setUniformi("u_texture", 0);
                program.setUniformi("u_normals", 1);
                program.setUniformf("light", lightPos);
                //program.setUniformf("strength", strength);
                //program.setUniformf("ambientIntensity", ambientIntensity);
                //program.setUniformf("ambientColor", DEFAULT_AMBIENT_COLOR);
                program.setUniformf("resolution", resolution);
                //program.setUniformf("lightColor", DEFAULT_LIGHT_COLOR);
                program.setUniformf("attenuation", attenuation);
                //program.setUniformi("useShadow", useShadow ? 1 : 0);
                //program.setUniformi("useNormals", useNormals ? 1 : 0);
                //program.setUniformi("yInvert", flipY ? 1 : 0);
                program.end();
 
                return program;
        }
 
        public void dispose() {
                fxBatch.dispose();
                batch.dispose();
                texture.dispose();
                texture_n.dispose();
        }
 
        public void resize(int width, int height) {
                cam.setToOrtho(false, width, height);
                resolution.set(width, height);
                //program.setUniformf("resolution", resolution);
        }
 
        public void pause() {
        }
 
        public void resume() {
        }
        
		@Override
		public void render(float delta) {
			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//	        batch.begin();
//	        String str = MessageFormat.format(TEXT, ambientIntensity,
//	                          attenuation.x, attenuation.y, DEC_FMT.format(attenuation.z),
//	                          strength, lightPos.z);
//	        font.drawMultiLine(batch, str, 10, Gdx.graphics.getHeight()-10);
//            batch.end();
    		Delta += delta;
    		Camera cam = new OrthographicCamera();
    		Matrix4 transform_FG = new Matrix4();
    		transform_FG = cam.combined.cpy();
    		transform_FG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
    		transform_FG.translate(-330, -60, 0);
    		//transform_FG.rotate(0, 0, 1, 180);
    		transform_FG.rotate(1, 0, 0, -10);
    		transform_FG.rotate(0, 1, 0, Delta*10f);
    		transform_FG.rotate(0, 1, 0, -Gdx.input.getX()/2f);
    		
    		

            

            final int IMG_Y = 500;
        
//            // start our FX batch, which will bind our shader program
            lightPos.x = Gdx.input.getX();
            lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
            fxBatch.begin();   
            // update our uniforms
            program.setUniformf("light", lightPos);
            texture_n.bind(1);
            texture.bind(0);
            fxBatch.draw(texture, texWidth*2 + 20, IMG_Y);
            fxBatch.end();		
            

         
            //TESTING HERE! //XXX
            
    		GLCommon gl = Gdx.gl;
//
//    		gl.glEnable(GL20.GL_DEPTH_TEST);
    		gl.glEnable(GL20.GL_CULL_FACE);
//    		gl.glEnable(GL20.GL_TEXTURE_2D);

            //third render

    		program.begin();
           // program.setUniformf("light", lightPos);
 
//          //program.setUniformMatrix("u_projView", transform_FG);
//          // program.setUniformi("u_diffuse", 0);
            texture_n.bind(1);
            texture.bind(0);
    		
            program.setUniformMatrix("u_projTrans", transform_FG);
            program.setUniformi("u_texture", 0);

            sphereMesh.render(program, GL20.GL_TRIANGLES);
            program.end();
//    		gl.glDisable(GL20.GL_DEPTH_TEST);
//    		gl.glDisable(GL20.GL_TEXTURE_2D);
    		
    		transform_FG = new Matrix4();
    		transform_FG = cam.combined.cpy();
    		transform_FG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
    		transform_FG.translate(+400, -60, 0);
    		//transform_FG.rotate(0, 0, 1, 180);
    		transform_FG.rotate(1, 0, 0, -10);
    		transform_FG.rotate(0, 1, 0, Delta*10f);
    		transform_FG.rotate(0, 1, 0, -Gdx.input.getX()/2f);
    		
    		texShader.begin();
            texture_n.bind(1);
            texture.bind(0);
    		texShader.setUniformMatrix("u_projView", transform_FG);
    		texShader.setUniformi("u_normals", 1);
            sphereMesh.render(texShader, GL20.GL_TRIANGLES);
            texShader.end();
            
    		gl.glDisable(GL20.GL_CULL_FACE);

//    		
//    		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);      
//    		Gdx.gl20.glBlendFunc(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//    		Gdx.gl20.glEnable(GL20.GL_BLEND);
    		
//          gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//          gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//
//          gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//          gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
//          
//   		gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
//   		gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
  		
//          
    		
    		//Gdx.app.log("OPENGL1", ""+gl.glGetError() );
    		//Gdx.app.log("OPENGL2", ""+gl.glGetError() );

  
		}
		
		@Override
		public void hide() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void show() {
			
            // load our textures
            teapot = new Texture(Gdx.files.internal("data/rock.png"));
            teapot_n = new Texture(Gdx.files.internal("data/rock_n.png"));

            texture = teapot;
            texture_n = teapot_n;
            
    		InputStream in = Gdx.files.internal("data/sphereMesh.obj").read();
    		sphereMesh = ObjLoader.loadObj(in);
    		try { in.close(); }
    		catch (IOException e) { e.printStackTrace(); }
//    		
//    		sphereMeshLoad.setAutoBind(true);
////    		
////
//    		sphereMeshLoad.scale(300f, 350f, 300f);
//    		System.out.println(sphereMeshLoad.getMaxVertices()+" "+ sphereMeshLoad.getNumIndices());
//    		
//    		int size = 1;
//    		sphereMesh = new Mesh(
//    				VertexDataType.VertexArray,
//    				false, 
//    				sphereMeshLoad.getNumVertices(), 
//    				sphereMeshLoad.getNumIndices(),
//    				new VertexAttribute(Usage.Position, 4, ShaderProgram.POSITION_ATTRIBUTE),
//    				new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
//    				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
//    		
//    		short[] indices = new short[ sphereMeshLoad.getNumIndices() ];
//    		float[] verticies = new float[ 30720 ];
//    		
//    		sphereMeshLoad.getIndices(indices);
//    		sphereMeshLoad.getVertices(verticies);
//    		
//    		sphereMesh.setIndices( indices );
//    		sphereMesh.setVertices( verticies );
////    		
//    		System.out.println(sphereMesh.getNumVertices()+" "+ sphereMesh.getNumIndices());


//    		InputStream in = Gdx.files.internal("data/sphere.obj").read();
//    		sphereMesh = ObjLoaderTan.loadObj(in);
//    		Gdx.app.log("dbg","mesh:"+sphereMesh);
//    		sphereMesh.getVertexAttribute(Usage.Position).alias = "a_vertex";
//    		sphereMesh.getVertexAttribute(Usage.Normal).alias = "a_normal";
//    		sphereMesh.getVertexAttribute(10).alias = "a_tangent";
//    		sphereMesh.getVertexAttribute(11).alias = "a_binormal";
//    		sphereMesh.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texcoord0";
//    		try {
//    			in.close();
//    		} catch (IOException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
    		
    		sphereMesh.scale(300f, 350f, 300f);
            
            
            
    		texShader =workingShader(); //new ShaderProgram(Gdx.files.internal("data/tex-vs.glsl"),
    				//Gdx.files.internal("data/tex-fs.glsl"));
           
            //we only use this to show what the strength-adjusted normal map looks like on screen
            Pixmap pix = new Pixmap(1, 1, Format.RGB565);
            pix.setColor(0.5f, 0.5f, 1.0f, 1.0f);
            pix.fill();
            normalBase = new Texture(pix);
           
            texWidth = texture.getWidth();
            texHeight = texture.getHeight();
           
            // a simple 2D orthographic camera
            cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.setToOrtho(false);

            // create our shader program...
        //    program = createShader();
            program = createShader();

            // now we create our sprite batch for our shader
            fxBatch = new SpriteBatch(100, program);
            // setShader is needed; perhaps this is a LibGDX bug?
            fxBatch.setShader(program);
            fxBatch.setProjectionMatrix(cam.combined);
            fxBatch.setTransformMatrix(transform);

            // usually we would just use a single batch for our application,
            // but for demonstration let's also show the un-affected image
//            batch = new SpriteBatch(100);
//            batch.setProjectionMatrix(cam.combined);
//            batch.setTransformMatrix(transform);

            // quick little input for debugging -- press S to toggle shadows, N to
            // toggle normals
            Gdx.input.setInputProcessor(new InputAdapter() {
                    public boolean keyDown(int key) {
                            if (key == Keys.S) {
                                    useShadow = !useShadow;
                                    return true;
                            } else if (key == Keys.N) {
                                    useNormals = !useNormals;
                                    return true;
                            } else if (key == Keys.NUM_1) {
                                    program.begin();
                                    program.setUniformf("ambientColor", rndColor());
                                    program.end();
                                    return true;
                            } else if (key == Keys.NUM_2) {
                                    ambientIntensity = rnd.nextFloat();
                                    return true;
                            } else if (key == Keys.NUM_3) {
                                    program.begin();
                                    program.setUniformf("lightColor", rndColor());
                                    program.end();
                                    return true;
                            } else if (key == Keys.NUM_0) {
                                    attenuation.set(DEFAULT_ATTENUATION);
                                    ambientIntensity = DEFAULT_AMBIENT_INTENSITY;
                                    lightPos.set(DEFAULT_LIGHT_POS);
                                    strength = DEFAULT_STRENGTH;
                                    program.begin();
                                    program.setUniformf("lightColor", DEFAULT_LIGHT_COLOR);
                                    program.setUniformf("ambientColor", DEFAULT_AMBIENT_COLOR);
                                    program.setUniformf("ambientIntensity", ambientIntensity);
                                    program.setUniformf("attenuation", attenuation);
                                    program.setUniformf("lightPos", lightPos);
                                    program.setUniformf("strength", strength);
                                    program.end();
                            } else if (key == Keys.T) {
                                    texture = texture==teapot ? rock : teapot;
                                    texture_n = texture_n==teapot_n ? rock_n : teapot_n;
                                    flipY = texture==rock;
                                    texWidth = texture.getWidth();
                                    texHeight = texture.getHeight();
                                    program.begin();
                                    program.setUniformi("yInvert", flipY ? 1 : 0);
                                    program.end();
                            }
                            return false;
                    }
                    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    	genWorld();
                    	return false;
                    }
            });
           
            font = new BitmapFont();
         
    		//earthTexture = new Texture(Gdx.files.internal("data/moonTex.jpg"), Format.RGB565, false);
    		//earthTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

    		genWorld();
			
		}
		
		public void genWorld() {
        	++random_r;
        	randomseed = 2 + random_r*random_r;
        	rnd2.setSeed(randomseed);
			
    		octaves = rnd2.getRandI(4) + 3;//3-6
    		frequency = (rnd2.getRandI(3) + 1) * (1f/512f); //1-3
    		persistence = (rnd2.getRandI(3) + 4) * 0.1f; //0.4 - 0.6
    		
	        Pixmap worldPixOcean = new Pixmap((int) maxU, (int) maxV, Format.RGBA8888);
	        Pixmap worldPixLand = new Pixmap((int) maxU, (int) maxV, Format.RGBA8888);
	        Pixmap worldPixHeight = new Pixmap((int) maxU, (int) maxV, Format.RGB888);
	        Pixmap worldPixNormal = new Pixmap((int) maxU, (int) maxV, Format.RGB888);
            
	        float min=999;
            float max=-999;
            float[][] data = new float[(int) maxU][(int) maxV];
	        float theta = 0 ;
	        float r = maxV;
	        float phi = 0;
	        for (int _y = 0; _y < maxV; ++_y) {
	        	for (int _x = 0; _x < maxU; ++_x) {
	        		phi = (float) (((float)_x / maxU) * 2 * Math.PI);
	        		//theta = (float) (2 * Math.atan( Math.exp((float)(_y) / (r/Math.PI)) ) - (Math.PI/2f));
	        		theta = 2f*(float) (2 * Math.atan( Math.exp((float)(_y) / (r/Math.PI)) ) - (Math.PI/2f));

	        		float X = (float) (r * Math.sin(theta) * Math.cos(phi));
	        		float Y = (float) (r * Math.cos(theta));
	        		float Z = (float) (r * Math.sin(theta) * Math.sin(phi));
	          	
	        		float h = getHeight(X,Y,Z);
	              	min = Math.min(min, h);
	              	max = Math.max(max,h);
	              	if (h > 1) h = 1;
	              	if (h < -1) h = -1;
	              	
	              	//int y_coord = (int) (_y +  maxV);
	              	//if (y_coord >= maxV) y_coord -= maxV;
	              	data[_x][_y] = h;
	        	}
	        	//Gdx.app.log("toSphere", "V:"+_y+" -> theta:"+Math.toDegrees(theta)+" phi:"+Math.toDegrees(phi));
	        }
		
        	Color world = getWorldColor();
        	Color ocean = getWorldColor();
	        for (int _y = 0; _y < maxV; ++_y) {
	        	for (int _x = 0; _x < maxU; ++_x) {
		        	float height = toHeight(data[_x][_y]); //map 0 to 1
		        	worldPixHeight.setColor(height, height, height, 1f);
		        	worldPixHeight.drawPixel(_x, _y);
		        	

		        	
		        	worldPixLand.setColor(world.r * height, world.g * height, world.b * height, 1f);
		        	worldPixLand.drawPixel(_x, _y);
		
		        	float opaque = 1f;
		        	waterHeight=0;
					if (data[_x][_y] > waterHeight) opaque = 0f;
		        	//else if (data[_x][_y] > -waterHeight) opaque = 1f - (((data[_x][_y] + waterHeight)/2f) * 10f);
		        	else { //map 
		        		opaque = 1-(data[_x][_y] + 1f) ;
		        	}
		        	worldPixOcean.setColor(ocean.r, ocean.g, ocean.b, opaque);
		        	worldPixOcean.drawPixel(_x, _y);
		        	
		        	//normal
		        	int _xp1 = _x + 1;
		        	if (_xp1 == maxU) _xp1 = 0;
		        	int _xm1 = _x - 1;
		        	if (_xm1 == -1) _xm1 = (int) (maxU - 1);
		        	int _yp1 = _y + 1;
		        	if (_yp1 == maxV) _yp1 = 0;
		        	int _ym1 = _y - 1;
		        	if (_ym1 == -1) _ym1 = (int) (maxV - 1);
                	float hxm = toNormHeight(data[_xm1][_y]);
                	float hxp = toNormHeight(data[_xp1][_y]);
                	float hym = toNormHeight(data[_x][_ym1]);
                	float hyp = toNormHeight(data[_x][_yp1]);
                	Vector3 vx = new Vector3(0, 1, hxm - hxp);
                	Vector3 vy = new Vector3(1, 0, hym - hyp);
                	vx.crs(vy); // vx X vy
                	vx.nor(); //normalise
                	float R= ((vx.x + 1f)/2f);  
                	float G = ((vx.y + 1f)/2f);  
                	float B = 1 - ((vx.z + 1f)/2f);  
                	worldPixNormal.setColor(R, G, B, 1f);
                    worldPixNormal.drawPixel(_x, _y);
	        	}
	        }
	        worldPixLand.drawPixmap(worldPixOcean, 0, 0);
	     
            worldTexture = new Texture(worldPixLand);
            worldHeight = new Texture(worldPixHeight);
            worldNormal = new Texture(worldPixNormal);

            worldPixOcean.dispose();
            worldPixLand.dispose();
            worldPixHeight.dispose();
            worldPixNormal.dispose(); 
            Gdx.app.log("Noise","Min:"+(min)+" max:"+(max));
		}
		
 
	        
		
		
        public Color getWorldColor() {
        	int nC = 0;
        	boolean cR = false, cG = false, cB = false;
        	while (nC == 0 || nC == 3) {
        		nC = 0;
        		cR=false;
        		cG=false;
        		cB=false;
        		if (rnd2.getRandChance(0.5f) == true) {
        			++nC;
        			cR=true;
        		}
        		if (rnd2.getRandChance(0.5f) == true) {
        			++nC;
        			cG=true;
        		}
        		if (rnd2.getRandChance(0.5f) == true) {
        			++nC;
        			cB=true;
        		}
        	}
        	float r = 0,g = 0, b=0f;
        	if (cR == true) r = 0.5f + (rnd2.getRandF()/2f);
        	if (cG == true) g = 0.5f + (rnd2.getRandF()/2f);
        	if (cB == true) b = 0.5f + (rnd2.getRandF()/2f);
        	return new Color(r, g, b, 1f);
        }
		
		public float mapToSphere(float _x, float _y, float m_dxloop) {
			//m_dxloop instead of maxU
        	float magOfEffect = (float) Math.abs(Math.sin(((_y+1) / maxV) * Math.PI));
        	float range = magOfEffect * m_dxloop;
        	return ((m_dxloop/2f) - (range/2f)) + (range * (_x/m_dxloop));
		}
		
		public float toLand(float h) {
        	float green = 1f;
			if (h > waterHeight) green = 1f;
        	else if (h > 0f) green = h * 10f;
        	else if (h < 0f) green = 0;
        	return green;
		}
		
		public float toHeight(float _h) {
			_h += 1;
			_h /= 2f;
        	if (_h < 0f) _h = 0f;
        	if (_h > 1f) _h = 1f;
        	return _h;
		}
		
		public float toNormHeight(float _h) {
        	if (_h < 0) _h = 0;
        	if (_h > 1f) _h = 1f;
        	return _h;
		}
		
		
		public float getHeight(float _x, float _y, float _z) {
			return amplitude * total(_x, _y, _z);
		}
		
		public float total(float _i, float _j, float _k) {
		    //properties of one octave (changing each loop)
			float _t = 0.0f;
			float _amplitude = 1;
			float _freq = frequency;

		    for(int oc = 0; oc < octaves; ++oc) {
		        _t += getValue3D(_k * _freq + randomseed, _j * _freq + randomseed, _i * _freq + randomseed) * _amplitude;
		        _amplitude *= persistence;
		        _freq *= 2;
		    }

		    return _t;
		}

		
		public float getValue3D(float _x, float _y, float _z) {
			return SimplexNoise.noise(_x,  _y, _z);
		}
		
		
		public float getHeight(float _x, float _y) {
			return amplitude * total(_x, _y);
		}
		
		public float total(float _i, float _j) {
		    //properties of one octave (changing each loop)
			float _t = 0.0f;
			float _amplitude = 1;
			float _freq = frequency;

		    for(int k = 0; k < octaves; ++k) {
		        _t += getValueTile(_j * _freq + randomseed, _i * _freq + randomseed, _freq) * _amplitude;
		        _amplitude *= persistence;
		        _freq *= 2;
		    }

		    return _t;
		}

		
		public float getValue(float _x, float _y) {
			return SimplexNoise.noise(_x,  _y);
		}

		public float getValueTile(float _x, float _y, float _freq) {
		
//			s = (double) localx / (double) m_dxloop;
//			t = (double) localy / (double) m_dyloop;
//
//			nx = m_loopx0 + Math.cos(s * 2 * Math.PI)*m_dxloop/(2*Math.PI);
//			ny = m_loopy0 + Math.cos(t * 2 * Math.PI)*m_dyloop/(2*Math.PI);
//			nz = m_loopx0 + Math.sin(s * 2 * Math.PI)*m_dxloop/(2*Math.PI);
//			nw = m_loopy0 + Math.sin(t * 2 * Math.PI)*m_dyloop/(2*Math.PI);
//
//			noise = SimplexNoise4D.noise(nx* scalingfactor, ny * scalingfactor, nz * scalingfactor, nw * scalingfactor);
//			
//			
//			

        	float magOfEffect = (float) Math.abs(Math.sin(((_y+1) / maxV) * Math.PI));
        	float range = magOfEffect * repeat * _freq;
        	//return ((m_dxloop/2f) - (range/2f)) + (range * (_x/m_dxloop));
			
			float scale = 1f;
			
			float m_loopx0 = 0f;
			float m_loopy0 = 0f;
			float m_dxloop = range;//(repeat * _freq);
			float m_dyloop = (repeat * _freq);
			
           float _x2 = ((512f/2f) - (range/2f)) + (range * ((_x*_freq)/range));

			
			float s = (float) _x2 / m_dxloop;
			float t = (float) _y / m_dyloop;
			
			float nx = (float) (m_loopx0 + Math.cos(s * 2 * Math.PI)*m_dxloop/(2*Math.PI));
			float ny = (float) (m_loopy0 + Math.cos(t * 2 * Math.PI)*m_dyloop/(2*Math.PI));
			float nz = (float) (m_loopx0 + Math.sin(s * 2 * Math.PI)*m_dxloop/(2*Math.PI));
			float nw = (float) (m_loopy0 + Math.sin(t * 2 * Math.PI)*m_dyloop/(2*Math.PI));
			
			
//        	float s = (float)_x / maxU;
//        	float t = (float)_y / maxV;
//        	
//            final float x1 = 0;//_x;
//            //final float x2 = _x + (1f/maxU);
//            
//            final float y1 = 0;// _y;
//           // final float y2 = _y + (1f/maxV);
//            
//        	float dx = (_x/maxU);
//        	float dy = (_y/maxV);
//        	
//        	float nx = (float) (x1+Math.cos(s*2*Math.PI)*dx/(2*Math.PI));
//        	float ny = (float) (y1+Math.cos(t*2*Math.PI)*dy/(2*Math.PI));
//        	float nz = (float) (x1+Math.sin(s*2*Math.PI)*dx/(2*Math.PI));
//        	float nw = (float) (y1+Math.sin(t*2*Math.PI)*dy/(2*Math.PI));
//        	
			return SimplexNoise.noise(nx*scale, ny*scale, nz*scale, nw*scale);
			
			//return SimplexNoise.noise(_x, _y);
			
//			int Xint = (int)_x;
//			int Yint = (int)_y;
//			float Xfrac = _x - Xint;
//			float Yfrac = _y - Yint;
//
//			//noise values
//		  	float n01 = noise(Xint-1, Yint-1);
//			float n02 = noise(Xint+1, Yint-1);
//			float n03 = noise(Xint-1, Yint+1);
//			float n04 = noise(Xint+1, Yint+1);
//			float n05 = noise(Xint-1, Yint);
//			float n06 = noise(Xint+1, Yint);
//			float n07 = noise(Xint, Yint-1);
//			float n08 = noise(Xint, Yint+1);
//			float n09 = noise(Xint, Yint);
//			
//			float n12 = noise(Xint+2, Yint-1);
//			float n14 = noise(Xint+2, Yint+1);
//			float n16 = noise(Xint+2, Yint);
//			
//			float n23 = noise(Xint-1, Yint+2);
//			float n24 = noise(Xint+1, Yint+2);
//			float n28 = noise(Xint, Yint+2);
//			
//			float n34 = noise(Xint+2, Yint+2);
//
//		    //find the noise values of the four corners
//			float x0y0 = 0.0625f*(n01+n02+n03+n04) + 0.125f*(n05+n06+n07+n08) + 0.25f*(n09);  
//			float x1y0 = 0.0625f*(n07+n12+n08+n14) + 0.125f*(n09+n16+n02+n04) + 0.25f*(n06);  
//			float x0y1 = 0.0625f*(n05+n06+n23+n24) + 0.125f*(n03+n04+n09+n28) + 0.25f*(n08);  
//			float x1y1 = 0.0625f*(n09+n16+n28+n34) + 0.125f*(n08+n14+n06+n24) + 0.25f*(n04);  
//
//		    //interpolate between those values according to the x and y fractions
//			float v1 = interpolate(x0y0, x1y0, Xfrac); //interpolate in x direction (y)
//			float v2 = interpolate(x0y1, x1y1, Xfrac); //interpolate in x direction (y+1)
//		    float fin = interpolate(v1, v2, Yfrac);  //interpolate in y direction
//
//		    return fin;
		}
		
		public float interpolate(float _x, float _y, float _a) {
			float negA = 1.0f - _a;
			float negASqr = negA * negA;
			float fac1 = 3.0f * (negASqr) - 2.0f * (negASqr * negA);
			float aSqr = _a * _a;
			float fac2 = 3.0f * aSqr - 2.0f * (aSqr * _a);

		    return _x * fac1 + _y * fac2; //add the weighted factors
		}
		
		float noise(int _x, int _y) {
		    int n = _x + _y * 57;
		    n = (n << 13) ^ n;
		    int t = (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff;
		    return (float) (1.0f - (double)t * 0.931322574615478515625e-9);/// 1073741824.0);
		}

}