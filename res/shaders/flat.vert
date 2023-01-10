//combined projection and view matrix
uniform mat4 uProjection;
uniform vec4 uColor;

//"in" attributes from our SpriteBatch
attribute vec3 Position;
attribute vec2 TexCoord;

//"out" varyings to our fragment shader
varying vec4 vColor;
varying vec2 vTexCoord;
 
void main() {
	vColor = uColor;
	vTexCoord = TexCoord;
	gl_Position = uProjection * vec4(Position, 1.0);
}