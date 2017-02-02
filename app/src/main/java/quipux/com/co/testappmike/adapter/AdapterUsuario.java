package quipux.com.co.testappmike.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import quipux.com.co.testappmike.R;
import quipux.com.co.testappmike.entity.Usuario;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by home on 2/2/17.
 */

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> {
  private Context mContext;
  private ArrayList<Usuario> usuarios;
  private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

  public AdapterUsuario(Context mContext, ArrayList<Usuario> usuarios) {
    this.mContext = mContext;
    this.usuarios = usuarios;
  }

  @NonNull public Observable<Integer> getPositionClicks() {
    return onClickSubject.asObservable();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(mContext).inflate(R.layout.item_usuario, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Usuario usuario = usuarios.get(position);
    if (usuario != null) {
      assert holder.txtEstado != null;
      holder.txtEstado.setText(
          mContext.getString(R.string.state_user, usuario.getRol(), usuario.getEstado()));
      assert holder.txtNombre != null;
      String apellido = usuario.getApellido() != null
          && usuario.getApellido().length() > 0
          && !usuario.getApellido().equalsIgnoreCase("null") ? usuario.getApellido() : "";
      holder.txtNombre.setText(usuario.getNombre() + " " + apellido);

      assert holder.rootSection != null;
      holder.rootSection.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtNombre.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtEstado.setOnClickListener(view -> onClickSubject.onNext(position));
    }
  }

  @Override public int getItemCount() {
    return usuarios != null ? usuarios.size() : 0;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Nullable @BindView(R.id.txt_estado) AppCompatTextView txtEstado;
    @Nullable @BindView(R.id.txt_nombre) AppCompatTextView txtNombre;
    @Nullable @BindView(R.id.root_section) CardView rootSection;

    ViewHolder(@NonNull View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
