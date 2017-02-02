package quipux.com.co.testappmike.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import io.realm.Realm;
import io.realm.RealmResults;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import quipux.com.co.testappmike.R;
import quipux.com.co.testappmike.entity.Producto;
import quipux.com.co.testappmike.entity.Usuario;
import quipux.com.co.testappmike.util.UtilUsuario;

/**
 * Created by home on 2/1/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
  private Realm realm;
  private MaterialDialog materialDialog;
  private MaterialDialog loading;
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private static final String NAME_PATTERN = "^[\\\\p{L} .'-]+$";
  private static final String NUMBER_PATTERN = "[0-9]+";

  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  private Pattern patternName = Pattern.compile(NAME_PATTERN);
  private Pattern patternNumber = Pattern.compile(NUMBER_PATTERN);

  public static final String KEY_DATA_USER = "usuario";

  public abstract int layoutId();

  private boolean isOnPause;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layoutId());
    ButterKnife.bind(this);
    realm = Realm.getDefaultInstance();
    final RealmResults<Usuario> usuarios = realm.where(Usuario.class).findAll();
    usuarios.addChangeListener(element -> log("cantidad de usuarios " + element.size()));
  }

  @Override protected void onResume() {
    super.onResume();
    isOnPause = false;
  }

  @Override protected void onPause() {
    super.onPause();
    isOnPause = true;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    realm.close();
  }

  public boolean isOnPause() {
    return isOnPause;
  }

  public Realm getRealm() {
    realm = realm != null && !realm.isClosed() ? realm = Realm.getDefaultInstance() : realm;
    return realm;
  }

  public Usuario getUserSync() {
    return getRealm() != null ? getRealm().where(Usuario.class).findFirst() : null;
  }

  public void actulizarUsuario(final Usuario upUser) {
    log("updateRealmUser");
    final RealmResults<Usuario> result =
        realm.where(Usuario.class).equalTo("documento", upUser.getDocumento()).findAll();
    if (!result.isEmpty()) {
      getRealm().executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          Usuario usuario = result.first();
          if (upUser.getNombre() != null) {
            usuario.setNombre(upUser.getNombre());
          }
          realm.copyToRealmOrUpdate(usuario);
        }
      });
    }
  }

  public void guardarUsuario(final Usuario usuario) {
    usuario.setRol(UtilUsuario.ROL_USURIO);
    usuario.setEstado(UtilUsuario.ESTADO_ACTIVO);

    getRealm().executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(usuario);
      }
    });
  }

  public String generatePin() {
    List<Integer> numbers = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      numbers.add(i);
    }

    Collections.shuffle(numbers);

    String result = "";
    for (int i = 0; i < 4; i++) {
      result += numbers.get(i).toString();
    }
    return result;
  }

  public void elimiarUsuario(final Usuario upUser) {
    log("updateRealmUser");
    final RealmResults<Usuario> result =
        realm.where(Usuario.class).equalTo("documento", upUser.getDocumento()).findAll();
    if (!result.isEmpty()) {
      getRealm().executeTransaction(realm1 -> {
        Usuario usuario = result.first();
        usuario.deleteFromRealm();
      });
    } else {
      log("usuario no encontrado" + upUser.toString());
    }
  }

  public void log(String log) {
    Logger.e(log);
  }

  public void showDialog(@NonNull String text) {
    runOnUiThread(() -> {
      if (!isOnPause()) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title(R.string.app_name)
            .content(text)
            .progress(true, 0)
            .autoDismiss(true)
            .progressIndeterminateStyle(true);
        loading = builder.build();
        if (!isOnPause) loading.show();
      }
    });
  }

  public void dismissDialog() {
    runOnUiThread(() -> {
      if (loading != null && !isOnPause) loading.dismiss();
    });
  }

  public void showMaterialDialog(@NonNull String body, final onClickCallback onClickCallback) {
    dismissDialog();
    if (!isOnPause) {
      clearMdialog();
      materialDialog = new MaterialDialog.Builder(this).title("App prueba")
          .content(body)
          .positiveText(getString(R.string.acept))
          .negativeText(getString(R.string.cancel))
          .dismissListener(dialog -> {
            if (onClickCallback != null) {
              onClickCallback.onDissmis();
            }
          })
          .onPositive((dialog, which) -> {
            if (onClickCallback != null) {
              onClickCallback.onPositive(true);
            }
          })
          .onNegative((dialog, which) -> {
            if (onClickCallback != null) {
              onClickCallback.onNegative(true);
            }
          })
          .onNeutral((dialog, which) -> {
            if (onClickCallback != null) {
              onClickCallback.onPositive(true);
            }
          })
          .show();
    }
  }

  private void clearMdialog() {
    if (materialDialog != null && materialDialog.isShowing()) {
      materialDialog.dismiss();
      materialDialog = null;
    }
  }

  public void showErr(@NonNull String text) {
    dismissDialog();
    runOnUiThread(() -> showMaterialDialog(text, new onClickCallback() {
      @Override public void onPositive(boolean result) {

      }

      @Override public void onDissmis() {

      }

      @Override public void onNegative(boolean result) {

      }
    }));
  }

  public void goActv(Class<?> cls, boolean clear) {
    Intent intent = new Intent(getApplicationContext(), cls);
    if (clear) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  public void goActv(@NonNull Intent intent, boolean clear) {
    if (clear) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  public boolean validateEmail(@NonNull String email) {
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validateEmail(@NonNull CharSequence email) {
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validateName(@NonNull CharSequence name) {
    Matcher matcher = patternName.matcher(name);
    return matcher.matches();
  }

  public boolean validateName(@NonNull String name) {
    Matcher matcher = patternName.matcher(name);
    return matcher.matches();
  }

  public boolean validateFirstName(@NonNull String firstName) {
    return firstName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
  }

  public boolean validateLastName(@NonNull String lastName) {
    return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
  }

  public boolean validateNumbers(@NonNull String number) {
    Matcher matcher = patternNumber.matcher(number);
    return matcher.matches();
  }

  public boolean validatePassword(@NonNull String password) {
    return password.length() >= 4;
  }

  public void setupToolbar(Toolbar toolbar, String title, boolean showBack) {
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(false);
      getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
      getSupportActionBar().setDisplayShowHomeEnabled(showBack);
      TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
      txtTitle.setText(title != null ? title : "TestApp");
    }
  }

  public void setupToolbar(Toolbar toolbar, String title) {
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(false);
      TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
      txtTitle.setText(title != null ? title : "TestApp");
    }
  }

  public boolean isAdult(Date date) {

    int years = 0;
    int months = 0;
    int days = 0;
    //create calendar object for birth day
    Calendar birthDay = Calendar.getInstance();
    birthDay.setTimeInMillis(date.getTime());
    //create calendar object for current day
    long currentTime = System.currentTimeMillis();
    Calendar now = Calendar.getInstance();
    now.setTimeInMillis(currentTime);
    //Get difference between years
    years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
    int currMonth = now.get(Calendar.MONTH) + 1;
    int birthMonth = birthDay.get(Calendar.MONTH) + 1;
    //Get difference between months
    months = currMonth - birthMonth;
    //if month difference is in negative then reduce years by one and calculate the number of months.
    if (months < 0) {
      years--;
      months = 12 - birthMonth + currMonth;
      if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) months--;
    } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
      years--;
      months = 11;
    }
    //Calculate the days
    if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE)) {
      days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
    } else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
      int today = now.get(Calendar.DAY_OF_MONTH);
      now.add(Calendar.MONTH, -1);
      days =
          now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
    } else {
      days = 0;
      if (months == 12) {
        years++;
        months = 0;
      }
    }
    log("edad" + years);
    return years >= 18;
  }

  public boolean validateAge(String dateFormat) {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    try {
      Date birthDate = sdf.parse(dateFormat);
      return isAdult(birthDate);
    } catch (ParseException e) {
      Logger.e(e.getMessage(), e);
      return false;
    }
  }

  public boolean validateLength(String text, int max) {
    return text != null && text.length() <= max;
  }

  public void guardarItem(final Producto item) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(item);
      }
    });
    realm.close();
  }
}
