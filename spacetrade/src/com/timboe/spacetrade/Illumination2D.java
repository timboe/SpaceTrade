package com.timboe.spacetrade;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Random;
 
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
 
/**
 * Simple illumination model with shaders in LibGDX.
 * @author davedes
 */
public class Illumination2D implements Screen {   
 
        Texture texture, texture_n;
       
        boolean flipY;
        Texture normalBase;
        OrthographicCamera cam;
        SpriteBatch fxBatch, batch;
        
        Texture earthTexture;

        Texture worldTexture;
        Texture worldHeight;
        Texture worldNormal;
        Mesh sphereMesh;
        float Delta=0;

       
        Matrix4 transform = new Matrix4();
 
        Random rnd = new Random();
 
        // position of our light
        final Vector3 DEFAULT_LIGHT_POS = new Vector3(0f, 0f, 0.07f);
        // the color of our light
        final Vector3 DEFAULT_LIGHT_COLOR = new Vector3(1f, 0.7f, 0.6f);
        // the ambient color (color to use when unlit)
        final Vector3 DEFAULT_AMBIENT_COLOR = new Vector3(0.3f, 0.3f, 1f);
        // the attenuation factor: x=constant, y=linear, z=quadratic
        final Vector3 DEFAULT_ATTENUATION = new Vector3(0.4f, 3f, 20f);
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

 
        BitmapFont font;
       
        private int texWidth, texHeight;
       
        final String TEXT = "Use number keys to adjust parameters:\n" +
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
 
        private ShaderProgram createShader() {
                // see the code here: http://pastebin.com/7fkh1ax8
                // simple illumination model using ambient, diffuse (lambert) and attenuation
                // see here: http://nccastaff.bournemouth.ac.uk/jmacey/CGF/slides/IlluminationModels4up.pdf
                String vert = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                                + "uniform mat4 u_proj;\n" //
                                + "uniform mat4 u_trans;\n" //
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
               
                String frag = "#ifdef GL_ES\n" +
                                "precision mediump float;\n" +
                                "#endif\n" +
                                "varying vec4 v_color;\n" +
                                "varying vec2 v_texCoords;\n" +
                               
                                "uniform sampler2D u_texture;\n" +
                                "uniform sampler2D u_normals;\n" +
                                "uniform vec3 light;\n" +
                                "uniform vec3 ambientColor;\n" +
                                "uniform float ambientIntensity; \n" +
                                "uniform vec2 resolution;\n" +
                                "uniform vec3 lightColor;\n" +
                                "uniform bool useNormals;\n" +
                                "uniform bool useShadow;\n" +
                                "uniform vec3 attenuation;\n" +
                                "uniform float strength;\n" +
                                "uniform bool yInvert;\n"+
                                "\n" +
                                "void main() {\n" +
                                "       //sample color & normals from our textures\n" +
                                "       vec4 color = texture2D(u_texture, v_texCoords.st);\n" +
                                "       vec3 nColor = texture2D(u_normals, v_texCoords.st).rgb;\n\n" +
                                "       //some bump map programs will need the Y value flipped..\n" +
                                "       nColor.g = yInvert ? 1.0 - nColor.g : nColor.g;\n\n" +
                                "       //this is for debugging purposes, allowing us to lower the intensity of our bump map\n" +
                                "       vec3 nBase = vec3(0.5, 0.5, 1.0);\n" +
                                "       nColor = mix(nBase, nColor, strength);\n\n" +
                                "       //normals need to be converted to [-1.0, 1.0] range and normalized\n" +
                                "       vec3 normal = normalize(nColor * 2.0 - 1.0);\n\n" +
                                "       //here we do a simple distance calculation\n" +
                                "       vec3 deltaPos = vec3( (light.xy - gl_FragCoord.xy) / resolution.xy, light.z );\n\n" +
                                "       vec3 lightDir = normalize(deltaPos);\n" +
                                "       float lambert = useNormals ? clamp(dot(normal, lightDir), 0.0, 1.0) : 1.0;\n" +
                                "       \n" +
                                "       //now let's get a nice little falloff\n" +
                                "       float d = sqrt(dot(deltaPos, deltaPos));"+
                                "       \n" +
                                "       float att = useShadow ? 1.0 / ( attenuation.x + (attenuation.y*d) + (attenuation.z*d*d) ) : 1.0;\n" +
                                "       \n" +
                                "       vec3 result = (ambientColor * ambientIntensity) + (lightColor.rgb * lambert) * att;\n" +
                                "       result *= color.rgb;\n" +
                                "       \n" +
                                "       gl_FragColor = v_color * vec4(result, color.a);\n" +
                                "}";
                System.out.println("VERTEX PROGRAM:\n------------\n\n"+vert);
                System.out.println("FRAGMENT PROGRAM:\n------------\n\n"+frag);
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
                program.setUniformf("strength", strength);
                program.setUniformf("ambientIntensity", ambientIntensity);
                program.setUniformf("ambientColor", DEFAULT_AMBIENT_COLOR);
                program.setUniformf("resolution", resolution);
                program.setUniformf("lightColor", DEFAULT_LIGHT_COLOR);
                program.setUniformf("attenuation", attenuation);
                program.setUniformi("useShadow", useShadow ? 1 : 0);
                program.setUniformi("useNormals", useNormals ? 1 : 0);
                program.setUniformi("yInvert", flipY ? 1 : 0);
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
                program.setUniformf("resolution", resolution);
        }
 
