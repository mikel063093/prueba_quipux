package quipux.com.co.testappmike;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import io.realm.RealmResults;
import java.util.ArrayList;
import quipux.com.co.testappmike.adapter.AdapterTabla;
import quipux.com.co.testappmike.base.BaseActivity;
import quipux.com.co.testappmike.entity.Producto;

/**
 * Created by yeysonjimenez on 2/1/17.
 */
public class ActivityProducto extends BaseActivity implements View.OnClickListener {

  private FloatingActionButton addItem;
  private AdapterTabla adapter;

  @Override public int layoutId() {
    return R.layout.productos;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectView();
    Intent intent = getIntent();
    String id = intent.getStringExtra("roll");
    setTitle(id);
  }

  private void injectView() {
    addItem = (FloatingActionButton) findViewById(R.id.fab);
    addItem.setOnClickListener(this);
  }

  @Override protected void onResume() {
    super.onResume();
    configTable();
  }

  public void configTable() {
    TableView tableView = (TableView) findViewById(R.id.tableView);
    tableView.setColumnCount(6);
    SimpleTableHeaderAdapter simpleTableHeaderAdapter =
        new SimpleTableHeaderAdapter(this, "Nombre", "Descripcion", "Estado", "Origen", "fecha",
            "cantidad");
    simpleTableHeaderAdapter.setTextColor(this.getResources().getColor(R.color.primary));
    tableView.setHeaderAdapter(simpleTableHeaderAdapter);

    RealmResults<Producto> result = getRealm().where(Producto.class).findAll();
    ArrayList<Producto> list = new ArrayList<>();
    if (!result.isEmpty()) {
      for (Producto item : result) {
        list.add(item);
        Log.e("ItemProduct add", item.getName());
      }
      adapter = new AdapterTabla(this, list);
      tableView.setDataAdapter(adapter);
    }
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {

      case R.id.fab:
        goaddItem();
        break;
    }
  }

  private void goaddItem() {
    Intent intent = new Intent(this, ActivityCrearProducto.class);
    startActivity(intent);
  }
}
