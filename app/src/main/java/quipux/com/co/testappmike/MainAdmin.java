package quipux.com.co.testappmike;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import quipux.com.co.testappmike.base.BaseActivity;

public class MainAdmin extends BaseActivity {

  @BindView(R.id.toolbaradmin) Toolbar toolbar;
  @BindView(R.id.fab) FloatingActionButton fab;

  @Override public int layoutId() {
    return R.layout.activity_main_admin;
  }

  @OnClick(R.id.fab) public void onClick() {
    log("add user");
    goActv(ActivityCrearUsuario.class, false);
  }
}