        public void pause() {
        }
 
        public void resume() {
        }
        
		@Override
		public void render(float delta) {
			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            
//            // draw our sprites without any effects
//           
            NORMAL_VCOLOR.a = 1.0f - strength;
//
            final int IMG_Y = 0;
//           
//            batch.begin();
//
//            //let's first simulate our resulting normal map by blending a blue square atop it
//            //we also could have achieved this with glTexEnv in the fixed function pipeline
//            batch.draw(worldHeight, 300, 800-256);
//            batch.draw(worldTexture, 300+512, 800-256);
//            batch.draw(worldNormal, 300, 800-512);
//            
//            batch.draw(texture_n, texWidth + 10, IMG_Y);
//            batch.setColor(NORMAL_VCOLOR);
//            batch.draw(normalBase, texWidth + 10, IMG_Y, texWidth, texHeight);
//            batch.setColor(Color.WHITE);
//            batch.draw(texture, 0, IMG_Y);
//            batch.end();
//
//            //now let's simulate how our normal map will be sampled using strength
//            //we can do this simply by blending a blue fill overtop
//           
//
//            
//
//           
//            // start our FX batch, which will bind our shader program
            fxBatch.begin();
//           
//            // get y-down light position based on mouse/touch
            lightPos.x = Gdx.input.getX();
            lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
//           
            // handle attenuation input
            if (Gdx.input.isKeyPressed(Keys.NUM_4)) {
                    attenuation.x += 0.025f;
            } else if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
                    attenuation.x -= 0.025f;
                    if (attenuation.x < 0)
                            attenuation.x = 0;
            } else if (Gdx.input.isKeyPressed(Keys.NUM_6)) {
                    attenuation.y += 0.25f;
            } else if (Gdx.input.isKeyPressed(Keys.NUM_7)) {
                    attenuation.y -= 0.25f;
                    if (attenuation.y < 0)
                            attenuation.y = 0;
            } else if (Gdx.input.isKeyPressed(Keys.NUM_8)) {
                    attenuation.z += 0.25f;
            } else if (Gdx.input.isKeyPressed(Keys.NUM_9)) {
                    attenuation.z -= 0.25f;
                    if (attenuation.z < 0)
                            attenuation.z = 0;
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                    strength += 0.025f;
                    if (strength > 1f)
                            strength = 1f;
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                    strength -= 0.025f;
                    if (strength < 0)
                            strength = 0;
            } else if (Gdx.input.isKeyPressed(Keys.UP)) {
                    lightPos.z += 0.0025f;
            } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                    lightPos.z -= 0.0025f;
            }
