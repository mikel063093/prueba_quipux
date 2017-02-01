package quipux.com.co.testappmike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;
import io.realm.Realm;
import quipux.com.co.testappmike.base.BaseActivity;
import quipux.com.co.testappmike.entity.Usuario;
import quipux.com.co.testappmike.util.UtilUsuario;

public class ActivityCrearUsuario extends BaseActivity {

  @BindView(R.id.toolbar_registro) Toolbar toolbar;
  @BindView(R.id.fullname) EditText fullname;
  @BindView(R.id.fullnameWrapper) TextInputLayout fullnameWrapper;
  @BindView(R.id.lastName) EditText lastName;
  @BindView(R.id.lastNamePhoneWrapper) TextInputLayout lastNamePhoneWrapper;
  @BindView(R.id.email) EditText email;
  @BindView(R.id.emailWrapper) TextInputLayout emailWrapper;
  @BindView(R.id.username_) EditText user;
  @BindView(R.id.userName_Wrapper) TextInputLayout userWrapper;
  @BindView(R.id.password) EditText password;
  @BindView(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @BindView(R.id.fecha_nacimiento) DatePickerInputEditText fechaNacimiento;
  @BindView(R.id.fechaWrapper) TextInputLayout fechaWrapper;
  @BindView(R.id.documento) EditText documento;
  @BindView(R.id.documentoWrapper) TextInputLayout documentoWrapper;
  @BindView(R.id.btn) Button btn;

  @Override public int layoutId() {
    return R.layout.nuevo_usuario;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fechaNacimiento.setManager(getSupportFragmentManager());
    password.setText(generatePin());
  }

  @OnClick(R.id.btn) public void onClick() {
    log("save");
    boolean allOk;
    if (fullname.getText().toString().length() > 0) {
      allOk = true;
      fullnameWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      fullnameWrapper.setError("Campo invalido");
    }
    if (lastName.getText().toString().length() > 0) {
      allOk = true;
      lastNamePhoneWrapper.setErrorEnabled(false);
    } else {
      lastNamePhoneWrapper.setError("Campo invalido");
    }

    if (validateEmail(email.getText().toString())) {
      allOk = true;
      emailWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      emailWrapper.setError("Campo invalido");
    }

    // validar fecha
    if (fechaNacimiento.getText().toString().length() > 0) {
      allOk = true;
    } else {
      allOk = false;
      fullnameWrapper.setError("Campo invalido");
    }

    if (validateNumbers(documento.getText().toString())) {
      allOk = true;
      documentoWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      documentoWrapper.setError("Campo invalido");
    }

    if (user.getText().toString().length() > 0) {
      allOk = true;
      userWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      userWrapper.setError("Nombre de usuario muy corto");
    }
    if (allOk) {
      log("user datos ok");

      Realm realm = Realm.getDefaultInstance();
      realm.executeTransaction(realm1 -> {
        Usuario usuario = new Usuario();

        usuario.setNombre(fullname.getText().toString());
        usuario.setApellido(
            lastName.getText().toString().length() >= 0 ? lastName.getText().toString() : null);
        usuario.setCorreoEletronico(email.getText().toString());
        usuario.setDocumento(Integer.parseInt(documento.getText().toString()));
        usuario.setFechaNacimiento(fechaNacimiento.getText().toString());
        usuario.setContrasena(password.getText().toString());
        usuario.setUsuario(user.getText().toString());

        usuario.setRol(UtilUsuario.ROL_USURIO);
        usuario.setEstado(UtilUsuario.ESTADO_ACTIVO);

        realm1.copyToRealmOrUpdate(usuario);
      });
      realm.close();
    }
  }
}
