package quipux.com.co.testappmike;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import quipux.com.co.testappmike.base.BaseActivity;
import quipux.com.co.testappmike.base.onClickCallback;
import quipux.com.co.testappmike.entity.Usuario;
import quipux.com.co.testappmike.util.UtilUsuario;

public class LoginActivity extends BaseActivity {

  @BindView(R.id.username) EditText username;
  @BindView(R.id.password) EditText password;

  @Override public int layoutId() {
    return R.layout.login;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @OnClick(R.id.btn) public void onClick() {
    validarUsuario(username.getText().toString(), password.getText().toString());
  }

  private void validarUsuario(String usuario, String password) {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Usuario> results = realm.where(Usuario.class)
        .equalTo("usuario", usuario)
        .equalTo("contrasena", password)
        .findAll();
    if (!results.isEmpty()) {
      Usuario user = results.first();
      if (user.getRol().equalsIgnoreCase(UtilUsuario.ROL_ADMIN)) {
        log("login usuario admin");
        goActv(MainAdmin.class, true);
      } else {
        Toast.makeText(this, "Usuario: " + user.getNombre(), Toast.LENGTH_SHORT).show();
      }
    } else {
      showMaterialDialog(getString(R.string.datos_invaldiso), new onClickCallback() {
        @Override public void onPositive(boolean result) {
          // call superr
        }

        @Override public void onDissmis() {
          // call super
        }

        @Override public void onNegative(boolean result) {
          // call super
        }
      });
    }
    realm.close();
  }
}
