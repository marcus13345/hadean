package xyz.valnet.engine.sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

import static org.lwjgl.stb.STBVorbis.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.memFree;
import org.lwjgl.system.MemoryStack;

public class Sound {

  private int bufferId;
  private int sourceId;

  private boolean loops = false;

  // private float volume = 1.0f;

  public Sound setVolume(float vol) {
    alSourcef(sourceId, AL_GAIN, vol);
    return this;
  }

  public Sound(String path) {

    try (MemoryStack stack = stackPush()) {
      IntBuffer channelsBuffer = stack.mallocInt(1); // int*
      IntBuffer sampleRateBuffer = stack.mallocInt(1); // int*
      
      ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer);

      if(rawAudioBuffer == null) {
        System.err.println("SOUND FAILED BRUV");
        return;
      }

      int channels = channelsBuffer.get();
      int samplerate = sampleRateBuffer.get();

      int format = -1;
      if(channels == 1) {
        format = AL_FORMAT_MONO16;
      } else if (channels == 2) {
        format = AL_FORMAT_STEREO16;
      }

      bufferId = alGenBuffers();
      alBufferData(bufferId, format, rawAudioBuffer, samplerate);

      sourceId = alGenSources();

      alSourcei(sourceId, AL_BUFFER, bufferId);
      alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
      alSourcei(sourceId, AL_POSITION, 0);
      alSourcef(sourceId, AL_GAIN, 1.0f);

      memFree(rawAudioBuffer);
    }
  }

  public void play() {
    int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
    if(state == AL_PLAYING) {
      alSourceStop(sourceId);
      alSourcei(sourceId, AL_POSITION, 0);
    }
    alSourcePlay(sourceId);
  }

  public void stop() {
    int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
    if(state == AL_PLAYING) {
      alSourceStop(sourceId);
      alSourcei(sourceId, AL_POSITION, 0);
    }
  }

  public boolean isPlaying() {
    return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
  }

  // public static void start() {
  //   FileInputStream fileStream = new FileInputStream("res/sounds/p0ss/interface-sounds/appear-online.ogg");
  //   IntBuffer samples = readOffSamplesToBuffer(fileStream);
  // }

  // private static IntBuffer readOffSamplesToBuffer(FileInputStream fileStream) {
  //   // This theoretical class is what I need – something that
  //   // can read the data from the OGG file.
  //   STBVorbis.info
  //   OggReader reader = new OggReader(fileStream);

  //   // Very simplified example
  //   ShortBuffer buffer = BufferUtils.createIntBuffer(reader.getSampleCount());
  //   buffer.put(reader.samplesAsShortArray());
  //   return buffer;
  // }
}
