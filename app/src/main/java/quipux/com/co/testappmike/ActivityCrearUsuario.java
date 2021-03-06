package quipux.com.co.testappmike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;
import io.realm.Realm;
import quipux.com.co.testappmike.base.BaseActivity;
import quipux.com.co.testappmike.base.onClickCallback;
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
  @BindView(R.id.spinner) MaterialSpinner spinner;

  @Override public int layoutId() {
    return R.layout.nuevo_usuario;
  }

  String[] ITEMS = { UtilUsuario.ESTADO_ACTIVO, UtilUsuario.ESTADO_INACTIVO };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fechaNacimiento.setManager(getSupportFragmentManager());
    password.setText(generatePin());
    setupToolbar(toolbar, "Crear usuario", true);

    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setSelection(0);
  }

  private boolean isFormValid() {
    boolean allOk = false;

    if (fullname.getText().toString().length() > 0 && validateLength(fullname.getText().toString(),
        30)) {
      allOk = true;
      fullnameWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      fullnameWrapper.setError("Campo invalido");
      return false;
    }
    if (lastName.getText().toString().length() > 0 && validateLength(lastName.getText().toString(),
        30)) {
      allOk = true;
      lastNamePhoneWrapper.setErrorEnabled(false);
    } else {
      lastNamePhoneWrapper.setError("Campo invalido");
    }

    if (validateEmail(email.getText().toString()) && validateLength(email.getText().toString(),
        30)) {
      allOk = true;
      emailWrapper.setErrorEnabled(false);
    } else {
      allOk = false;

      emailWrapper.setError("Campo invalido");
      return false;
    }

    if (fechaNacimiento.getText().toString().length() > 0 && validateAge(
        fechaNacimiento.getText().toString()) && validateLength(
        fechaNacimiento.getText().toString(), 30)) {
      allOk = true;
      fechaWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      fechaWrapper.setError("No eres mayor de edad");
      return false;
    }

    if (validateNumbers(documento.getText().toString()) && validateLength(
        documento.getText().toString(), 30)) {
      allOk = true;
      documentoWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      documentoWrapper.setError("Campo invalido");
      return false;
    }

    if (user.getText().toString().length() > 0 && validateLength(user.getText().toString(),30)) {
      allOk = true;
      userWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      userWrapper.setError("Nombre de usuario muy corto");
      return false;
    }
    return allOk;
  }

  @OnClick(R.id.btn) public void onClick() {
    log("save");
    log("status form" + isFormValid());
    if (isFormValid()) {
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
        usuario.setCorreoEletronico(email.getText().toString());

        usuario.setRol(UtilUsuario.ROL_USURIO);
        String element = ITEMS[spinner.getSelectedItemPosition()];
        usuario.setEstado(element);

        realm1.copyToRealmOrUpdate(usuario);
        showMaterialDialog(
            getString(R.string.succes_user, usuario.getNombre(), usuario.getContrasena()),
            new onClickCallback() {
              @Override public void onPositive(boolean result) {
                // call super
              }

              @Override public void onDissmis() {
                // call super
              }

              @Override public void onNegative(boolean result) {
                // call super
              }
            });
      });
      realm.close();
    }
  }
}
