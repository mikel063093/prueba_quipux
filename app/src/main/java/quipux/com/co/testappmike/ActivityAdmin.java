package quipux.com.co.testappmike;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;
import java.util.ArrayList;
import quipux.com.co.testappmike.adapter.AdapterUsuario;
import quipux.com.co.testappmike.base.BaseActivity;
import quipux.com.co.testappmike.entity.Usuario;

public class ActivityAdmin extends BaseActivity {

  @BindView(R.id.toolbaradmin) Toolbar toolbar;
  @BindView(R.id.fab) FloatingActionButton fab;
  @BindView(R.id.rv_usuarios) RecyclerView rvUsuarios;
  private AdapterUsuario adapter;

  @Override public int layoutId() {
    return R.layout.activity_main_admin;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupToolbar(toolbar, "Usuarios");
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
    rvUsuarios.setNestedScrollingEnabled(false);
    rvUsuarios.setLayoutManager(mLinearLayoutManager);
  }

  @Override protected void onResume() {
    super.onResume();
    initList();
  }

  private void initList() {
    RealmResults<Usuario> usuarios = getRealm().where(Usuario.class).findAll();
    ArrayList<Usuario> listUsuarios = new ArrayList<>();
    if (!usuarios.isEmpty()) {
      for (Usuario u : usuarios) {
        listUsuarios.add(u);
      }

      adapter = new AdapterUsuario(this, listUsuarios);
    }

    if (adapter != null) {
      rvUsuarios.setAdapter(adapter);
      adapter.getPositionClicks().subscribe(integer -> {
        log("clickOn position " + integer);
        Intent intent = new Intent(this, ActivityActualizarUsuario.class);
        intent.putExtra(KEY_DATA_USER, listUsuarios.get(integer).toString());
        goActv(intent, false);
      });
    }
  }

  @OnClick(R.id.fab) public void onClick() {
    log("add user");
    goActv(ActivityCrearUsuario.class, false);
  }
}
