package xyz.valnet.engine.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.lang.Math;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Names {

  public static class NamesYaml {
    public List<Name> names;

    public static class Name {
      public String name;
      public String sex;
    }
  }

  private static Names instance;

  private List<NamesYaml.Name> names;

  public static void loadNames() {
    instance = new Names();
  }


  private void load() {
    InputStream fileStream = null;
    try {
      // Constructor constructor = new Constructor();
      // TypeDescription typeDescription = new TypeDescription(NameYaml.class);
      // // typeDescription.putListPropertyType("things", ConfigurableThing.class);
      // constructor.addTypeDescription(typeDescription);

      Yaml yaml = new Yaml();
      fileStream = new FileInputStream("res/names.yaml");
      var loaded = yaml.loadAs(fileStream, NamesYaml.class).names;
      this.names = loaded;
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (fileStream != null) {
      try {
        fileStream.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private Names() {
    load();
  }

  public static String getRandomName() {
    return instance.names.get((int)Math.floor(Math.random() * instance.names.size())).name;
  }
}