//           
            // update our uniforms
            program.setUniformf("ambientIntensity", ambientIntensity);
            program.setUniformf("attenuation", attenuation);
            program.setUniformf("light", lightPos);
            program.setUniformi("useNormals", useNormals ? 1 : 0);
            program.setUniformi("useShadow", useShadow ? 1 : 0);
            program.setUniformf("strength", strength);
           
            // bind the normal first at texture1
            texture_n.bind(1);
           
            // bind the actual texture at texture0
            texture.bind(0);
           
            // we bind texture0 second since draw(texture) will end up binding it at
            // texture0...
            fxBatch.draw(texture, texWidth*2 + 20, IMG_Y);
           // fxBatch.draw(worldTexture, 500+270, 400);

            fxBatch.end();			
//            
//            //second render
//            fxBatch.begin();
//            // get y-down light position based on mouse/touch
//            lightPos.x = Gdx.input.getX();
//            lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
//            program.setUniformf("ambientIntensity", ambientIntensity);
//            program.setUniformf("attenuation", attenuation);
//            program.setUniformf("light", lightPos);
//            program.setUniformi("useNormals", useNormals ? 1 : 0);
//            program.setUniformi("useShadow", useShadow ? 1 : 0);
//            program.setUniformf("strength", strength);
//            worldNormal.bind(1);
//            worldTexture.bind(0);
//            fxBatch.draw(worldTexture,300+512, 800-512);
//            fxBatch.end();
            
            
    		GLCommon gl = Gdx.gl;
//    		gl.glEnable(GL20.GL_DEPTH_TEST);
//    		gl.glEnable(GL20.GL_CULL_FACE);
//    		gl.glEnable(GL20.GL_TEXTURE_2D);
            //third render
            Delta += delta;
    		Camera cam = new OrthographicCamera();
    		Matrix4 transform_FG = new Matrix4();
    		transform_FG = cam.combined.cpy();
    		transform_FG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
    		transform_FG.translate(-50, 0f, 0f);
    		//transform_FG.rotate(0, 0, 1, 180);
    		transform_FG.rotate(0, 1, 0, Delta*10f);
    		transform_FG.rotate(0, 1, 0, -Gdx.input.getX()/5f);
    		program.begin();
            lightPos.x = Gdx.input.getX();
            lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
            program.setUniformf("ambientIntensity", ambientIntensity);
            program.setUniformf("attenuation", attenuation);
            program.setUniformf("light", lightPos);
            program.setUniformi("useNormals", useNormals ? 1 : 0);
            program.setUniformi("useShadow", useShadow ? 1 : 0);
            program.setUniformf("strength", strength);
            program.setUniformMatrix("u_projTrans", transform_FG);
            texShader.setUniformMatrix("u_projView", transform_FG);
    		texShader.setUniformi("u_diffuse", 0);

            worldNormal.bind(1);
            worldTexture.bind(0);
//    		earthTexture.bind();
    		sphereMesh.render(program, GL10.GL_TRIANGLES);
    		program.end();
