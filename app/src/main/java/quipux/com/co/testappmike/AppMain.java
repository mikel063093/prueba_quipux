package quipux.com.co.testappmike;

import android.app.Application;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import quipux.com.co.testappmike.entity.Usuario;
import quipux.com.co.testappmike.util.UtilUsuario;

/**
 * Created by home on 2/1/17.
 */

public class AppMain extends Application {
  private static GsonBuilder gsonBuilder = new GsonBuilder();
  private static Gson gson = gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
    @Override public boolean shouldSkipField(@NonNull FieldAttributes f) {
      return f.getDeclaringClass().equals(RealmObject.class);
    }

    @Override public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }
  }).create();

  @Override public void onCreate() {
    super.onCreate();
    Logger.init("TestApp")
        .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
        .methodCount(1);

    Logger.e("onCreate App");

    Realm.init(this);
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
    Realm.setDefaultConfiguration(realmConfiguration);
    crearAdmin();
  }

  public static Gson getGson() {
    return gson;
  }

  private void crearAdmin() {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(realm1 -> {
      Usuario usuario = new Usuario();
      usuario.setNombre("admin");
      usuario.setContrasena("root");
      usuario.setUsuario("admin");
      usuario.setDocumento(-1);
      usuario.setCorreoEletronico("root@root.com");
      usuario.setFechaNacimiento("01/01/1993");
      usuario.setRol(UtilUsuario.ROL_ADMIN);
      usuario.setEstado(UtilUsuario.ESTADO_ACTIVO);

      realm1.copyToRealmOrUpdate(usuario);
    });
    realm.close();
  }
}
