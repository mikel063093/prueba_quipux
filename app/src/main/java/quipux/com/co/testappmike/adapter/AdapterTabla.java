package quipux.com.co.testappmike.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.codecrafters.tableview.TableDataAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import quipux.com.co.testappmike.ActivityCrearProducto;
import quipux.com.co.testappmike.ActivityProducto;
import quipux.com.co.testappmike.R;
import quipux.com.co.testappmike.entity.Producto;

/**
 * Created by yeysonjimenez on 2/2/17.
 */

public class AdapterTabla extends TableDataAdapter {

  ArrayList<Producto> data;
  ActivityProducto context;

  public AdapterTabla(ActivityProducto context, ArrayList<Producto> result) {
    super(context, result);
    this.data = result;
    this.context = context;
  }

  @Override public View getCellView(final int rowIndex, int columnIndex, ViewGroup parentView) {

    Producto car = (Producto) getRowData(rowIndex);
    LayoutInflater mInflater =
        (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    View view = mInflater.inflate(R.layout.itemtable, null);
    TextView it = (TextView) view.findViewById(R.id.txtItem);
    ImageView btnEdit = (ImageView) view.findViewById(R.id.btnEdit);
    ImageView btnDelete = (ImageView) view.findViewById(R.id.btnDelete);
    //
    //Log.e("columnas",columnIndex+"");
    //
    //Log.e("filas",rowIndex+"");
    //for (ItemsProduct p:data
    //) {
    //  Log.e("dato",p.getName())
    //}
    switch (columnIndex) {
      case 0:
        it.setText(data.get(rowIndex).getName());
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        break;
      case 1:
        it.setText(data.get(rowIndex).getDescription());
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        break;
      case 2:
        it.setText(data.get(rowIndex).getEstado());
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        break;
      case 3:
        it.setText(data.get(rowIndex).getOrigen());
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        break;
      case 4:
        it.setText(data.get(rowIndex).getFecha());
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        break;
      case 5:
        it.setText(data.get(rowIndex).getCantidad());

        btnEdit.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            goMyEdit(rowIndex, data);
          }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            goDelete(rowIndex, data);
          }
        });

        break;
    }
    //TextView it = (TextView) view.findViewById(R.id.txtItem);
    //Log.e("star", ""+items.get(i).getIcon());

    //it.setText(data.get(rowIndex).getName());
    return view;
  }

  private void goDelete(final int rowIndex, final ArrayList<Producto> data) {

    context.getRealm().executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        RealmResults<Producto> res =
            realm.where(Producto.class).equalTo("name", data.get(rowIndex).getName()).findAll();

        if (!res.isEmpty()) {
          Producto item = res.first();
          item.deleteFromRealm();
          goMyProduct();
        }
      }
    });
  }

  private void goMyProduct() {
    context.configTable();
  }

  private void goMyEdit(int rowIndex, ArrayList<Producto> data) {

    Intent i = new Intent(context, ActivityCrearProducto.class);

    i.putExtra("name", data.get(rowIndex).getName());
    i.putExtra("description", data.get(rowIndex).getDescription());
    i.putExtra("estado", data.get(rowIndex).getEstado());
    i.putExtra("origen", data.get(rowIndex).getOrigen());
    i.putExtra("cant", data.get(rowIndex).getCantidad());
    context.startActivity(i);
  }
}
