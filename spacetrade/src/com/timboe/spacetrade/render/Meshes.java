package com.timboe.spacetrade.render;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.timboe.spacetrade.ObjLoaderTan;
import com.timboe.spacetrade.enumerator.WorldSize;

public class Meshes {

	static Mesh planetSmall = null;
	static Mesh planetMedium = null;
	static Mesh planetLarge = null;
	
	static Mesh pinkTube = null;
	
	public static Mesh getPinkTube() {
		if (pinkTube == null) {
			InputStream in = Gdx.files.internal("data/pinkTube.obj").read();
			pinkTube = ObjLoader.loadObj(in);
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pinkTube.scale(150f, 150f, 150f);
		}
		return pinkTube;
	}
	
	public static Mesh getPlanet(WorldSize _ws) {
		if (planetSmall == null) init();
		switch (_ws) {
			case Small: return planetSmall;
			case Medium: return planetMedium;
			default: return planetLarge;
		}
	}

	public static void dispose() {
		if (planetSmall != null) planetSmall.dispose();
		if (planetMedium != null) planetMedium.dispose();
		if (planetLarge != null) planetLarge.dispose();
	}
	
	private static void init() {
		InputStream in = Gdx.files.internal("data/sphereB3D.obj").read();
		planetMedium = ObjLoaderTan.loadObj(in);
		planetMedium.getVertexAttribute(Usage.Position).alias = "a_vertex";
		planetMedium.getVertexAttribute(Usage.Normal).alias = "a_normal";
		planetMedium.getVertexAttribute(10).alias = "a_tangent";
		planetMedium.getVertexAttribute(11).alias = "a_binormal";
		planetMedium.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texcoord0";
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		in = Gdx.files.internal("data/sphereB3D.obj").read();
		planetSmall = ObjLoaderTan.loadObj(in);
		planetSmall.getVertexAttribute(Usage.Position).alias = "a_vertex";
		planetSmall.getVertexAttribute(Usage.Normal).alias = "a_normal";
		planetSmall.getVertexAttribute(10).alias = "a_tangent";
		planetSmall.getVertexAttribute(11).alias = "a_binormal";
		planetSmall.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texcoord0";
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		in = Gdx.files.internal("data/sphereB3D.obj").read();
		planetLarge = ObjLoaderTan.loadObj(in);
		planetLarge.getVertexAttribute(Usage.Position).alias = "a_vertex";
		planetLarge.getVertexAttribute(Usage.Normal).alias = "a_normal";
		planetLarge.getVertexAttribute(10).alias = "a_tangent";
		planetLarge.getVertexAttribute(11).alias = "a_binormal";
		planetLarge.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texcoord0";
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		planetSmall.scale(300f * WorldSize.Small.getSizeMod(), 
		350f * WorldSize.Small.getSizeMod(), 
		300f * WorldSize.Small.getSizeMod());	
		
		planetMedium.scale(300f, 350f, 300f);	

		planetLarge.scale(300f * WorldSize.Large.getSizeMod() * 0.9f, 
		350f * WorldSize.Large.getSizeMod() * 0.9f, 
		300f * WorldSize.Large.getSizeMod() * 0.9f); //reduce this a bit
		
//		InputStream in = Gdx.files.internal("data/sphere.obj").read();
//		planetMedium = ObjLoader.loadObj(in);
//		try {
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		planetMedium.scale(300f, 350f, 300f);	
//		
//		in = Gdx.files.internal("data/sphere.obj").read();
//		planetSmall = ObjLoader.loadObj(in);
//		try {
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		planetSmall.scale(300f * WorldSize.Small.getSizeMod(), 
//				350f * WorldSize.Small.getSizeMod(), 
//				300f * WorldSize.Small.getSizeMod());		
//
//		in = Gdx.files.internal("data/sphere.obj").read();
//		planetLarge = ObjLoader.loadObj(in);
//		try {
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		planetLarge.scale(300f * WorldSize.Large.getSizeMod() * 0.9f, 
//				350f * WorldSize.Large.getSizeMod() * 0.9f, 
//				300f * WorldSize.Large.getSizeMod() * 0.9f); //reduce this a bit
	}
	
    public static ShaderProgram createShader() {
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
        //System.out.println("VERTEX PROGRAM:\n------------\n\n"+vert);
        //System.out.println("FRAGMENT PROGRAM:\n------------\n\n"+frag);
        ShaderProgram program = new ShaderProgram(vert, frag);
        // u_proj and u_trans will not be active but SpriteBatch will still try to set them...
        ShaderProgram.pedantic = false;
        if (program.isCompiled() == false)
                throw new IllegalArgumentException("couldn't compile shader: "
                                + program.getLog());
        
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
        boolean flipY = false;

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
}
