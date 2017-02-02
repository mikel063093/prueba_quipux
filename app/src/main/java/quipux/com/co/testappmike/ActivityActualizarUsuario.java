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

public class ActivityActualizarUsuario extends BaseActivity {

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
  String[] ITEMS = { UtilUsuario.ESTADO_ACTIVO, UtilUsuario.ESTADO_INACTIVO };

  @Override public int layoutId() {
    return R.layout.nuevo_usuario;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fechaNacimiento.setManager(getSupportFragmentManager());
    password.setText(generatePin());
    password.setEnabled(true);
    btn.setText("Actualizar");

    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    setupToolbar(toolbar, "Actualizar usuario", true);
    if (getIntent() != null
        && getIntent().getExtras() != null
        && getIntent().getExtras().getString(KEY_DATA_USER) != null) {
      String json = getIntent().getExtras().getString(KEY_DATA_USER);
      Usuario usuario = AppMain.getGson().fromJson(json, Usuario.class);
      if (usuario != null) {
        fullname.setText(usuario.getNombre());
        String apellido = usuario.getApellido() != null
            && usuario.getApellido().length() > 0
            && !usuario.getApellido().equalsIgnoreCase("null") ? usuario.getApellido() : "";
        lastName.setText(apellido);
        email.setText(usuario.getCorreoEletronico());
        fechaNacimiento.setText(usuario.getFechaNacimiento());
        documento.setText(usuario.getDocumento() + "");
        user.setText(usuario.getUsuario());
        password.setText(usuario.getContrasena());
        int position = usuario.getEstado().equalsIgnoreCase(UtilUsuario.ESTADO_ACTIVO) ? 0 : 1;
        spinner.setSelection(position);
      }
    }
  }

  private boolean isFormValid() {
    boolean allOk = false;

    if (fullname.getText().toString().length() > 0) {
      allOk = true;
      fullnameWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      fullnameWrapper.setError("Campo invalido");
      return false;
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
      return false;
    }

    if (fechaNacimiento.getText().toString().length() > 0 && validateAge(
        fechaNacimiento.getText().toString())) {
      allOk = true;
      fechaWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      fechaWrapper.setError("No eres mayor de edad");
      return false;
    }

    if (validateNumbers(documento.getText().toString())) {
      allOk = true;
      documentoWrapper.setErrorEnabled(false);
    } else {
      allOk = false;
      documentoWrapper.setError("Campo invalido");
      return false;
    }

    if (user.getText().toString().length() > 0) {
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
            getString(R.string.succes_update_user, usuario.getNombre(), usuario.getContrasena()),
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