//    		gl.glDisable(GL20.GL_DEPTH_TEST);
//    		gl.glDisable(GL20.GL_CULL_FACE);
//    		gl.glDisable(GL20.GL_TEXTURE_2D);
            
            batch.begin();
            String str = MessageFormat.format(TEXT, ambientIntensity,
                            attenuation.x, attenuation.y, DEC_FMT.format(attenuation.z),
                            strength, lightPos.z);
            font.drawMultiLine(batch, str, 10, Gdx.graphics.getHeight()-10);
           
            font.draw(batch, "Diffuse Color", 10, IMG_Y+texHeight + 30);
            font.draw(batch, "Normal Map", texWidth+20, IMG_Y+texHeight + 30);
            font.draw(batch, "Final Color", texWidth*2+30, IMG_Y+texHeight + 30);
            batch.end();
           

		}
		
		@Override
		public void hide() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void show() {
            // load our textures
            rock = new Texture(Gdx.files.internal("data/teapot.png"));
            rock_n = new Texture(Gdx.files.internal("data/teapot_n.png"));
            teapot = new Texture(Gdx.files.internal("data/rock.png"));
            teapot_n = new Texture(Gdx.files.internal("data/rock_n.png"));
           
            texture = teapot;
            texture_n = teapot_n;
            flipY = texture==rock;
            
    		InputStream in = Gdx.files.internal("data/sphereMesh.obj").read();
    		sphereMesh = ObjLoader.loadObj(in);
    		try {
    			in.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		//sphereMesh.scale(0.35f, 0.5f, 0.35f);
    		sphereMesh.scale(300f, 350f, 300f);
    		//sphereMesh.
    		System.out.println(sphereMesh.getNumVertices()+" "+ sphereMesh.getNumIndices());
    		
    		texShader = new ShaderProgram(Gdx.files.internal("data/tex-vs.glsl"),
    				Gdx.files.internal("data/tex-fs.glsl"));
           
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
            program = createShader();

            // now we create our sprite batch for our shader
            fxBatch = new SpriteBatch(100, program);
            // setShader is needed; perhaps this is a LibGDX bug?
            fxBatch.setShader(program);
            fxBatch.setProjectionMatrix(cam.combined);
            fxBatch.setTransformMatrix(transform);

            // usually we would just use a single batch for our application,
            // but for demonstration let's also show the un-affected image
            batch = new SpriteBatch(100);
            batch.setProjectionMatrix(cam.combined);
            batch.setTransformMatrix(transform);

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
            });
           
            font = new BitmapFont();
            
            Pixmap worldPixOcean = new Pixmap(512, 256, Format.RGBA8888);
            Pixmap worldPixLand = new Pixmap(512, 256, Format.RGBA8888);
            Pixmap worldPixHeight = new Pixmap(512, 256, Format.RGB888);
            Pixmap worldPixNormal = new Pixmap(512, 256, Format.RGB888);

    		earthTexture = new Texture(Gdx.files.internal("data/sphereMesh.jpg"), Format.RGB565, true);
    		earthTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

            
            worldPixOcean.setColor(0f, 0.3f, 1f, 1f);
            worldPixOcean.fill();
            
            worldPixLand.setColor(0f, 1f, 0f, 0f);
            worldPixLand.fill();
            
            //Do noise!
            //http://stackoverflow.com/questions/4753055/perlin-noise-generation-for-terrain
            float min=0;
            float max=0;
            for (int _x = 0; _x < 512; ++_x) {
                for (int _y = 0; _y < 256; ++_y) {
                	float h = getHeight(_x,_y);

                	min = Math.min(min, h);
                	max = Math.max(max,h);
                	//Gdx.app.log("Noise",""+h);
                	
                	float height = toHeight(h);
                	worldPixHeight.setColor(height, height, height, 1f);
                	worldPixHeight.drawPixel(_x, _y);
                	
                	float green = 1f;
                	if (h > waterHeight) green = 1f;
                	else if (h > 0f) green = h * 10f;
                	else if (h < 0f) green = 0;
                	worldPixLand.setColor(0f, 1f, 0f, green);
                	worldPixLand.drawPixel(_x, _y);
                }
            }
            worldPixOcean.drawPixmap(worldPixLand, 0, 0);
            
        	Gdx.app.log("Noise","Min:"+min+" max:"+max);

            
            //make normal map
        	//http://www.gamedev.net/topic/586973-how-to-generate-normal-map-from-texture/
            for (int _x = 1; _x < (512)-1; ++_x) {
                for (int _y = 1; _y < (256)-1; ++_y) {
                	float hxm = toNormHeight(getHeight(_x-1,_y  ));
                	float hxp = toNormHeight(getHeight(_x+1,_y  ));
                	float hym = toNormHeight(getHeight(_x  ,_y-1));
                	float hyp = toNormHeight(getHeight(_x  ,_y+1));

                	Vector3 vx = new Vector3(0, 1, hxm - hxp);
                	Vector3 vy = new Vector3(1, 0, hym - hyp);
                	
                	Vector3 normal = vx.cpy();
                	normal.crs(vy); //normal = vx X vy
                	normal.nor(); //normalise
                	
                	float r = ((normal.x + 1.0f) / 2.0f) * 255f;  
                	float g = ((normal.y + 1.0f) / 2.0f) * 255f;  
                	float b = ((normal.z + 1.0f) / 2.0f) * 255f;  
                	
                    worldPixNormal.setColor(r, g, b, 1f);
                    worldPixNormal.drawPixel(_x, _y);
                }
            }

            
           // worldPix.fillRectangle(50, 50, 100, 100);
            worldTexture = new Texture(worldPixOcean);
            worldHeight = new Texture(worldPixHeight);
            worldNormal = new Texture(worldPixNormal);
            
            worldTexture = new Texture(worldPixOcean);
            worldTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            worldNormal = new Texture(worldPixNormal);
            worldNormal.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);


            worldPixOcean.dispose();
            worldPixLand.dispose();
            worldPixHeight.dispose();
            worldPixNormal.dispose();

            
			
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
		
		//NOISE VARS
		float persistence = 0.7f;
		float frequency = 0.1f;
		float amplitude  = 1f;
		int octaves = 6;
		int randomseed = 2 + 5*5;
		
		float waterHeight = 0.1f;
		
		public float getHeight(float _x, float _y) {
			return amplitude * total(_x, _y);
		}
		
		public float total(float _i, float _j) {
		    //properties of one octave (changing each loop)
			float _t = 0.0f;
			float _amplitude = 1;
			float _freq = frequency;

		    for(int k = 0; k < octaves; ++k) {
		        _t += getValue(_j * _freq + randomseed, _i * _freq + randomseed) * _amplitude;
		        _amplitude *= persistence;
		        _freq *= 2;
		    }

		    return _t;
		}


		public float getValue(float _x, float _y) {
			int Xint = (int)_x;
			int Yint = (int)_y;
			float Xfrac = _x - Xint;
			float Yfrac = _y - Yint;

			//noise values
		  	float n01 = noise(Xint-1, Yint-1);
			float n02 = noise(Xint+1, Yint-1);
			float n03 = noise(Xint-1, Yint+1);
			float n04 = noise(Xint+1, Yint+1);
			float n05 = noise(Xint-1, Yint);
			float n06 = noise(Xint+1, Yint);
			float n07 = noise(Xint, Yint-1);
			float n08 = noise(Xint, Yint+1);
			float n09 = noise(Xint, Yint);
			
			float n12 = noise(Xint+2, Yint-1);
			float n14 = noise(Xint+2, Yint+1);
			float n16 = noise(Xint+2, Yint);
			
			float n23 = noise(Xint-1, Yint+2);
			float n24 = noise(Xint+1, Yint+2);
			float n28 = noise(Xint, Yint+2);
			
			float n34 = noise(Xint+2, Yint+2);

		    //find the noise values of the four corners
			float x0y0 = 0.0625f*(n01+n02+n03+n04) + 0.125f*(n05+n06+n07+n08) + 0.25f*(n09);  
			float x1y0 = 0.0625f*(n07+n12+n08+n14) + 0.125f*(n09+n16+n02+n04) + 0.25f*(n06);  
			float x0y1 = 0.0625f*(n05+n06+n23+n24) + 0.125f*(n03+n04+n09+n28) + 0.25f*(n08);  
			float x1y1 = 0.0625f*(n09+n16+n28+n34) + 0.125f*(n08+n14+n06+n24) + 0.25f*(n04);  

		    //interpolate between those values according to the x and y fractions
			float v1 = interpolate(x0y0, x1y0, Xfrac); //interpolate in x direction (y)
			float v2 = interpolate(x0y1, x1y1, Xfrac); //interpolate in x direction (y+1)
		    float fin = interpolate(v1, v2, Yfrac);  //interpolate in y direction

		    return fin;
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