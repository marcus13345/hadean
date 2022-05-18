//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;

//"in" varyings from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

void main() {
  vec4 texColor = texture2D(u_texture, vTexCoord);
  if(texColor == vec4(1, 0, 1, 1) || texColor == vec4(1, 0, 0, 1)) {
    gl_FragColor = vec4(0, 0, 0, 0);
  } else {
    gl_FragColor = texColor * vColor;
  }
}